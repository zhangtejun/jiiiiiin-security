package cn.jiiiiiin.module.mngauth.service.impl;

import cn.jiiiiiin.module.common.dto.mngauth.RoleDto;
import cn.jiiiiiin.module.common.entity.mngauth.Role;
import cn.jiiiiiin.module.common.enums.common.ChannelEnum;
import cn.jiiiiiin.module.common.mapper.mngauth.RoleMapper;
import cn.jiiiiiin.module.mngauth.service.IRoleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.NonNull;
import lombok.val;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author jiiiiiin
 * @since 2018-09-27
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public IPage<RoleDto> pageDto(@NonNull Page<RoleDto> roleDtoPage, @NonNull ChannelEnum channel, String authorityName) {
        return roleMapper.selectPageDto(roleDtoPage, channel, authorityName);
    }

    @Transactional
    @Override
    public Boolean save(Role role, Long[] resourceIds) {
        val res = SqlHelper.retBool(roleMapper.insert(role));
        _insertOrUpdateRelationResourceRecords(role, resourceIds);
        return res;
    }

    @Transactional
    @Override
    public Boolean update(Role role, Long[] resourceIds) {
        val res = SqlHelper.retBool(roleMapper.updateById(role));
        _insertOrUpdateRelationResourceRecords(role, resourceIds);
        return res;
    }

    private void _insertOrUpdateRelationResourceRecords(Role role, Long[] resourceIds) {
        _clearRelationResourceRecords(role);
        if (resourceIds.length > 0) {
            // 添加/更新角色资源element-ui树形控件选择记录关联表
            roleMapper.insertRelationEleUiResourceRecords(role.getId(), StringUtils.join(resourceIds, ","));
            // 添加/更新角色资源记录关联表
            roleMapper.insertRelationResourceRecords(role);
        }
    }

    private void _clearRelationResourceRecords(Role role) {
        val idList = new ArrayList<Long>();
        idList.add(role.getId());
        roleMapper.deleteRelationResourceRecords(idList);
        roleMapper.deleteRelationResourceEleUiResourceRecord(role.getId());
    }

    @Override
    public RoleDto getRoleAndRelationRecords(Long id) {
        val role = roleMapper.selectRoleAndRelationRecords(id);
        val arr = new ArrayList<String>(role.getResources().size());
        role.getResources().forEach(item -> arr.add(String.valueOf(item.getId())));
        val temp = new String[arr.size()];
        val keys = arr.toArray(temp);
        role.setCheckedKeys(keys);
        role.setExpandedKeys(keys);
        return role;
    }

    @Override
    public RoleDto getRoleAndRelationEleUiResourceRecords(Long id) {
        val role = roleMapper.selectRoleAndRelationEleUiResourceRecords(id);
        val resIds = role.getResourceIds();
        if (StringUtils.isNotEmpty(resIds)) {
            val temp = role.getResourceIds().split(",");
            role.setCheckedKeys(temp);
            role.setExpandedKeys(temp);
        }
        return role;
    }

    @Transactional
    @Override
    public Boolean remove(Collection<? extends Serializable> idList) {
        roleMapper.deleteRelationAdminRecords(idList);
        roleMapper.deleteRelationResourceRecords(idList);
        roleMapper.deleteRelationResourceEleUiResourceRecords(idList);
        return SqlHelper.delBool(roleMapper.deleteBatchIds(idList));
    }

}

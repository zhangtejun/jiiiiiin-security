package cn.jiiiiiin.module.mngauth.service.impl;

import cn.jiiiiiin.module.common.dto.mngauth.RoleDto;
import cn.jiiiiiin.module.common.entity.mngauth.Resource;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
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

    @Override
    public Boolean save(Role role, Long[] resourceIds) {
        role.insert();
        _parseResrouceIds2RoleProperty(role, resourceIds);
        _clearRelationResourceRecords(role);
        return SqlHelper.retBool(roleMapper.insertRelationResourceRecords(role));
    }

    @Override
    public Boolean update(Role role, Long[] resourceIds) {
        role.updateById();
        _parseResrouceIds2RoleProperty(role, resourceIds);
        _clearRelationResourceRecords(role);
        return SqlHelper.retBool(roleMapper.insertRelationResourceRecords(role));
    }

    private void _parseResrouceIds2RoleProperty(Role role, Long[] resourceIds) {
        val resList = new ArrayList<Resource>(resourceIds.length);
        for (Long id : resourceIds) {
            resList.add((Resource) new Resource().setId(id));
        }
        role.setResources(resList);
    }

    private void _clearRelationResourceRecords(Role role) {
        val idList = new ArrayList<Long>();
        idList.add(role.getId());
        roleMapper.deleteRelationResourceRecords(idList);
    }

    @Override
    public Role getRoleAndRelationRecords(Long id) {
        return roleMapper.selectRoleAndRelationRecords(id);
    }

    @Transactional
    @Override
    public Boolean remove(Collection<? extends Serializable> idList) {
        roleMapper.deleteRelationAdminRecords(idList);
        roleMapper.deleteRelationResourceRecords(idList);
        return SqlHelper.delBool(roleMapper.deleteBatchIds(idList));
    }

}

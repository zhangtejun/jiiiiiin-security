package cn.jiiiiiin.module.mngauth.service.impl;

import cn.jiiiiiin.data.orm.entity.BaseEntity;
import cn.jiiiiiin.module.common.dto.mngauth.RoleDto;
import cn.jiiiiiin.module.common.entity.mngauth.Resource;
import cn.jiiiiiin.module.common.entity.mngauth.Role;
import cn.jiiiiiin.module.common.enums.common.ChannelEnum;
import cn.jiiiiiin.module.common.exception.BusinessErrException;
import cn.jiiiiiin.module.common.mapper.mngauth.ResourceMapper;
import cn.jiiiiiin.module.common.mapper.mngauth.RoleMapper;
import cn.jiiiiiin.module.mngauth.service.IRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public IPage<RoleDto> pageDto(Page<RoleDto> roleDtoPage, ChannelEnum channel, Role role) {
        return roleMapper.selectPageDto(roleDtoPage, channel, role);
    }

    @Transactional
    @Override
    public Boolean save(Role role, Long[] resourceIds) {
        Role.checkRootRole(role, "不能创建和系统管理员角色相同名称或相同标识的记录");
        _saveCheckRoleUniqueness(role);
        val res = SqlHelper.retBool(roleMapper.insert(role));
        _insertOrUpdateRelationResourceRecords(role, resourceIds);
        return res;
    }

    private void _saveCheckRoleUniqueness(Role role) {
        if (null != roleMapper.selectOne(new QueryWrapper<Role>().eq(Role.AUTHORITY_NAME, role.getAuthorityName()))) {
            throw new BusinessErrException(String.format("系统已经存在角色标识为【%s】的记录", role.getAuthorityName()));
        }
        if (null != roleMapper.selectOne(new QueryWrapper<Role>().eq(Role.NAME, role.getName()))) {
            throw new BusinessErrException(String.format("系统已经存在角色名字为【%s】的记录", role.getAuthorityName()));
        }
    }

    private void _updateCheckRoleUniqueness(Role role) {
        val currentRecord = roleMapper.selectById(role);
        if (!currentRecord.getAuthorityName().equals(role.getAuthorityName())) {
            if (null != roleMapper.selectOne(new QueryWrapper<Role>().eq(Role.AUTHORITY_NAME, role.getAuthorityName()))) {
                throw new BusinessErrException(String.format("系统已经存在角色标识为【%s】的记录", role.getAuthorityName()));
            }
        }
        if (!currentRecord.getName().equals(role.getName())) {
            if (null != roleMapper.selectOne(new QueryWrapper<Role>().eq(Role.NAME, role.getName()))) {
                throw new BusinessErrException(String.format("系统已经存在角色名字为【%s】的记录", role.getAuthorityName()));
            }
        }

    }

    @Transactional
    @Override
    public Boolean update(Role role, Long[] resourceIds) {
        Role.checkRootRole(role, "系统管理员角色不允许修改");
        _updateCheckRoleUniqueness(role);
        val res = SqlHelper.retBool(roleMapper.updateById(role));
        _insertOrUpdateRelationResourceRecords(role, resourceIds);
        return res;
    }

    private void _insertOrUpdateRelationResourceRecords(Role role, Long[] resourceIds) {
        _clearRelationResourceRecords(role);
        if (resourceIds.length > 0) {
            // 添加/更新角色资源element-ui树形控件选择记录关联表
            // String.join(",", List<String>)
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
            // 前端更新角色资源记录时候需要使用资源的`pids`
            val resources = resourceMapper.selectList(new QueryWrapper<Resource>().in(BaseEntity.ID, temp));
            resources.forEach(item -> role.getResources().add(item));
        }
        return role;
    }

    @Transactional
    @Override
    public Boolean remove(Collection<? extends Serializable> idList) {
        // 检查是否为`系统管理员角色`
        Role.checkRootRole(idList, "系统管理员角色不允许删除");
        roleMapper.deleteRelationAdminRecords(idList);
        roleMapper.deleteRelationResourceRecords(idList);
        roleMapper.deleteRelationResourceEleUiResourceRecords(idList);
        return SqlHelper.delBool(roleMapper.deleteBatchIds(idList));
    }

}

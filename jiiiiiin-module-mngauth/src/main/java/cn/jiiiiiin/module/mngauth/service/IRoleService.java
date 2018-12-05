package cn.jiiiiiin.module.mngauth.service;

import cn.jiiiiiin.module.common.dto.mngauth.RoleDto;
import cn.jiiiiiin.module.common.entity.mngauth.Role;
import cn.jiiiiiin.module.common.enums.common.ChannelEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author jiiiiiin
 * @since 2018-09-27
 */
public interface IRoleService extends IService<Role> {

    /**
     * 批量删除角色及其关联资源记录
     *
     * @param idList
     * @return
     */
    Boolean remove(Collection<? extends Serializable> idList);

    /**
     * 添加角色及其关联资源记录
     *
     * @param role 角色及其资源记录（resources属性）
     * @param resourceIds element-ui用户选择的资源记录
     * @return
     */
    Boolean save(Role role, Long[] resourceIds);

    /**
     *
     * @param role 角色及其资源记录（resources属性）
     * @param resourceIds element-ui用户选择的资源记录
     * @return
     */
    Boolean update(Role role, Long[] resourceIds);

    /**
     * 获取角色和其关联的记录
     *
     * @param id
     * @return
     */
    RoleDto getRoleAndRelationRecords(Long id);

    IPage<RoleDto> pageDto(Page<RoleDto> roleDtoPage, ChannelEnum channel, String authorityName);

    RoleDto getRoleAndRelationEleUiResourceRecords(Long id);
}

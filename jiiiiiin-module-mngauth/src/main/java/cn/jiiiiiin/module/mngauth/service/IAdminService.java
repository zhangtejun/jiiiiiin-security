package cn.jiiiiiin.module.mngauth.service;

import cn.jiiiiiin.module.common.entity.mngauth.Admin;
import cn.jiiiiiin.module.common.enums.common.ChannelEnum;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author jiiiiiin
 * @since 2018-09-27
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 除了下面两种角色，将会load对应用户具有角色的资源信息集合
     * {@link cn.jiiiiiin.security.rbac.component.dict.RbacDict#ROLE_ADMIN_AUTHORITY_NAME}
     * {@link cn.jiiiiiin.security.rbac.component.dict.RbacDict#ROLE_DB_ADMIN_AUTHORITY_NAME}
     * <p>
     * 且区分不同的渠道
     *
     * @param username
     * @param channel  {@link cn.jiiiiiin.module.common.entity.mngauth.Resource#channel}
     * @return
     */
    Admin signInByUsername(@NonNull String username, ChannelEnum channel);

    /**
     * spring 事务：
     * https://www.ibm.com/developerworks/cn/java/j-master-spring-transactional-use/index.html
     * http://blog.didispace.com/springboottransactional/
     *
     * @param admin
     * @return
     */
    @Transactional
    boolean saveRelationRoleRecords(@NonNull Admin admin);

    /**
     * 保存用户和关联的角色记录
     *
     * @param admin
     * @return
     */
    Boolean saveAdminAndRelationRecords(@NonNull Admin admin);

    /**
     * 更新用户和关联的角色记录
     *
     * @param admin
     * @return
     */
    Boolean updateAdminAndRelationRecords(@NonNull Admin admin);

    /**
     * 删除和用户相关的记录和用户记录
     *
     * @param id
     * @param channel
     * @return
     */
    Boolean delAdminAndRelationRecords(Long id, ChannelEnum channel);
}

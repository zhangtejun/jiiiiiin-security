package cn.jiiiiiin.module.mngauth.service;

import cn.jiiiiiin.module.common.entity.mngauth.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.NonNull;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
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

    Admin signInByUsername(@NonNull String username);

    /**
     * spring 事务：
     * https://www.ibm.com/developerworks/cn/java/j-master-spring-transactional-use/index.html
     * http://blog.didispace.com/springboottransactional/
     *
     * @param admin
     * @return
     */
    @Transactional
    boolean relationRole(@NonNull Admin admin);

}

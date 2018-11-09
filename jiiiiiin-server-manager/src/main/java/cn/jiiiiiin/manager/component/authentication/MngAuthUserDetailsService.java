package cn.jiiiiiin.manager.component.authentication;

import cn.jiiiiiin.manager.properties.ManagerProperties;
import cn.jiiiiiin.module.mngauth.component.MngUserDetails;
import cn.jiiiiiin.module.mngauth.service.IAdminService;
import cn.jiiiiiin.security.core.authentication.AuthenticationBeanConfig;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 内管`UserDetailsService`
 *
 * 配置：{@link AuthenticationBeanConfig#userDetailsService()}
 *
 * @author jiiiiiin
 */
@Component
public class MngAuthUserDetailsService implements UserDetailsService {

    @Autowired
    private IAdminService adminService;

    @Autowired
    private ManagerProperties managerProperties;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据channel去获取登录用户的权限信息
        val optionalAdmin = adminService.signInByUsername(username, managerProperties.getChannel());
        if (optionalAdmin == null) {
            throw new UsernameNotFoundException("用户名密码不符");
        } else {
            return new MngUserDetails(optionalAdmin);
        }
    }

}

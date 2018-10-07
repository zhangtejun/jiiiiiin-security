package cn.jiiiiiin.module.mngauth.component;

import cn.jiiiiiin.module.common.mapper.mngauth.AdminMapper;
import cn.jiiiiiin.module.mngauth.service.IAdminService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author jiiiiiin
 */
@Component
public class MngAuthUserDetailsService implements UserDetailsService {

    @Autowired
    private IAdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        val optionalAdmin = adminService.signInByUsername(username);
        if (optionalAdmin == null) {
            throw new UsernameNotFoundException("用户名密码不符");
        } else {
            return new CustomUserDetails(optionalAdmin);
        }
    }

}

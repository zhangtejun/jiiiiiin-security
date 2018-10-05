package cn.jiiiiiin.module.mngauth.component;

import cn.jiiiiiin.module.common.mapper.mngauth.AdminMapper;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        val optionalAdmin = adminMapper.selectByUsername(username);
//        optionalAdmin.orElseThrow(() -> new UsernameNotFoundException(ErrDict.ERR_USERNAME_NOT_FOUND));
//        return optionalAdmin.map(CustomUserDetails::new).get();
        return null;
    }

}

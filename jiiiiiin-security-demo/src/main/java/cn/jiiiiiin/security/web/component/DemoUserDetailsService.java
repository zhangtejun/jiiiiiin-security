/**
 *
 */
package cn.jiiiiiin.security.web.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author zhailiang
 * @author jiiiiiin
 */
@Component
@Transactional
public class DemoUserDetailsService implements UserDetailsService, SocialUserDetailsService {

    final static Logger L = LoggerFactory.getLogger(DemoUserDetailsService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 当前类中可以根据需要注入响应的mybatis dao等持久层bean来通过username去数据库中查询应用的用户信息
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     * @see User 可以参考这个类来返回当前接口需要的登录认证对象
     * username: 用户名
     * password：用户密码，传递密码之后，密码的校验是由框架来帮我们做校验，也就是说这里只是做待登录用户的信息查询
     * authorities：用户权限集合，参考AuthorityUtils#commaSeparatedStringToAuthorityList的构建方式
     * <p>
     * 如果需要自定义UserDetails接口的下面4个方法来实现校验：
     * <p>
     * boolean isAccountNonExpired(); 返回false则认为账户过期了
     * <p>
     * boolean isAccountNonLocked(); 返回false则认为账户被锁定（冻结）了
     * <p>
     * boolean isCredentialsNonExpired(); 返回false则认为密码过期了
     * <p>
     * boolean isEnabled(); 标识账户是否可用
     * <p>
     * 关于密码的加解密：
     * @see org.springframework.security.crypto.password.PasswordEncoder
     * <p>
     * encode需要一个原始密码（客户端上传的）方法用来对原始密码进行加密，这个方法调用时机是，用户注册的时候，客户端传递上来的密码，需要我们手动调用该方法进行加密，之后再存储到数据库等
     * matches方法用来判断加密之后的密码和客户端传递上来的密码是否匹配，由框架负责调用，在下面的方法返回UserDetails之后，拿到UserDetails中的密码和客户端上传的密码调用该方法进行匹配
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        L.info("准备登陆的用户 {}", username);
        // TODO 测试实现
        // 根据用户名查找用户信息
        // 根据查找到的用户信息判断用户是否被冻结
        // 设置上面3中状态，参考：
        // return new User(username, "a11111", true, true, true, false, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        // return new User(username, "a11111", AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        // 当配置了PasswordEncoder之后，这里为了测试，直接就调用PasswordEncoder进行加密，实际开发中应该直接返回数据库中的密码字段（密文）
        return buildUser(username);
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        L.info("准备授权登陆的用户 {}", userId);
        return buildUser(userId);
    }

    /**
     *
     * @param userId 业务系统用户唯一标识
     * @return
     */
    private SocialUserDetails buildUser(String userId) {
        return new SocialUser(userId, passwordEncoder.encode("a11111"),
                true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }

}

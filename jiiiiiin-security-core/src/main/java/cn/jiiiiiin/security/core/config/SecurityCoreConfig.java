package cn.jiiiiiin.security.core.config;

import cn.jiiiiiin.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.social.security.SocialUserDetailsService;

import javax.sql.DataSource;

/**
 * 通用配置（app或者browser模块）
 *
 * @author jiiiiiin
 */
@Configuration
// 注册配置
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityCoreConfig {

    @Autowired
    private DataSource dataSource;

    /**
     * @return
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername
     * @see SocialUserDetailsService#loadUserByUserId(String)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 可以参考下面框架提供的PasswordEncoder去实现自己系统的密码加密机制
        return new BCryptPasswordEncoder();
    }

    /**
     * 记住我功能的token存取器配置
     * <p>
     * 需要插入一张框架需要的表[persistent_logins]：{@link JdbcTokenRepositoryImpl#CREATE_TABLE_SQL}
     * <p>
     * ![关于remember me功能](https://ws1.sinaimg.cn/large/0069RVTdgy1fuoes59unqj30zo0fuabd.jpg)
     *
     * @return
     * @see HttpSecurity#rememberMe()
     * @see org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer#tokenRepository(PersistentTokenRepository)
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // 帮我们在开发阶段建表
        // tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

}

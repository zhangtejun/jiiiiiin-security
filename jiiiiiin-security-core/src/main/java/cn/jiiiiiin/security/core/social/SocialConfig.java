/**
 *
 */
package cn.jiiiiiin.security.core.social;

import cn.jiiiiiin.security.core.properties.SecurityProperties;
import cn.jiiiiiin.security.core.social.support.SocialAuthenticationFilterPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * 社交登录配置主类
 *
 * @author zhailiang
 */
@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SecurityProperties securityProperties;

//    @Autowired(required = false)
//    private ConnectionSignUp connectionSignUp;

//    @Autowired(required = false)
//    private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;

    /**
     * 提供{@link UsersConnectionRepository}帮助我们将授权数据插入框架定义的表中
     *
     * @param connectionFactoryLocator 查找可用的{@link org.springframework.social.connect.ConnectionFactory}
     *                                 因为每个授权服务提供商都会有自己的一个ConnectionFactory在系统中，需要根据条件去查询不同的工厂
     * @see org.springframework.social.config.annotation.SocialConfigurerAdapter#getUsersConnectionRepository(org.springframework.social.connect.ConnectionFactoryLocator)
     * <p>
     * {@link JdbcUsersConnectionRepository}构建时候传递的第三个参数，帮我们对插入数据库的数据进行加解密，可用使用{@link Encryptors#standard(CharSequence, CharSequence)}来保证安全性
     * <p>
     * 建表语句在{@link JdbcUsersConnectionRepository}类定义的源代码包下
     * <p>
     * create table UserConnection (
     * # 业务系统用户id
     * # {@link org.springframework.social.security.SocialUserDetailsService#loadUserByUserId(String)}
     * # spring social 根据上面这个接口传递当前表的userId字段去获取业务系统的用户信息
     * userId varchar(255) not null,
     * # 服务提供商id
     * providerId varchar(255) not null,
     * # 服务提供商对应用户的id
     * providerUserId varchar(255),
     *
     * # 上面3个字段将我们业务系统和服务提供商的两组用户信息关联
     *
     * # 等级
     * `rank` int not null,
     * # 用户名 通过ApiAdapter设置
     * displayName varchar(255),
     * # 用户主页 通过ApiAdapter设置
     * profileUrl varchar(512),
     * # 用户名头像url 通过ApiAdapter设置
     * imageUrl varchar(512),
     * # 用户token令牌
     * accessToken varchar(512) not null,
     * secret varchar(512),
     * refreshToken varchar(512),
     * expireTime bigint,
     * primary key (userId, providerId, providerUserId));
     * <p>
     * create unique index UserConnectionRank on UserConnection(userId, providerId, `rank`);
     */
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        // !Encryptors.noOpText()为调试使用，不做加解密
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,
                connectionFactoryLocator, Encryptors.noOpText());
        // 添加表前缀
        repository.setTablePrefix("springsocial_");
//        if (connectionSignUp != null) {
//            repository.setConnectionSignUp(connectionSignUp);
//        }
        return repository;
    }

    /**
     * 社交登录配置类，供浏览器或app模块引入设计登录配置用。
     *
     * @return
     */
    @Bean
    public SpringSocialConfigurer socialSecurityConfig() {
//        String filterProcessesUrl = securityProperties.getSocial().getFilterProcessesUrl();
//        ImoocSpringSocialConfigurer configurer = new ImoocSpringSocialConfigurer(filterProcessesUrl);
//        configurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());
//        configurer.setSocialAuthenticationFilterPostProcessor(socialAuthenticationFilterPostProcessor);
//        return configurer;
        return new SpringSocialConfigurer();
    }

//    /**
//     * 用来处理注册流程的工具类
//     *
//     * @param connectionFactoryLocator
//     * @return
//     */
//    @Bean
//    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
//        return new ProviderSignInUtils(connectionFactoryLocator,
//                getUsersConnectionRepository(connectionFactoryLocator)) {
//        };
//    }
}

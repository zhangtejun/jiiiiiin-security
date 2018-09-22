/**
 *
 */
package cn.jiiiiiin.security.app.server;

import cn.jiiiiiin.security.app.component.authentication.social.openid.OpenIdAuthenticationSecurityConfig;
import cn.jiiiiiin.security.core.authentication.FormAuthenticationConfig;
import cn.jiiiiiin.security.core.authorize.AuthorizeConfigManager;
import cn.jiiiiiin.security.core.config.component.SmsCodeAuthenticationSecurityConfig;
import cn.jiiiiiin.security.core.dict.SecurityConstants;
import cn.jiiiiiin.security.core.properties.SecurityProperties;
import cn.jiiiiiin.security.core.social.SocialConfig;
import cn.jiiiiiin.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * 资源服务器配置
 * 使用`@EnableResourceServer`注解标明当前应用是一个“资源服务器”提供商
 * <p>
 * 类`browser`项目针对spring security的权限配置类`BrowserSpringSecurityBaseConfig`
 *
 * @author zhailiang
 */
@Configuration
@EnableResourceServer
public class CustomResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    /**
     * @see SocialConfig#socialSecurityConfig() 注入social配置到ss
     */
    @Autowired
    private SpringSocialConfigurer socialSecurityConfig;

    @Autowired
    private FormAuthenticationConfig formAuthenticationConfig;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

    @Override
    public void configure(HttpSecurity http) throws Exception {


        // TODO 业务系统的注册接口
        final String registerUrl = "/user/auth/register";

        formAuthenticationConfig.configure(http);

        http
                // 添加自定义验证码过滤器，校验session中的图形验证码
                .apply(validateCodeSecurityConfig)
                .and()
                // 追加短信验证码公共配置
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                // 添加social拦截过滤器，引导用户进行社交登录,`SocialAuthenticationFilter`
                .apply(socialSecurityConfig)
                .and()
                // 添加针对`openid`第三方授权登录的token版本支持
                .apply(openIdAuthenticationSecurityConfig)
                .and()
                // 临时关闭防护
                .csrf().disable();

        // 对请求进行授权，这个方法下面的都是授权的配置
        authorizeConfigManager.config(http.authorizeRequests());
    }

}
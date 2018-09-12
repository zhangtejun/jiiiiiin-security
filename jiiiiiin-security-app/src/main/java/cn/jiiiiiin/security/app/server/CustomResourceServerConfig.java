/**
 *
 */
package cn.jiiiiiin.security.app.server;

import cn.jiiiiiin.security.core.authentication.FormAuthenticationConfig;
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
 *
 * 类`browser`项目针对spring security的权限配置类`BrowserSpringSecurityBaseConfig`
 *
 * @author zhailiang
 */
@Configuration
@EnableResourceServer
public class CustomResourceServerConfig extends ResourceServerConfigurerAdapter {

//	@Autowired
//	protected AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;
//
//	@Autowired
//	protected AuthenticationFailureHandler imoocAuthenticationFailureHandler;
//
	@Autowired
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
//
//	@Autowired
//	private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;
//
	@Autowired
	private ValidateCodeSecurityConfig validateCodeSecurityConfig;
//
    /**
     * @see SocialConfig#socialSecurityConfig() 注入social配置到ss
     */
    @Autowired
    private SpringSocialConfigurer socialSecurityConfig;
//
//	@Autowired
//	private AuthorizeConfigManager authorizeConfigManager;
//
	@Autowired
	private FormAuthenticationConfig formAuthenticationConfig;

    @Autowired
    private SecurityProperties securityProperties;

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
                // 对请求进行授权，这个方法下面的都是授权的配置
                .authorizeRequests()
                // 添加匹配器，匹配器必须要放在`.anyRequest().authenticated()`之前配置
                // 配置授权，允许匹配的请求不需要进行认证（permitAll()）
                // https://docs.spring.io/spring-security/site/docs/4.2.7.RELEASE/reference/htmlsingle/#authorize-requests
                .antMatchers(
                        SecurityConstants.STATIC_RESOURCES_JS,
                        SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_FORM,
                        SecurityConstants.DEFAULT_SOCIAL_USER_INFO_URL,
                        SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
                        securityProperties.getBrowser().getSignInUrl(),
                        securityProperties.getBrowser().getSignUpUrl(),
                        securityProperties.getBrowser().getSignOutUrl(),
                        securityProperties.getBrowser().getSession().getSessionInvalidUrl(),
                        registerUrl
                ).permitAll()
                // 对所有请求// 都需要身份认证
                .anyRequest().authenticated()
                .and()
                // 临时关闭防护
                .csrf().disable();

//		formAuthenticationConfig.configure(http);
//
//		http.apply(validateCodeSecurityConfig)
//				.and()
//			.apply(smsCodeAuthenticationSecurityConfig)
//				.and()
//			.apply(imoocSocialSecurityConfig)
//				.and()
//			.apply(openIdAuthenticationSecurityConfig)
//				.and()
//			.csrf().disable();
//
//		authorizeConfigManager.config(http.authorizeRequests());
	}

}
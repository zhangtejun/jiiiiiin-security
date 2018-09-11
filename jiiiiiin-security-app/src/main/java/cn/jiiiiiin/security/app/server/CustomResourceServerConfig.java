/**
 *
 */
package cn.jiiiiiin.security.app.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * 资源服务器配置
 * 使用`@EnableResourceServer`注解标明当前应用是一个“资源服务器”提供商
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
//	@Autowired
//	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
//
//	@Autowired
//	private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;
//
//	@Autowired
//	private ValidateCodeSecurityConfig validateCodeSecurityConfig;
//
//	@Autowired
//	private SpringSocialConfigurer imoocSocialSecurityConfig;
//
//	@Autowired
//	private AuthorizeConfigManager authorizeConfigManager;
//
//	@Autowired
//	private FormAuthenticationConfig formAuthenticationConfig;
//
//	@Override
//	public void configure(HttpSecurity http) throws Exception {
//
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
//	}

}
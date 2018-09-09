/**
 * 
 */
package cn.jiiiiiin.security.browser;
import cn.jiiiiiin.security.browser.session.CustomExpiredSessionStrategy;
import cn.jiiiiiin.security.browser.session.CustomInvalidSessionStrategy;
import cn.jiiiiiin.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

/**
 * 浏览器环境下扩展点配置，配置在这里的bean，业务系统都可以通过声明同类型或同名的bean来覆盖安全
 * 模块默认的配置。
 * 
 * @author zhailiang
 *
 */
@Configuration
public class BrowserSecurityBeanConfig {

	@Autowired
	private SecurityProperties securityProperties;
	
	/**
	 * session失效时的处理策略配置
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(InvalidSessionStrategy.class)
	public InvalidSessionStrategy invalidSessionStrategy(){
		return new CustomInvalidSessionStrategy(securityProperties);
	}
	
	/**
	 * 并发登录导致前一个session失效时的处理策略配置
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
	public SessionInformationExpiredStrategy sessionInformationExpiredStrategy(){
		return new CustomExpiredSessionStrategy(securityProperties);
	}
	
//	/**
//	 * 退出时的处理策略配置
//	 *
//	 * @return
//	 */
//	@Bean
//	@ConditionalOnMissingBean(LogoutSuccessHandler.class)
//	public LogoutSuccessHandler logoutSuccessHandler(){
//		return new ImoocLogoutSuccessHandler(securityProperties.getBrowser().getSignOutUrl());
//	}
	
}

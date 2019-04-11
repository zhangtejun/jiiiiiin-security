/**
 *
 */
package cn.jiiiiiin.security.browser.config;

import cn.jiiiiiin.security.browser.component.authentication.BrowserAuthenticationFailureHandler;
import cn.jiiiiiin.security.browser.component.authentication.BrowserAuthenticationSuccessHandler;
import cn.jiiiiiin.security.browser.component.authentication.BrowserLoginUrlAuthenticationEntryPoint;
import cn.jiiiiiin.security.browser.component.authorize.BrowserAccessDeniedHandler;
import cn.jiiiiiin.security.browser.session.CustomSessionInformationExpiredStrategy;
import cn.jiiiiiin.security.browser.session.CustomInvalidSessionStrategy;
import cn.jiiiiiin.security.core.dict.SecurityConstants;
import cn.jiiiiiin.security.core.properties.SecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import cn.jiiiiiin.security.browser.component.logout.BrowserLogoutSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 浏览器环境下扩展点配置，配置在这里的bean，业务系统都可以通过声明同类型或同名的bean来覆盖安全
 * 模块默认的配置。
 *
 * @author zhailiang
 * @author jiiiiiin
 */
@Configuration
@AllArgsConstructor
public class BrowserSecurityBeanConfig {

    private final SecurityProperties securityProperties;

    /**
     * session失效时的处理策略配置
     *
     * @return
     * @see HttpSecurity#sessionManagement() {@link #invalidSessionStrategy()}
     */
    @Bean
    @ConditionalOnMissingBean(InvalidSessionStrategy.class)
    public InvalidSessionStrategy invalidSessionStrategy() {
        return new CustomInvalidSessionStrategy(securityProperties);
    }

    /**
     * 并发登录导致前一个session失效时的处理策略配置
     *
     * @return
     * @see HttpSecurity#sessionManagement() {@link #sessionInformationExpiredStrategy()}
     */
    @Bean
    @ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        return new CustomSessionInformationExpiredStrategy(securityProperties);
    }

    /**
     * 退出时的处理策略配置
     * 授权配置之退出成功处理器配置
     *
     * @return
     * @see HttpSecurity#logout()
     */
    @Bean
    @ConditionalOnMissingBean(LogoutSuccessHandler.class)
    public LogoutSuccessHandler logoutSuccessHandler(ObjectMapper objectMapper, LiteDeviceResolver liteDeviceResolver) {
        return new BrowserLogoutSuccessHandler(securityProperties.getBrowser().getSignOutSuccessUrl(), objectMapper, liteDeviceResolver);
    }

    /**
     * 权限校验失败时候的处理策略配置
     *
     * @return
     * @see org.springframework.security.web.access.ExceptionTranslationFilter#doFilter(ServletRequest, ServletResponse, FilterChain)
     * @see org.springframework.security.web.access.AccessDeniedHandlerImpl
     */
    @Bean
    @ConditionalOnMissingBean(AccessDeniedHandler.class)
    public AccessDeniedHandler accessDeniedHandler() {
        return new BrowserAccessDeniedHandler(securityProperties.getBrowser().getErrorPage());
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new BrowserLoginUrlAuthenticationEntryPoint(SecurityConstants.DEFAULT_UNAUTHENTICATED_URL);
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationSuccessHandler.class)
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new BrowserAuthenticationSuccessHandler();
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationFailureHandler.class)
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new BrowserAuthenticationFailureHandler();
    }

}

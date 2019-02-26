/**
 *
 */
package cn.jiiiiiin.manager.component;

import cn.jiiiiiin.manager.config.Swagger2Config;
import cn.jiiiiiin.security.core.authorize.AuthorizeConfigProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * @author jiiiiiin
 */
@Component
public class MngAuthAuthorizeConfigProvider implements AuthorizeConfigProvider {

    @Override
    public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config
                .antMatchers(Swagger2Config.AUTH_WHITELIST).permitAll()
                .antMatchers(
                        "/js/**", "/css/**", "/img/**", "/images/**", "/fonts/**", "/**/favicon.ico",
                        // Druid监控的配置
                        "/", "/druid", "/druid/*", "/druid/**",
                        "/testZuul"
                ).permitAll();
        return false;
    }

}

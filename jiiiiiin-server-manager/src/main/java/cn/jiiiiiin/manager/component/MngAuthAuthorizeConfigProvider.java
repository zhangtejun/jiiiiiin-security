/**
 *
 */
package cn.jiiiiiin.manager.component;

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
                .antMatchers(
                        "/js/**", "/css/**", "/img/**", "/images/**", "/fonts/**", "/**/favicon.ico",
                        // TODO 判断是否是develop模式
                        // Druid监控的配置
                        "/", "/druid", "/druid/*", "/druid/**",
                        "/testZuul"
                ).permitAll();
        return false;
    }

}

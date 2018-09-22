/**
 *
 */
package cn.jiiiiiin.security.web.component;

import cn.jiiiiiin.security.core.authorize.AuthorizeConfigProvider;
import cn.jiiiiiin.security.core.dict.SecurityConstants;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * @author zhailiang
 */
@Component
public class DemoAuthorizeConfigProvider implements AuthorizeConfigProvider {

    /**
     * 业务系统的注册接口
     */
    final String registerUrl = "/user/auth/register";

    /* (non-Javadoc)
     * @see com.imooc.security.core.authorize.AuthorizeConfigProvider#config(org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry)
     */
    @Override
    public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        //demo项目授权配置
        config.antMatchers(registerUrl)
                .permitAll()
                .antMatchers("/user").hasRole("ADMIN");
        return false;
    }

}

package cn.jiiiiiin.security.browser.config;

import cn.jiiiiiin.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;

/**
 * @author jiiiiiin
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * https://docs.spring.io/spring-security/site/docs/4.2.7.RELEASE/reference/htmlsingle/
     * <p>
     * https://ws3.sinaimg.cn/large/006tNbRwgy1fuib0js6rhj31kw0ju0vk.jpg
     * <p>
     * security 过滤器链关键环节：
     *
     * @param http
     * @throws Exception
     * @see org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
     * 这个类是用来做身份认证（登录认证）
     * @see org.springframework.security.web.access.ExceptionTranslationFilter
     * 用来处理FilterSecurityInterceptor认证过程中认证失败时候的流程控制
     * @see org.springframework.security.web.access.intercept.FilterSecurityInterceptor#invoke(FilterInvocation)
     * 中的InterceptorStatusToken token = super.beforeInvocation(fi); 会进行身份认证授权判断
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 对请求进行授权，这个方法下面的都是授权的配置
                .authorizeRequests()
                // 配置授权，允许匹配的请求不需要进行认证（permitAll()）
                // https://docs.spring.io/spring-security/site/docs/4.2.7.RELEASE/reference/htmlsingle/#authorize-requests
                .antMatchers("/resources/**", "/signIn", securityProperties.getBrowser().getLoginPage()).permitAll()
                // 对所有请求// 都需要身份认证
                .anyRequest().authenticated()
                // 默认使用下面的方式进行用户身份认证
//                .and()
//                .httpBasic();
                .and()
                // formLogin() 指定身份认证的方式
                // 下面这样配置就改变了默认的httpBasic认证方式，而提供一个登录页面
                .formLogin()
                // 自定义登录页面
//                .loginPage("/signIn.html")
                .loginPage("/signIn")
                // 自定义登录交易请求接口，会被UsernamePasswordAuthenticationFilter所识别作为requiresAuthenticationRequestMatcher
                .loginProcessingUrl("/signIn")
                .and()
                .csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 可以参考下面框架提供的PasswordEncoder去实现自己系统的密码加密机制
        return new BCryptPasswordEncoder();
    }
}

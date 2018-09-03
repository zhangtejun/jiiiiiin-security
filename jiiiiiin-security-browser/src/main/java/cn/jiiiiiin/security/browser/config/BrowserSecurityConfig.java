package cn.jiiiiiin.security.browser.config;

import cn.jiiiiiin.security.core.config.component.SmsCodeAuthenticationSecurityConfig;
import cn.jiiiiiin.security.core.properties.SecurityProperties;
import cn.jiiiiiin.security.core.social.SocialConfig;
import cn.jiiiiiin.security.core.validate.code.ValidateCodeSecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * @author jiiiiiin
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    final static Logger L = LoggerFactory.getLogger(BrowserSecurityConfig.class);

    /**
     * 需要进行身份认证的接口
     */
    public static final String LOGIN_URL = "/authentication/require";
    /**
     * 身份认证表单提交的接口
     */
    public static final String LOGIN_PROCESSING_URL = "/authentication/from";
    /**
     * 验证码接口（图形验证码、短信验证码）
     */
    private static final String CODE_IMAGE = "/code/*";

    private static final String STATIC_RESOURCES = "/js/**";

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthenticationSuccessHandler jAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler jAuthenticationFailureHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    /**
     * @see SocialConfig#socialSecurityConfig() 注入social配置到ss
     */
    @Autowired
    private SpringSocialConfigurer socialSecurityConfig;

    /**
     * https://docs.spring.io/spring-security/site/docs/4.2.7.RELEASE/reference/htmlsingle/
     * <p>
     * https://ws3.sinaimg.cn/large/006tNbRwgy1fuib0js6rhj31kw0ju0vk.jpg
     * <p>
     * 默认使用下面的方式进行用户身份认证
     * .and()
     * .httpBasic();
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
     *
     * @see org.springframework.social.security.SocialAuthenticationFilter
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        final String signInUrl = securityProperties.getBrowser().getSignInUrl();
        final String signUpUrl = securityProperties.getBrowser().getSignUpUrl();
        // TODO 业务系统的注册接口
        final String registerUrl = "/user/register";

        L.info("配置的signInUrl: {} signUpUrl: {}", signInUrl, signUpUrl);

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
                .antMatchers(STATIC_RESOURCES, CODE_IMAGE, LOGIN_URL, signInUrl, signUpUrl, registerUrl).permitAll()
                // 对所有请求// 都需要身份认证
                .anyRequest().authenticated()
                .and()
                // 开启表单登录（指定身份认证的方式）
                // 下面这样配置就改变了默认的httpBasic认证方式，而提供一个登录页面
                .formLogin()
                // 配置自定义登录页面所在的接口，如`/signIn.html`，在需要登录的时候去访问的接口（渲染的页面）
                .loginPage(LOGIN_URL)
                // 配置自定义登录交易请求接口（上面的登录页面提交表单之后登录接口），会被UsernamePasswordAuthenticationFilter所识别作为requiresAuthenticationRequestMatcher
                .loginProcessingUrl(LOGIN_PROCESSING_URL)
                // 配置自定义认证成功处理器
                .successHandler(jAuthenticationSuccessHandler)
                // 配置自定义认证失败处理器
                .failureHandler(jAuthenticationFailureHandler)
                .and()
                .rememberMe()
                // 配置记住用户的配置
                // 配置将需要记住用户的用户名通过一下的dao设置到数据库
                .tokenRepository(persistentTokenRepository())
                // 设置记住用户的时长
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                // 需要业务系统自己实现
                .userDetailsService(userDetailsService)
                .and()
                // 临时关闭防护
                .csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 可以参考下面框架提供的PasswordEncoder去实现自己系统的密码加密机制
        return new BCryptPasswordEncoder();
    }


    /**
     * 记住我功能的token存取器配置
     * <p>
     * 需要插入一张框架需要的表：{@link JdbcTokenRepositoryImpl#CREATE_TABLE_SQL}
     * <p>
     * ![关于remember me功能](https://ws1.sinaimg.cn/large/0069RVTdgy1fuoes59unqj30zo0fuabd.jpg)
     *
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // 帮我们在开发阶段建表
        // tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }


}

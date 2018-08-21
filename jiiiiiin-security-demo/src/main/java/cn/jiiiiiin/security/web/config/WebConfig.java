package cn.jiiiiiin.security.web.config;

import cn.jiiiiiin.security.web.filter.TimeFilter;
import cn.jiiiiiin.security.web.interceptor.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;

/**
 * 参考：
 * https://segmentfault.com/a/1190000011420942
 *
 * @author jiiiiiin
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private TimeInterceptor timeInterceptor;

    /**
     * 如何注册第三方的filter
     * <p>
     * 注册time filter
     * <p>
     * https://blog.csdn.net/u010054969/article/details/62232592
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean registerTimeFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new TimeFilter());
        registrationBean.setUrlPatterns(Arrays.asList("/*"));
        registrationBean.setOrder(1);
        return registrationBean;
    }

    /**
     * 注册拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(timeInterceptor);
    }
}

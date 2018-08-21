package cn.jiiiiiin.security.web.config;

import cn.jiiiiiin.security.web.filter.TimeFilter;
import cn.jiiiiiin.security.web.interceptor.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
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

    /**
     * 针对异步接口的拦截器配置需要通过下面的接口进行注册（相应的拦截器也需要重写）
     * 否则常规的拦截器是拦截不到异步的接口
     *
     * @param configurer
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        super.configureAsyncSupport(configurer);
        // 设置异步请求的超时时间
//        configurer.setDefaultTimeout()
        // 设置可重用的线程池，而不是spring默认的简单线程池
//        configurer.setTaskExecutor()
//        configurer.registerDeferredResultInterceptors()
//        configurer.registerCallableInterceptors()
    }
}

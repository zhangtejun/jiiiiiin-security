package cn.jiiiiiin.module.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author jiiiiiin
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    /**
     * 配置系统的消息转换器
     * <p>
     * `MappingJackson2HttpMessageConverter`可以实现将java对象转换成json对象
     * 处理消息响应
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(0, new MappingJackson2HttpMessageConverter());
//        converters.clear();
//        converters.add(new MappingJackson2HttpMessageConverter());
    }
}

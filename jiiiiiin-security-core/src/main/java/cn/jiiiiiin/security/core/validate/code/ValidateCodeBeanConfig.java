package cn.jiiiiiin.security.core.validate.code;

import cn.jiiiiiin.security.core.properties.SecurityProperties;
import cn.jiiiiiin.security.core.validate.code.impl.ImageCodeGenerator;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiiiiiin
 */
@Configuration
public class ValidateCodeBeanConfig {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private Producer captchaProducer;

    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    public ValidateCodeGenerator imageCodeGenerator() {
        final ImageCodeGenerator imageCodeGenerator = new ImageCodeGenerator(securityProperties, captchaProducer);
        return imageCodeGenerator;
    }


}

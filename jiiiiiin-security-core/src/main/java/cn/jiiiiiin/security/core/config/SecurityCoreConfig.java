package cn.jiiiiiin.security.core.config;

import cn.jiiiiiin.security.core.properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author jiiiiiin
 */
@Configuration
// 注册配置
@EnableConfigurationProperties(SecurityProperties.class)
// 引入kaptcha验证码配置
@ImportResource(locations = {"classpath:config/kaptcha.xml"})
public class SecurityCoreConfig {

}

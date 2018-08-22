package cn.jiiiiiin.security.core.config;

import cn.jiiiiiin.security.core.properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiiiiiin
 */
@Configuration
// 注册配置
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityCoreConfig {

}

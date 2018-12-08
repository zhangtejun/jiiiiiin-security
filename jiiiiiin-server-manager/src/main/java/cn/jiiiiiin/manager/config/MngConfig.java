package cn.jiiiiiin.manager.config;

import cn.jiiiiiin.manager.properties.ManagerProperties;
import cn.jiiiiiin.security.core.properties.SecurityProperties;
import cn.jiiiiiin.security.rbac.component.dict.RbacDict;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author jiiiiiin
 */
@Configuration
@EnableConfigurationProperties(ManagerProperties.class)
@Slf4j
public class MngConfig {

    @Bean
    public EhCacheManagerFactoryBean cacheManagerFactory() {
        log.info("初始化EhCacheManagerFactoryBean");
        val factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        factoryBean.setShared(true);
        return factoryBean;
    }

    @Bean
    public SimpleGrantedAuthority adminSimpleGrantedAuthority() {
        return new SimpleGrantedAuthority(RbacDict.ROLE_ADMIN_AUTHORITY_FULL_NAME);
    }
}

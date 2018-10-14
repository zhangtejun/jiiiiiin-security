package cn.jiiiiiin.manager.config;

import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * @author jiiiiiin
 */
@Configuration
public class MngConfig {

    private Logger logger = LoggerFactory.getLogger(MngConfig.class);

    @Bean
    public EhCacheManagerFactoryBean cacheManagerFactory() {
        logger.info("初始化EhCacheManagerFactoryBean");
        val factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        factoryBean.setShared(true);
        return factoryBean;
    }
}

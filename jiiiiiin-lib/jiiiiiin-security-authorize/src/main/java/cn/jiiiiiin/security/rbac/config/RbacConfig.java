package cn.jiiiiiin.security.rbac.config;

import cn.jiiiiiin.security.rbac.component.service.RBACService;
import cn.jiiiiiin.security.rbac.component.service.impl.RBACServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RbacConfig {

    @Bean
    @ConditionalOnMissingBean(name = "rbacService")
    public RBACService rbacService() {
        return new RBACServiceImpl();
    }
}

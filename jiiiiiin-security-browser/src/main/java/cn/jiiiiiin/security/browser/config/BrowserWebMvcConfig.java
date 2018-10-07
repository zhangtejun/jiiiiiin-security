package cn.jiiiiiin.security.browser.config;

import cn.jiiiiiin.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author jiiiiiin
 */
@Configuration
public class BrowserWebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // setViewName("signIn")指向`/jiiiiiin-security-browser/src/main/resources/templates/signIn.html`
        registry.addViewController(securityProperties.getBrowser().getSignInUrl()).setViewName("signIn");
    }

}

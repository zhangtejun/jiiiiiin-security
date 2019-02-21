package cn.jiiiiiin;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

/**
 * `@EnableZuulProxy`：开启zuul网关
 * @author jiiiiiin
 */
@SuppressWarnings("ALL")
@EnableApolloConfig
@EnableZuulProxy
@SpringCloudApplication
@Slf4j
public class ZuulGatewayApplication {

    public static void main(String[] args) {
        val app = new SpringApplication(ZuulGatewayApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @ConfigurationProperties("zuul")
    @RefreshScope
    public ZuulProperties zuulProperties() {
        log.debug("zuulProperties refresh");
        return new ZuulProperties();
    }
}

package cn.jiiiiiin;

import lombok.val;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * `@EnableZuulProxy`：开启zuul网关
 * @author jiiiiiin
 */
@EnableZuulProxy
@SpringCloudApplication
public class ZuulGatewayApplication {

    public static void main(String[] args) {
        val app = new SpringApplication(ZuulGatewayApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}

package cn.jiiiiiin;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import lombok.val;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @author jiiiiiin
 */
@EnableApolloConfig
@SpringBootApplication
@EnableEurekaServer
@EnableHystrixDashboard
public class DiscoveryServerApplication {

    public static void main(String[] args) {
        val app = new SpringApplication(DiscoveryServerApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}

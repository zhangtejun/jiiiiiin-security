package cn.jiiiiiin;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author jiiiiiin
 */
@SpringBootApplication
@EnableTransactionManagement
@Slf4j
@EnableApolloConfig
public class ManagerApp {

    public static void main(String[] args) {
        val app = new SpringApplication(ManagerApp.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

}

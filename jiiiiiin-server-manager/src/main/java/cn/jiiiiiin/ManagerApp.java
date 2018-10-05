package cn.jiiiiiin;

import lombok.val;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Hello world!
 *
 * @author jiiiiiin
 */
@RestController
@EnableSwagger2
@SpringBootApplication
@MapperScan(value = {"cn.jiiiiiin.module.common.mapper", "cn.jiiiiiin.security.rbac.mapper"})
public class ManagerApp {

    public static void main(String[] args) {
        val app = new SpringApplication(ManagerApp.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello spring security";
    }

}

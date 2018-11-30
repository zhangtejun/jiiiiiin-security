package cn.jiiiiiin;

import cn.jiiiiiin.module.common.entity.mngauth.Resource;
import com.baomidou.mybatisplus.extension.api.R;
import lombok.val;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author jiiiiiin
 */
@RestController
@EnableSwagger2
@SpringBootApplication
@EnableTransactionManagement
// @EnableGlobalMethodSecurity(prePostEnabled = true)
public class ManagerApp {

    public static void main(String[] args) {
        val app = new SpringApplication(ManagerApp.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    // @PreAuthorize("hasAnyRole('OPERATOR')")
    @GetMapping("/hello")
    public String hello() {
        return "hello spring security";
    }
}

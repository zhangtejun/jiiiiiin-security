package cn.jiiiiiin;

import cn.jiiiiiin.module.common.dto.mngauth.AdminDto;
import cn.jiiiiiin.module.common.entity.mngauth.Admin;
import cn.jiiiiiin.validation.core.Style;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author jiiiiiin
 */
@RestController
@EnableSwagger2
@SpringBootApplication
@EnableTransactionManagement
@Slf4j
public class ManagerApp {

    public static void main(String[] args) {
        val app = new SpringApplication(ManagerApp.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @NoArgsConstructor
    @Getter
    @ToString
    public static class AdminVo extends Admin {
        @NotBlank
        private String username = "losi";
    }

    @GetMapping("/hello")
    public String hello(@Style(vo = AdminVo.class) @ModelAttribute AdminDto admin) {
        log.debug("hello params {}", admin.getUsername());
        return "hello spring security";
    }

}

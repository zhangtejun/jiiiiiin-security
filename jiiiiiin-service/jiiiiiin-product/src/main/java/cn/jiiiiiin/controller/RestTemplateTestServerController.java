package cn.jiiiiiin.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给order服务针对`RestTemplate`的调用测试
 *
 * @author jiiiiiin
 */
@RestController
public class RestTemplateTestServerController {

    @Value("${server.port}")
    private int port;

    @GetMapping("/msg")
    public String getProduct() {
        return "商品" + port;
    }
}

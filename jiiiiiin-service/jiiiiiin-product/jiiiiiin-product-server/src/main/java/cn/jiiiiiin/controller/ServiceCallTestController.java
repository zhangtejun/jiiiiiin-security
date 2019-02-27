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
public class ServiceCallTestController {

    @Value("${server.port}")
    private int port;

    @GetMapping("/msg")
    public String getMsg() throws InterruptedException {
        Thread.sleep(2000);
        return "商品" + port;
    }
}

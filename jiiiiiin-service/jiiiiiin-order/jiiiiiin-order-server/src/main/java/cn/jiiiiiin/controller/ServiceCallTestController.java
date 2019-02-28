package cn.jiiiiiin.controller;

import cn.jiiiiiin.product.client.FeignClientTest;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author jiiiiiin
 */
@RestController
@Slf4j
// 2. 针对整个控制器进行服务降级接口的声明
@DefaultProperties(defaultFallback = "defaultFallback")
public class ServiceCallTestController {

    private static final String PRODUCT_SERVICE_ID = "JIIIIIIN-PRODUCT";

    @Value("${server.port}")
    private int port;

    /**
     * 通过LB去eureka server获取到的服务地址集合做软负载
     */
    private final LoadBalancerClient loadBalancerClient;

    private final RestTemplate restTemplate;

    private final FeignClientTest feignClientTest;

    @Autowired
    public ServiceCallTestController(LoadBalancerClient loadBalancerClient, RestTemplate restTemplate, FeignClientTest feignClientTest) {
        this.loadBalancerClient = loadBalancerClient;
        this.restTemplate = restTemplate;
        this.feignClientTest = feignClientTest;
    }

    // 1. 针对单个接口进行服务降级接口的声明
    // 如果要实现服务降级，就算使用默认配置`fallbackMethod`，也需要在接口上面配置`@HystrixCommand`
    @HystrixCommand(fallbackMethod = "fallback",
            // `HystrixCommandProperties`查看更多配置信息
            commandProperties = {
            // 配置超时时间，默认为1秒，这里设置为3秒
            // 超时时间要看具体接口的调用执行逻辑，根据情况来设置
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
    })
    @GetMapping("/msg")
    public String getOrder() {
        val templ = new RestTemplate();

        // 1.第一种通讯方式（直接使用restTemplate，url写死）
//        val product = templ.getForObject("http://localhost:7000/product/msg", String.class);
//        val product = restTemplate.getForObject("http://localhost:7000/product/msg", String.class);

        // 2.第二种方式
        // loadBalancerClient帮我们做客户端软负载
//        val serviceInstance = loadBalancerClient.choose(PRODUCT_SERVICE_ID);
//        val url = String.format("http://%s:%s/product/msg", serviceInstance.getHost(), serviceInstance.getPort());
//        val product = templ.getForObject(url, String.class);

        // 3.第三种方式
        val product = restTemplate.getForObject(String.format("http://%s/product/msg", PRODUCT_SERVICE_ID), String.class);

        // 4.第四种方式
//        val product = feignClientTest.getMsg();
        log.debug("getOrder:: product {}", product);

//        throw new RuntimeException("服务降级测试触发");

        // print -> `订单7100::获取到商品7000`
        return "订单" + port + " :: " + product;
    }

    private String fallback() {
        return "断路器生效了【fallback】";
    }

    private String defaultFallback() {
        return "断路器生效了【defaultFallback】";
    }
}

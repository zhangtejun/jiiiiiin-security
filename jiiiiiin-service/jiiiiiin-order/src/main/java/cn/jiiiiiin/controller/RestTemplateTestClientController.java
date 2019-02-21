package cn.jiiiiiin.controller;

import cn.jiiiiiin.client.FeignClientTest;
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
public class RestTemplateTestClientController {

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
    public RestTemplateTestClientController(LoadBalancerClient loadBalancerClient, RestTemplate restTemplate, FeignClientTest feignClientTest) {
        this.loadBalancerClient = loadBalancerClient;
        this.restTemplate = restTemplate;
        this.feignClientTest = feignClientTest;
    }

    @GetMapping("/msg")
    public String getOrder() {
//        val templ = new RestTemplate();

        // 1.第一种通讯方式（直接使用restTemplate，url写死）
//        val product = restTemplate.getForObject("http://localhost:7000/product/msg", String.class);

        // 2.第二种方式
        // loadBalancerClient帮我们做客户端软负载
//        val serviceInstance = loadBalancerClient.choose(PRODUCT_SERVICE_ID);
//        val url = String.format("http://%s:%s/product/msg", serviceInstance.getHost(), serviceInstance.getPort());
//        val product = templ.getForObject(url, String.class);

        // 3.第三种方式
//        val product = restTemplate.getForObject(String.format("http://%s/product/msg", PRODUCT_SERVICE_ID), String.class);

        // 4.第四种方式
        val product = feignClientTest.getMsg();
        log.debug("getOrder:: product {}", product);
        // print -> `订单7100::获取到商品7000`
        return "订单" + port + " :: " + product;
    }
}

package cn.jiiiiiin.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * `RestTemplateTestServerController`的Feign接口
 *  1.name标识代理的服务名
 * @author jiiiiiin
 */
//@FeignClient(name="jiiiiiin-product")
public interface RestTemplateTestClient {

    @GetMapping("/msg")
    String getMsg();
}

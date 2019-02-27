package cn.jiiiiiin.product.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * `RestTemplateTestServerController`的Feign接口
 *  1.name标识代理的服务名
 * @author jiiiiiin
 */
@FeignClient(name="jiiiiiin-product")
public interface FeignClientTest {

    /**
     * 注意如果被调用的服务设置了上下文，那么接口必须手动添加上下文
     * @return
     */
    @GetMapping("/product/msg")
    String getMsg();
}

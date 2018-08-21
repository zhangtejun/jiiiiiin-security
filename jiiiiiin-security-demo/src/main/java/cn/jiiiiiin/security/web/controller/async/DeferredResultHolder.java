package cn.jiiiiiin.security.web.controller.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jiiiiiin
 */
@Component
public class DeferredResultHolder {

    @Autowired
    MockQueue queue;

    /**
     * map
     *  key: 订单号
     *  val: 订单处理结果
     */
    private Map<String, DeferredResult<String>> map = new HashMap<>();

    public Map<String, DeferredResult<String>> getMap() {
        return map;
    }
}

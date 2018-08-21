package cn.jiiiiiin.security.web.controller.async;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;

/**
 * @author jiiiiiin
 */
@RestController
public class AsyncController {

    final static Logger L = LoggerFactory.getLogger(AsyncController.class);

    @Autowired
    private MockQueue queue;

    @Autowired
    private DeferredResultHolder holder;

    @RequestMapping("/sync/order")
    public String order() throws InterruptedException {
        // 同步处理方式
        L.info("主线程开始");
        // 模拟下单耗时操作
        Thread.sleep(1000);
        L.info("主线程结束");
        return "success";
    }

    /**
     * 对于浏览器来说，还是一个【正常】的请求，因为耗时还是1.x秒得到响应
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/async/order")
    public Callable<String> order2() throws InterruptedException {
        // 同步处理方式
        L.info("主线程开始");
        // 开一个spring管理下的线程去处理耗时操作
        final Callable<String> res = () -> {
            L.info("子线程开始");
            // 模拟下单耗时操作
            Thread.sleep(1000);
            L.info("子线程返回");
            return "success";
        };
        // 主线程被释放，返还给容器
        L.info("主线程结束");
        return res;
    }

    /**
     * 对于浏览器来说，还是一个【正常】的请求，因为耗时还是1.x秒得到响应
     * 模拟服务器1接收到请求将请求丢到消息队列
     * 下面就模拟线程1的处理逻辑
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/async/order2")
    public DeferredResult<String> order3() throws InterruptedException {
        // 同步处理方式
        L.info("主线程开始");
        // 模拟生成订单号
        String orderNumber = RandomStringUtils.randomNumeric(8);
        // 模拟将请求放入消息队列
        queue.setPlaceOrder(orderNumber);
        // 开一个spring管理下的线程去处理耗时操作
        final DeferredResult<String> res = new DeferredResult<>();
        holder.getMap().put(orderNumber, res);
        // 主线程被释放，返还给容器
        L.info("主线程结束");
        return res;
    }
}

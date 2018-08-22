package cn.jiiiiiin.security.web.controller.async;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 模拟server1的消息队列监听器
 * 监听订单的处理结果
 * <p>
 * ContextRefreshedEvent这个事件是spring容器初始化完毕的事件
 */
@Component
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {

    final static Logger L = LoggerFactory.getLogger(AsyncController.class);

    @Autowired
    MockQueue mockQueue;

    @Autowired
    DeferredResultHolder deferredResultHolder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        // 当前方法是在主线程表调用，下面的无线循环必须单开线程，不然会将主线程资源消耗

        new Thread(() -> {
            // 当应用启动之后，去监听消息队列；
            while (true) {
                final String completeOrder = mockQueue.getCompleteOrder();
                if (!StringUtils.isBlank(completeOrder)) {
                    L.info("返回订单处理结果 {}", completeOrder);
                    // .setResult("")标识本次请求的异步处理完毕，需要返回前端结果
                    // 这里就是请求线程（控制器）和返回结果给前端的线程直接进行通讯的逻辑，依赖DeferredResult来完成处理
                    deferredResultHolder.getMap().get(completeOrder)
                            .setResult(completeOrder + " order complete");

                    // mock 将模拟消息队列置空
                    mockQueue.setPlaceOrder(null);
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}

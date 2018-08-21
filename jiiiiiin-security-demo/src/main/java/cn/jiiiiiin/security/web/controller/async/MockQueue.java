package cn.jiiiiiin.security.web.controller.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 模拟消息队列
 *
 * @author jiiiiiin
 */
@Component
public class MockQueue {

    final static Logger L = LoggerFactory.getLogger(MockQueue.class);

    /**
     * 表示下单的消息
     */
    private String placeOrder;

    /**
     * 表示订单完成的消息
     */
    private String completeOrder;

    public String getPlaceOrder() {
        return placeOrder;
    }

    public void setPlaceOrder(String placeOrder) {
        new Thread(()-> {
            L.info("接收到下单请求 {}", placeOrder);
            this.placeOrder = placeOrder;
            // 模拟server2处理订单
            // 模拟下单耗时操作
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 下面这个属性被设值，我们就认为该消息已经处理完成，返回到队列
            this.completeOrder = placeOrder;
            L.info("下单请求处理完毕 {}", placeOrder);
        }).start();
    }

    public String getCompleteOrder() {
        return completeOrder;
    }

    public void setCompleteOrder(String completeOrder) {
        this.completeOrder = completeOrder;
    }
}

/**
 *
 */
package cn.jiiiiiin.security.core.social.view;

import cn.jiiiiiin.security.core.utils.HttpDataUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 绑定/解绑结果默认视图
 * <p>
 * 支持多渠道可重用
 * <p>
 * 应用需要自定义定义当前视图
 *
 * @author zhailiang
 * @see org.springframework.social.connect.web.ConnectController#connect(String, NativeWebRequest) 定义该接口的响应视图
 * @see cn.jiiiiiin.security.core.social.weixin.config.WeixinAutoConfiguration#weixinConnectedView 在这里去声明具体的响应视图组件
 */
@AllArgsConstructor
@Slf4j
public class CustomBindingConnectView extends AbstractView {

    private final ObjectMapper objectMapper;

    private final LiteDeviceResolver liteDeviceResolver;

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        log.debug("绑定解绑接口被执行 model {}", model);
        // 响应绑定页面，微信在授权完毕之后的回调url接口响应内容
        response.setContentType("text/html;charset=UTF-8");
        // 区分绑定还是解绑成功
        final Device currentDevice = liteDeviceResolver.resolveDevice(request);
        if (model.get("connections") == null) {
            // 执行解绑逻辑
            if (currentDevice.isNormal()) {
                response.sendRedirect(request.getContextPath() + "/userBinding?unbindOptions=success");
            } else {
                HttpDataUtil.respJson(response, objectMapper.writeValueAsString(R.ok("解绑成功")));
            }
        } else {
            // 执行绑定逻辑
            if (currentDevice.isNormal()) {
                response.sendRedirect(request.getContextPath() + "/userBinding?bindOptions=success");
            } else {
                HttpDataUtil.respJson(response, objectMapper.writeValueAsString(R.ok("绑定成功")));
            }
        }

    }

}

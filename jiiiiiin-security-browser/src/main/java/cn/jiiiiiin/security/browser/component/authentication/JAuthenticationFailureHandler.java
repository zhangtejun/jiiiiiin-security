package cn.jiiiiiin.security.browser.component.authentication;

import cn.jiiiiiin.security.browser.support.SimpleResponse;
import cn.jiiiiiin.security.browser.utils.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * 登录失败处理接口
 * https://docs.spring.io/spring-security/site/docs/4.2.7.RELEASE/reference/htmlsingle/#form-login-flow-handling
 * <p>
 * 可以自定义实现`AuthenticationFailureHandler`接口，或者继承其下的子类
 * <p>
 * spring默认实现是调整到一个错误页面
 *
 * @author jiiiiiin
 */
@Component("jAuthenticationFailureHandler")
public class JAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    final static Logger L = LoggerFactory.getLogger(JAuthenticationFailureHandler.class);

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Autowired
    ObjectMapper objectMapper;

    /**
     * 登录失败之后被调用
     * <p>
     * {
     * "cause": null,
     * // 错误堆栈
     * "stackTrace": [
     * {
     * "methodName": "additionalAuthenticationChecks",
     * "fileName": "DaoAuthenticationProvider.java",
     * "lineNumber": 98,
     * "className": "org.springframework.security.authentication.dao.DaoAuthenticationProvider",
     * "nativeMethod": false
     * },
     * // ...
     * ],
     * // 对应`AuthenticationException`子类[BadCredentialsException/....]的代表含义，
     * "message": "坏的凭证",
     * "localizedMessage": "坏的凭证",
     * "suppressed": []
     * }
     *
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        L.info("身份认证（登录）失败", exception);
        // 根据渠道返回不同的响应数据
        final SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            final Device currentDevice = HttpUtils.resolveDevice(savedRequest.getHeaderValues("user-agent").get(0), savedRequest.getHeaderValues("accept").get(0));
            // 根据渠道返回不同的响应数据
            if (!currentDevice.isNormal()) {
                response.setStatus(INTERNAL_SERVER_ERROR.value());
                response.setContentType("application/json;charset=UTF-8");
                // 将authentication转换成json str输出
                response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse(exception.getMessage())));
            } else {
                // 默认是做重定向到登录之前的【期望访问资源】接口
                super.onAuthenticationFailure(request, response, exception);
            }
        } else {
            // 默认是做重定向到登录之前的【期望访问资源】接口
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}

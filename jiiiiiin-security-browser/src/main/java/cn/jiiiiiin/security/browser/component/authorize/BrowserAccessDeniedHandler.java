package cn.jiiiiiin.security.browser.component.authorize;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 默认的区分渠道的`AccessDeniedHandler`
 *
 * 1.如果ss中的安全配置了：.exceptionHandling().accessDeniedPage("/403") 则基类的实现是跳转页面
 * @author jiiiiiin
 */
@Setter
@Getter
public class BrowserAccessDeniedHandler extends AccessDeniedHandlerImpl {

    private String errorPage;

    @Autowired
    protected LiteDeviceResolver liteDeviceResolver;

    public BrowserAccessDeniedHandler(String errorPage) {
        this.errorPage = errorPage;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (!response.isCommitted()) {
            final Device currentDevice = liteDeviceResolver.resolveDevice(request);
            if (!currentDevice.isNormal()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        accessDeniedException.getMessage());
            } else {
                // Put exception into request scope (perhaps of use to a view)
                request.setAttribute(WebAttributes.ACCESS_DENIED_403,
                        accessDeniedException);

                // Set the 403 status code.
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                // forward to error page.
                RequestDispatcher dispatcher = request.getRequestDispatcher(errorPage);
                dispatcher.forward(request, response);
            }
        }
    }

}

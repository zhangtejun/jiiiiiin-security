package cn.jiiiiiin.security.browser;

import cn.jiiiiiin.security.browser.support.SimpleResponse;
import cn.jiiiiiin.security.core.config.SecurityCoreConfig;
import cn.jiiiiiin.security.core.properties.SecurityProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mobile.device.Device;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jiiiiiin:
 *   security:
 *     browser:
 *       loginPage = /demo-signIn.html
 * @author jiiiiiin
 */
@RestController
public class BrowserSecurityController {

    final static Logger L = LoggerFactory.getLogger(BrowserSecurityController.class);

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 当框架认为需要进行身份认证，会将请求缓存到requestCache中，这里我们在登录完成之后，从这个对象中拿出框架帮我们缓存的上一个被拦截的请求
     */
    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * 登录处理接口
     * 即当需要身份认证时候需要访问该接口
     *
     * 对应spring security的`.loginPage("/signIn")`配置
     * 将区分请求的渠道进行不同的响应
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/signIn")
    // 返回的状态码标识返回给非网页版客户端，标识需要进行用户授权
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response, Device device) throws IOException {
        // 获取到上一个被拦截的请求(原始请求）
        final SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            final String transTarget = savedRequest.getRedirectUrl();
            L.info("需要进行身份认证的请求是 {}", transTarget);
            // 检测请求是否是以html结尾，我们就认为是访问网页版本
            // if(StringUtils.endsWithIgnoreCase(transTarget, ".html")){
            // 借助spring mobile来区分渠道
            if (device.isNormal()) {
                // 直接跳转到登录页面
                redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
            }
        }
        return SimpleResponse.newInstance("访问的服务需要身份认证");
    }
}

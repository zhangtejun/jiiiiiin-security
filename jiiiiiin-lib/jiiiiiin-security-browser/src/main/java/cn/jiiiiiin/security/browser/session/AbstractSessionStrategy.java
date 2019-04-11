/**
 *
 */
package cn.jiiiiiin.security.browser.session;

import cn.jiiiiiin.security.core.properties.SecurityProperties;
import com.baomidou.mybatisplus.extension.api.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 抽象的session失效处理器
 *
 * @author zhailiang
 */
@Slf4j
public class AbstractSessionStrategy {

    /**
     * 跳转的url
     */
    private String destinationUrl;
    /**
     * 系统配置信息
     */
    private SecurityProperties securityProperties;
    /**
     * 重定向策略
     */
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    /**
     * 跳转前是否创建新的session
     */
    private boolean createNewSession = true;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    protected LiteDeviceResolver liteDeviceResolver;

    /**
     */
    public AbstractSessionStrategy(SecurityProperties securityProperties) {
        String invalidSessionUrl = securityProperties.getBrowser().getSession().getSessionInvalidUrl();
        Assert.isTrue(UrlUtils.isValidRedirectUrl(invalidSessionUrl), "url must start with '/' or with 'http(s)'");
        Assert.isTrue(StringUtils.isNotEmpty(invalidSessionUrl), "url must be not empty");
        this.destinationUrl = invalidSessionUrl;
        this.securityProperties = securityProperties;
    }

    /**
     * 在session过期之后的处理逻辑
     * <p>
     * 对渠道进行响应内容格式控制
     *
     * @see org.springframework.security.web.session.InvalidSessionStrategy#
     * onInvalidSessionDetected(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    protected void onSessionInvalid(HttpServletRequest request, HttpServletResponse response) throws IOException {

        log.info("session失效");

        if (createNewSession) {
            request.getSession();
        }

        String sourceUrl = request.getRequestURI();
        String targetUrl;

        final Device currentDevice = liteDeviceResolver.resolveDevice(request);

        if (currentDevice.isNormal()) {
            if (StringUtils.equals(sourceUrl, securityProperties.getBrowser().getSignInUrl())
                //|| StringUtils.equals(sourceUrl, securityProperties.getBrowser().getSignOutUrl())
                    ) {
                targetUrl = sourceUrl;
            } else {
                targetUrl = destinationUrl;
            }
            targetUrl = buildRedirectUrl(request, targetUrl);
            log.info("跳转到:" + targetUrl);
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else {
            Object result = buildResponseContent(request);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(result));
        }

    }

    protected String buildRedirectUrl(HttpServletRequest request, String targetUrl) {
        if (isConcurrency(request)) {
            targetUrl += "?flag=会话已失效，并发登录导致";
        }
        return targetUrl;
    }

    protected R<Object> buildResponseContent(HttpServletRequest request) {
        var message = "会话已失效";
        if (isConcurrency(request)) {
            message = message + "，并发登录导致的";
        }
        return R.failed(message).setCode(-2L);
    }

    /**
     * session失效是否是并发导致的
     *
     * {@link CustomSessionInformationExpiredStrategy}
     *
     * @param request
     * @return true 标识是并发登录导致的会话剔除，false则不是
     */
    protected boolean isConcurrency(HttpServletRequest request) {
        val flag = request.getAttribute(CustomSessionInformationExpiredStrategy.EXPIRED_SESSION_DETECTED_STATUS);
        return flag != null && (boolean) flag;
    }

    /**
     * Determines whether a new session should be created before redirecting (to
     * avoid possible looping issues where the same session ID is sent with the
     * redirected request). Alternatively, ensure that the configured URL does
     * not pass through the {@code SessionManagementFilter}.
     *
     * @param createNewSession defaults to {@code true}.
     */
    public void setCreateNewSession(boolean createNewSession) {
        this.createNewSession = createNewSession;
    }

}

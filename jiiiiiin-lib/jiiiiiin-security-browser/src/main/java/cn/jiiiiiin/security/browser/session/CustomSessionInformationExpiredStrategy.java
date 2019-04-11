/**
 *
 */
package cn.jiiiiiin.security.browser.session;

import cn.jiiiiiin.security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 并发登录导致session失效时，默认的处理策略
 * <p>
 * 即登录用户被“踢掉”的处理
 *
 * @author zhailiang
 * @author jiiiiiin
 */
@Slf4j
public class CustomSessionInformationExpiredStrategy extends AbstractSessionStrategy implements SessionInformationExpiredStrategy {

    public static final String EXPIRED_SESSION_DETECTED_STATUS = "EXPIRED_SESSION_DETECTED_STATUS";

    public CustomSessionInformationExpiredStrategy(SecurityProperties securityProperties) {
        super(securityProperties);
    }

    /**
     * @param event session超时事件，可以获取到“踢掉”用户的请求和响应对象
     *              在请求对象中，就可以获知是哪个登录导致当前登录用户被剔除等
     * @see org.springframework.security.web.session.SessionInformationExpiredStrategy#onExpiredSessionDetected(org.springframework.security.web.session.SessionInformationExpiredEvent)
     */
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        // TODO 这里可以做日志的记录等操作
        log.info("发生并发登录剔除事件");
        event.getRequest().setAttribute(EXPIRED_SESSION_DETECTED_STATUS, true);
        onSessionInvalid(event.getRequest(), event.getResponse());
    }

    @Override
    protected boolean isConcurrency(HttpServletRequest request) {
        return true;
    }

}

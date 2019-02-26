package cn.jiiiiiin.manager.component.authentication;

import cn.jiiiiiin.security.browser.component.authentication.BrowserAuthenticationFailureHandler;
import cn.jiiiiiin.security.core.dict.CommonConstants;
import com.baomidou.mybatisplus.extension.api.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理接口
 *
 * @author jiiiiiin
 */
@Slf4j
@Component("authenticationFailureHandler")
public class MngAuthenticationFailureHandler extends BrowserAuthenticationFailureHandler {

    @Override
    protected void respJson(HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setContentType(CommonConstants.CONTENT_TYPE_JSON);
        // 将authentication转换成json str输出
        response.getWriter().write(objectMapper.writeValueAsString(R.failed(exception.getMessage())));
    }
}

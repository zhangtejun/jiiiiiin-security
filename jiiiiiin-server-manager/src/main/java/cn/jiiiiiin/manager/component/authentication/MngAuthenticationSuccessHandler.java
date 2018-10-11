package cn.jiiiiiin.manager.component.authentication;


import cn.jiiiiiin.security.browser.component.authentication.BrowserAuthenticationSuccessHandler;
import cn.jiiiiiin.security.core.dict.CommonConstants;
import com.baomidou.mybatisplus.extension.api.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录成功处理接口
 *
 * @author jiiiiiin
 */
@Slf4j
@Component("authenticationSuccessHandler")
public class MngAuthenticationSuccessHandler extends BrowserAuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void respJson(HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType(CommonConstants.CONTENT_TYPE_JSON);
        response.getWriter().write(objectMapper.writeValueAsString(R.ok(authentication)));
    }
}

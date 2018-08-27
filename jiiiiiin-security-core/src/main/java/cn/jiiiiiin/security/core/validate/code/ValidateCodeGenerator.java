package cn.jiiiiiin.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jiiiiiin
 */
public interface ValidateCodeGenerator {

    /**
     * 生成图形验证码
     *
     * @param request
     * @return
     */
    ImageCode generate(HttpServletRequest request);
}

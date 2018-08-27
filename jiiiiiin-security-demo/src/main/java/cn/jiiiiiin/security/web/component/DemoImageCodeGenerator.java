package cn.jiiiiiin.security.web.component;

import cn.jiiiiiin.security.core.validate.code.ImageCode;
import cn.jiiiiiin.security.core.validate.code.ValidateCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 测试由应用实现框架需要的bean
 */
//@Component("imageCodeGenerator")
public class DemoImageCodeGenerator implements ValidateCodeGenerator {

    final static Logger L = LoggerFactory.getLogger(DemoImageCodeGenerator.class);

    @Override
    public ImageCode generate(HttpServletRequest request) {
        L.info("生成图形验证码");
        return null;
    }
}

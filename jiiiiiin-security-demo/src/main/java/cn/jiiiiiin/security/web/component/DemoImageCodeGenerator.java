package cn.jiiiiiin.security.web.component;

import cn.jiiiiiin.security.core.validate.code.ValidateCodeGenerator;
import cn.jiiiiiin.security.core.validate.code.entity.ValidateCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 测试由应用实现框架需要的bean
 */
//@Component("imageCodeGenerator")
public class DemoImageCodeGenerator implements ValidateCodeGenerator {

    final static Logger L = LoggerFactory.getLogger(DemoImageCodeGenerator.class);

    @Override
    public ValidateCode generate(ServletWebRequest request) {
        L.info("生成图形验证码");
        return null;
    }
}

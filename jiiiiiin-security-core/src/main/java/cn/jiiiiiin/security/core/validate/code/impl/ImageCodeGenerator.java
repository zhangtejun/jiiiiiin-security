package cn.jiiiiiin.security.core.validate.code.impl;

import cn.jiiiiiin.security.core.properties.SecurityProperties;
import cn.jiiiiiin.security.core.validate.code.ImageCode;
import cn.jiiiiiin.security.core.validate.code.ValidateCodeGenerator;
import com.google.code.kaptcha.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;

import static cn.jiiiiiin.security.core.validate.code.ValidateCodeController.KEY_EXPIRE_IN;

/**
 * 默认的图形验证码生成器
 *
 * @author jiiiiiin
 */
public class ImageCodeGenerator implements ValidateCodeGenerator {

    final static Logger L = LoggerFactory.getLogger(ImageCodeGenerator.class);

    private final SecurityProperties securityProperties;

    private final Producer captchaProducer;

    public ImageCodeGenerator(SecurityProperties securityProperties, Producer captchaProducer) {
        this.securityProperties = securityProperties;
        this.captchaProducer = captchaProducer;
    }

    @Override
    public ImageCode generate(HttpServletRequest request) {
        final int expireIn = ServletRequestUtils.getIntParameter(request, KEY_EXPIRE_IN, securityProperties.getValidate().getImageCode().getExpireIn());
        L.debug("验证码有效期 {}", expireIn);
        // 生成验证码
        final String capText = captchaProducer.createText();
        final BufferedImage bi = captchaProducer.createImage(capText);
        final ImageCode imageCode = new ImageCode(capText, bi, expireIn);
        return imageCode;
    }
}

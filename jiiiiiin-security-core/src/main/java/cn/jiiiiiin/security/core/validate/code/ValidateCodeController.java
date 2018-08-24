package cn.jiiiiiin.security.core.validate.code;

import com.google.code.kaptcha.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 校验码提供控制器
 *
 * @author jiiiiiin
 */
@RestController
public class ValidateCodeController {

    final static Logger L = LoggerFactory.getLogger(ValidateCodeController.class);

    private static final String KEY_IMAGE_CODE = "KEY_IMAGE_CODE";

    private SessionStrategy sessionStrategy =  new HttpSessionSessionStrategy();

    @Autowired
    private Producer captchaProducer;

    @GetMapping("/code/image")
    public void createImageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final ImageCode imageCode = _createImageCode(request);
        L.info("生成图形验证码 code: {}", imageCode.getCode());
        // session strategy会从指定设置响应的值到ServletRequestAttributes请求只有对象对应的session中
        sessionStrategy.setAttribute(new ServletRequestAttributes(request), KEY_IMAGE_CODE, imageCode.getCode());
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }

    private ImageCode _createImageCode(HttpServletRequest request) {
        // 生成验证码
        final String capText = captchaProducer.createText();
        final BufferedImage bi = captchaProducer.createImage(capText);
        final ImageCode imageCode = new ImageCode(capText, bi, 60);
        return imageCode;
    }

}

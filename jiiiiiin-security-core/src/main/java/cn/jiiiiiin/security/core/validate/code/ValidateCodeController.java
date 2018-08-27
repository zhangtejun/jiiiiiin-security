package cn.jiiiiiin.security.core.validate.code;

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
import java.io.IOException;

/**
 * 校验码提供控制器
 *
 * @author jiiiiiin
 */
@RestController
public class ValidateCodeController {

    final static Logger L = LoggerFactory.getLogger(ValidateCodeController.class);

    public static final String KEY_IMAGE_CODE = "KEY_IMAGE_CODE";
    /**
     * 表单图形验证码input中的name
     */
    public static final String IMAGE_CODE = "imageCode";
    public static final String KEY_EXPIRE_IN = "expireIn";

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Autowired
    private ValidateCodeGenerator imageCodeGenerator;

    @GetMapping("/code/image")
    public void createImageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final ImageCode imageCode = imageCodeGenerator.generate(request);
        L.info("生成图形验证码 code: {}", imageCode.getCode());
        // session strategy会从指定设置响应的值到ServletRequestAttributes请求只有对象对应的session中
        sessionStrategy.setAttribute(new ServletRequestAttributes(request), KEY_IMAGE_CODE, imageCode);
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }

}

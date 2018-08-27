package cn.jiiiiiin.security.core.validate.code;

import cn.jiiiiiin.security.core.properties.SecurityProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 图形验证码校验
 * 在执行@see UsernamePasswordAuthenticationFilter之前执行
 *
 * @author jiiiiiin
 */
@Component
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    final static Logger L = LoggerFactory.getLogger(ValidateCodeFilter.class);

    /**
     * 身份认证表单提交的接口
     */
    public static final String LOGIN_PROCESSING_URL = "/authentication/from";

    @Autowired
    SecurityProperties securityProperties;

    @Autowired
    private final AuthenticationFailureHandler jAuthenticationFailureHandler;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    private Set<String> interceptorUrls = new HashSet<>();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        interceptorUrls.add(LOGIN_PROCESSING_URL);
        interceptorUrls.addAll(securityProperties.getValidate().getImageCode().getInterceptorUrls());
        L.info("验证码将会拦截的接口集合 {}", interceptorUrls);
    }

    public ValidateCodeFilter(AuthenticationFailureHandler jAuthenticationFailureHandler) {
        this.jAuthenticationFailureHandler = jAuthenticationFailureHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String uri = request.getRequestURI();
        boolean needCheck = false;
        for (String url : interceptorUrls) {
            if (antPathMatcher.match(url, uri)) {
                needCheck = true;
                break;
            }
        }
        // 拦截身份认证表单提交请求
        if (needCheck) {
            final ServletRequestAttributes attrs = new ServletRequestAttributes(request);
            final ImageCode imageCode = (ImageCode) sessionStrategy.getAttribute(attrs, ValidateCodeController.KEY_IMAGE_CODE);
            // 如果session中存在imageCode标明需要进行校验
            if (imageCode != null) {
                try {
                    L.info("进行图形验证码校验");
                    validate(imageCode, request, attrs);
                } catch (ValidateCodeException e) {
                    jAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private void validate(ImageCode imageCode, HttpServletRequest request, ServletRequestAttributes attrs) {
        // 获取用户输入的验证码
        try {
            final String validateCode = ServletRequestUtils.getStringParameter(request, ValidateCodeController.IMAGE_CODE);
            final String realValidateCode = imageCode.getCode();
            L.info("验证码 imageCode::code {} validateCode {}", realValidateCode, validateCode);
            if (StringUtils.isBlank(validateCode)) {
                throw new ValidateCodeException("验证码不能为空");
            }
            if (imageCode.isExpired()) {
                sessionStrategy.removeAttribute(attrs, ValidateCodeController.KEY_IMAGE_CODE);
                throw new ValidateCodeException("验证码已经过期");
            }
            if (!StringUtils.equalsIgnoreCase(realValidateCode, validateCode)) {
                throw new ValidateCodeException("验证码不匹配");
            }
            // 验证通过
            sessionStrategy.removeAttribute(attrs, ValidateCodeController.KEY_IMAGE_CODE);
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取图形验证码失败，请检查是否输入了图形验证码");
        }

    }
}

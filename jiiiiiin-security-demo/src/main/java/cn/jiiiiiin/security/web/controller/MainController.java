package cn.jiiiiiin.security.web.controller;

import cn.jiiiiiin.security.core.properties.SecurityProperties;
import cn.jiiiiiin.security.dto.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jiiiiiin
 */
@Controller
public class MainController {

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    private RequestCache requestCache = new HttpSessionRequestCache();

    /**
     * 系统授权注册接口，提供给第三方授权之后，为查询到业务系统的userid，即没有记录时候渲染的注册页面使用
     * <p>
     * 如果注册页面需要获取第三方授权用户信息，可以shiyong {@link cn.jiiiiiin.security.browser.controller.BrowserSecurityController#getSocialUserInfo(HttpServletRequest)}
     *
     * @param user
     * @param request
     * @see cn.jiiiiiin.security.core.social.SocialConfig#socialSecurityConfig
     * <p>
     * 如果期望让用户进行第三方授权登录之后，自动帮用户创建业务系统的用户记录，完成登录，而无需跳转到下面这个接口进行注册，请看：
     * @see org.springframework.social.security.SocialAuthenticationProvider#toUserId 去获取userIds的方法，在{@link org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository#findUserIdsWithConnection}中通过注入{@link ConnectionSignUp}完成
     */
    @PostMapping("/user/auth/register")
    public String register(User user, HttpServletRequest request, HttpServletResponse response) {
        // TODO 待写注册或者绑定逻辑（绑定需要查询应用用户的userid，通过授权用户信息，授权用户信息在providerSignInUtils中可以获取）
        // 不管是注册用户还是绑定用户，都会拿到一个用户唯一标识。
        String userId = user.getUsername();
        // 真正让业务系统用户信息和spring social持有的授权用户信息进行绑定，记录`UserConnection`表
        providerSignInUtils.doPostSignUp(userId, new ServletWebRequest(request));
//		appSingUpUtils.doPostSignUp(new ServletWebRequest(request), userId);
        // ！注册成功还需要用户在重新登录才能进来，目前没有找到直接帮用户进行ss登录的方法
        final SavedRequest savedRequest = requestCache.getRequest(request, response);
        final String originalReqUrl = savedRequest.getRedirectUrl();
        if (!StringUtils.isBlank(originalReqUrl)) {
            return "redirect:" + originalReqUrl;
        } else {
            return "redirect:/";
        }
    }

}

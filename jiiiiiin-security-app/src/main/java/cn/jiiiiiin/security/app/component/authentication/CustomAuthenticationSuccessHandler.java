/**
 * 
 */
package cn.jiiiiiin.security.app.component.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * APP环境下认证成功处理器
 * 
 * @author zhailiang
 *
 */
@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	final static Logger L = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

//	@Autowired
//	private ObjectMapper objectMapper;
//
//	@Autowired
//	private ClientDetailsService clientDetailsService;
//
//	@Autowired
//	private AuthorizationServerTokenServices authorizationServerTokenServices;

	@Autowired
	ObjectMapper objectMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.web.authentication.
	 * AuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * org.springframework.security.core.Authentication)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

//		L.info("身份认证（登录）成功");
//		final SavedRequest savedRequest = requestCache.getRequest(request, response);
//		if (savedRequest != null) {
//			final Device currentDevice = HttpUtils.resolveDevice(savedRequest.getHeaderValues("user-agent").get(0), savedRequest.getHeaderValues("accept").get(0));
//			// 根据渠道返回不同的响应数据
//			// 还有一种做法是根据客户端程序配置来指定响应数据格式：https://coding.imooc.com/lesson/134.html#mid=6866
//			if (!currentDevice.isNormal()) {
				response.setContentType("application/json;charset=UTF-8");
				// 将authentication转换成json str输出
				response.getWriter().write(objectMapper.writeValueAsString(authentication));
//			} else {
//				// 默认是做重定向到登录之前的【期望访问资源】接口
//				super.onAuthenticationSuccess(request, response, authentication);
//			}
//		} else {
//			// 默认是做重定向到登录之前的【期望访问资源】接口
//			super.onAuthenticationSuccess(request, response, authentication);
//		}
	}

	private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException {

		byte[] base64Token = header.substring(6).getBytes("UTF-8");
		byte[] decoded;
		try {
			decoded = Base64.decode(base64Token);
		} catch (IllegalArgumentException e) {
			throw new BadCredentialsException("Failed to decode basic authentication token");
		}

		String token = new String(decoded, "UTF-8");

		int delim = token.indexOf(":");

		if (delim == -1) {
			throw new BadCredentialsException("Invalid basic authentication token");
		}
		return new String[] { token.substring(0, delim), token.substring(delim + 1) };
	}

}

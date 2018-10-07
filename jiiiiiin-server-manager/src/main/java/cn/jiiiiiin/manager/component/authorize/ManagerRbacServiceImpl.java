/**
 *
 */
package cn.jiiiiiin.manager.component.authorize;

import cn.jiiiiiin.module.common.entity.mngauth.Admin;
import cn.jiiiiiin.security.rbac.component.service.RbacService;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @author zhailiang
 */
@Component("rbacService")
public class ManagerRbacServiceImpl implements RbacService {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 需要读取用户所拥有权限的所有URL
     * 通过用户标识-》用户角色-》角色拥有的资源
     *
     * @param request        请求信息
     * @param authentication 身份认证信息
     * @return
     */
    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        // principal即系统的UserDetailsService返回的用户标识对象，如果没有通过认证则是一个匿名的字符串
        Object principal = authentication.getPrincipal();

        boolean hasPermission = false;
		if (principal instanceof Admin) {
            val admin = (Admin) principal;
			//如果用户名是admin，就永远返回true
//			if (StringUtils.equals(admin.getUsername(), "admin")) {
//				hasPermission = true;
//			} else {
				// 读取用户所拥有权限的所有URL
                // 通过用户标识-》用户角色-》角色拥有的资源
//				Set<String> urls = ((Admin) principal).getUrls();
//				for (String url : urls) {
//                    // 进行权限匹配
//					if (antPathMatcher.match(url, request.getRequestURI())) {
//						hasPermission = true;
//						break;
//					}
//				}
//			}
		}

        return hasPermission;
    }

}

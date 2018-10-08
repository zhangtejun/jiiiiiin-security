/**
 *
 */
package cn.jiiiiiin.manager.component.authorize;

import cn.jiiiiiin.module.common.entity.mngauth.Admin;
import cn.jiiiiiin.module.common.entity.mngauth.Role;
import cn.jiiiiiin.security.rbac.component.dict.RbacDict;
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
            val hasAdminRole = admin.getRoles().stream()
                    // TODO 可以作为配置化
                    .anyMatch(role -> role.getAuthorityName().equals(RbacDict.ROLE_ADMIN_AUTHORITY_NAME));
            if (hasAdminRole) {
                hasPermission = true;
            } else {
                val hasDBAdminRole = admin.getRoles().stream()
                        // TODO 可以作为配置化
                        .anyMatch(role -> role.getAuthorityName().equals(RbacDict.ROLE_DB_ADMIN_AUTHORITY_NAME));
                val reqURI = request.getRequestURI();
                if (hasDBAdminRole && antPathMatcher.match(RbacDict.DB_URI, reqURI)) {
                    hasPermission = true;
                } else {
                    val reqMethod = request.getMethod();
                    // 读取用户所拥有权限的所有URL
                    // 通过用户标识-》用户角色-》角色拥有的资源
                    val roles = admin.getRoles();
                    val iterator = roles.iterator();
                    while (iterator.hasNext()) {
                        val role = iterator.next();
                        // 进行权限匹配
                        val res = role.getResources().stream()
                                .anyMatch(resource -> antPathMatcher.match(resource.getUrl(), reqURI)
                                        && request.getMethod().equalsIgnoreCase(reqMethod));
                        if (res) {
                            hasPermission = true;
                            break;
                        }
                    }
                }
            }
        }

        return hasPermission;
    }

}

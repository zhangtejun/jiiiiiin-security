package cn.jiiiiiin.security.core.dict;

/**
 * 一些非配置的通用常量
 *
 * @author jiiiiiin
 */
public class CommonConstants {

    public static final String GET = "GET";
    public static final String POST = "POST";
    /**
     * 提示信息
     * <p>
     * 设置到{@link org.springframework.ui.Model}中的属性key
     */
    public static final String MODEL_KEY_HINT_MSG = "hintMsg";
    /**
     * token 授权需要传递到请求头的 Authorization字段前缀
     */
    public static final String DEFAULT_HEADER_NAME_AUTHORIZATION_PRIFIX = "bearer ";
}

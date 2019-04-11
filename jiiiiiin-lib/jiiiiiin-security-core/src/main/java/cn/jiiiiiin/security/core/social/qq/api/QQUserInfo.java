/**
 *
 */
package cn.jiiiiiin.security.core.social.qq.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 请求用户授权信息的封装对象
 * <p>
 * 参考qq官方文档：
 * http://wiki.connect.qq.com/get_user_info
 * <p>
 * success：
 * {
 * "figureurl": "http://qzapp.qlogo.cn/qzapp/111111/942FEA70050EEAFBD4DCE2C1FC775E56/30",
 * "figureurl_1": "http://qzapp.qlogo.cn/qzapp/111111/942FEA70050EEAFBD4DCE2C1FC775E56/50",
 * "figureurl_2": "http://qzapp.qlogo.cn/qzapp/111111/942FEA70050EEAFBD4DCE2C1FC775E56/100",
 * "figureurl_qq_1": "http://q.qlogo.cn/qqapp/100312990/DE1931D5330620DBD07FB4A5422917B6/40",
 * "figureurl_qq_2": "http://q.qlogo.cn/qqapp/100312990/DE1931D5330620DBD07FB4A5422917B6/100",
 * "gender": "男",
 * "is_yellow_vip": "1",
 * "is_yellow_year_vip": "1",
 * "level": "7",
 * "msg": "",
 * "nickname": "Peter",
 * "ret": 0,
 * "vip": "1",
 * "yellow_vip_level": "7"
 * }
 * <p>
 * err:
 * { "ret":1002, "msg":"请先登录" }
 *
 * @author zhailiang
 */
@Getter
@Setter
@ToString
public class QQUserInfo {

    /**
     * 返回码
     */
    private String ret;
    /**
     * 如果ret<0，会有相应的错误信息提示，返回数据全部用UTF-8编码。
     */
    private String msg;
    /**
     *
     */
    private String openId;
    /**
     * 不知道什么东西，文档上没写，但是实际api返回里有。
     */
    private String is_lost;
    /**
     * 省(直辖市)
     */
    private String province;
    /**
     * 市(直辖市区)
     */
    private String city;
    /**
     * 出生年月
     */
    private String year;
    /**
     * 用户在QQ空间的昵称。
     */
    private String nickname;
    /**
     * 大小为30×30像素的QQ空间头像URL。
     */
    private String figureurl;
    /**
     * 大小为50×50像素的QQ空间头像URL。
     */
    private String figureurl_1;
    /**
     * 大小为100×100像素的QQ空间头像URL。
     */
    private String figureurl_2;

    private String figureurl_qq;
    /**
     * 大小为40×40像素的QQ头像URL。
     */
    private String figureurl_qq_1;
    /**
     * 大小为100×100像素的QQ头像URL。需要注意，不是所有的用户都拥有QQ的100×100的头像，但40×40像素则是一定会有。
     */
    private String figureurl_qq_2;
    /**
     * 性别。 如果获取不到则默认返回”男”
     */
    private String gender;
    /**
     * 标识用户是否为黄钻用户（0：不是；1：是）。
     */
    private String is_yellow_vip;
    /**
     * 标识用户是否为黄钻用户（0：不是；1：是）
     */
    private String vip;
    /**
     * 黄钻等级
     */
    private String yellow_vip_level;
    /**
     * 黄钻等级
     */
    private String level;
    /**
     * 标识是否为年费黄钻用户（0：不是； 1：是）
     */
    private String is_yellow_year_vip;
    /**
     * constellation :
     */
    private String constellation;
    private String figureurl_type;

}

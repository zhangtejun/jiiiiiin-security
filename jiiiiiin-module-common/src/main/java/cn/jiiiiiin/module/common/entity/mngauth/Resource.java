package cn.jiiiiiin.module.common.entity.mngauth;

import cn.jiiiiiin.data.orm.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * <p>
 * 权限资源表
 * </p>
 *
 * @author jiiiiiin
 * @since 2018-09-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mng_resource")
@ApiModel(value="Resource对象", description="权限资源表")
public class Resource extends BaseEntity<Resource> {

    /**
     * {@link Resource#channel}
     */
    public static final Integer CHANNEL_SPA = 0;
    /**
     * 根节点
     */
    public static final Long IS_ROOT_MENU = 0L;

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜单父id")
    private Long pid;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "url地址")
    private String url;

    @ApiModelProperty(value = "接口类型，如GET标识添加")
    private String method;

    @ApiModelProperty(value = "菜单排序号")
    private Integer num;

    @ApiModelProperty(value = "菜单层级")
    private Integer levels;

    @ApiModelProperty(value = "是否是菜单:   1:是 0:不是")
    private Integer ismenu;

    @ApiModelProperty(value = "菜单状态:    1:启用   0:不启用")
    private Integer status;

    @Deprecated
    @ApiModelProperty(value = "是否打开:    1:打开   0:不打开")
    private Integer isopen;

    @ApiModelProperty(value = "标识渠道，不同的渠道就是不同的资源分组:    0: 前端资源 1: 后台资源")
    private Integer channel;

    public static final String PID = "pid";

    public static final String NAME = "name";

    public static final String ICON = "icon";

    public static final String URL = "url";

    public static final String NUM = "num";

    public static final String LEVELS = "levels";

    public static final String ISMENU = "ismenu";

    public static final String METHOD = "method";

    public static final String STATUS = "status";

    public static final String ISOPEN = "isopen";

    public static final String CHANNEL = "channel";

    public enum MENU {
        Y(1),
        N(0);

        public Integer ismenu;

        MENU(Integer ismenu) {
            this.ismenu = ismenu;
        }
    }

}

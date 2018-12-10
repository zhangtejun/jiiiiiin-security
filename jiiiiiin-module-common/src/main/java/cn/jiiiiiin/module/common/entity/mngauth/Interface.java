package cn.jiiiiiin.module.common.entity.mngauth;

import cn.jiiiiiin.module.common.enums.common.ChannelEnum;
import cn.jiiiiiin.module.common.enums.common.StatusEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import cn.jiiiiiin.data.orm.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * <p>
 * 系统接口表
 * </p>
 *
 * @author jiiiiiin
 * @since 2018-12-09
 */
@Data
@Accessors(chain = true)
@TableName("mng_interface")
@ApiModel(value="Interface对象", description="系统接口表")
public class Interface extends BaseEntity<Interface> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "接口名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "接口地址")
    @TableField("url")
    private String url;

    @ApiModelProperty(value = "接口类型，如GET标识添加")
    @TableField("method")
    private String method;

    @ApiModelProperty(value = "接口描述")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "接口状态:  1:启用   0:不启用")
    @TableField("status")
    private StatusEnum status;

    @ApiModelProperty(value = "标识渠道，不同的渠道就是不同的资源分组: 0:内管")
    @TableField("channel")
    private ChannelEnum channel;


    public static final String URL = "url";

    public static final String NAME = "name";

    public static final String METHOD = "method";

    public static final String DESCRIPTION = "description";

    public static final String STATUS = "status";

    public static final String CHANNEL = "channel";

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Interface)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Interface that = (Interface) o;
        return Objects.equals(url, that.url) &&
                Objects.equals(method, that.method) &&
                channel == that.channel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), url, method, channel);
    }
}

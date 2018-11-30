package cn.jiiiiiin.module.common.entity.mngauth;

import cn.jiiiiiin.module.common.enums.common.StatusEnum;
import cn.jiiiiiin.module.common.enums.common.ChannelEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.jiiiiiin.data.orm.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author jiiiiiin
 * @since 2018-09-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mng_role")
@ApiModel(value="Role对象", description="角色表")
public class Role extends BaseEntity<Role> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色标识")
    private String authorityName;

    @ApiModelProperty(value = "标识渠道，不同的渠道就是不同的资源分组: 0:内管")
    private ChannelEnum channel;

    @TableField(exist = false)
    private List<Resource> resources = new LinkedList<>();

    public static final String NAME = "name";

    public static final String AUTHORITY_NAME = "authority_name";

    public static final String CHANNEL = "channel";

}

package cn.jiiiiiin.module.common.entity.mngauth;

import cn.jiiiiiin.data.orm.entity.BaseEntity;
import cn.jiiiiiin.module.common.enums.common.ChannelEnum;
import cn.jiiiiiin.module.common.exception.BusinessErrException;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;
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
@Getter
@Setter
@Accessors(chain = true)
@TableName("mng_role")
@ApiModel(value = "Role对象", description = "角色表")
public class Role extends BaseEntity<Role> {

    public static final Long ROLE_ADMIN_ID = 1061277220292595713L;

    public static void checkRootRole(@NonNull Role role, @NonNull String errMsg){
        if((role.getId() != null && role.getId().equals(ROLE_ADMIN_ID))
                || "Admin".equals(role.getAuthorityName())
                || "系统管理员".equals(role.getName())) {
            throw new BusinessErrException(errMsg);
        }
    }

    public static void checkRootRole(@NonNull Collection<? extends Serializable> idList, @NonNull String errMsg){
        if(idList.stream().anyMatch(p -> p.equals(ROLE_ADMIN_ID))) {
            throw new BusinessErrException(errMsg);
        }
    }

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

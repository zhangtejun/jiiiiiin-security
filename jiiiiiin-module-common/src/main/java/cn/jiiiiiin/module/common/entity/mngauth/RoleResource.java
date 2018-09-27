package cn.jiiiiiin.module.common.entity.mngauth;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import cn.jiiiiiin.data.orm.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色资源关联表
 * </p>
 *
 * @author jiiiiiin
 * @since 2018-09-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mng_role_resource")
@ApiModel(value="RoleResource对象", description="角色资源关联表")
public class RoleResource extends BaseEntity<RoleResource> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色主键")
    private Long roleId;

    @ApiModelProperty(value = "资源主键")
    private Long resourceId;


    public static final String ROLE_ID = "role_id";

    public static final String RESOURCE_ID = "resource_id";

}

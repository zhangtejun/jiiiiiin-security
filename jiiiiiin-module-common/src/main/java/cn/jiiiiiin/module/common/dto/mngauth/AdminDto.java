package cn.jiiiiiin.module.common.dto.mngauth;

import cn.jiiiiiin.module.common.entity.mngauth.Admin;
import cn.jiiiiiin.module.common.entity.mngauth.Interface;
import cn.jiiiiiin.module.common.entity.mngauth.Resource;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;

/**
 * @author jiiiiiin
 */
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class AdminDto extends Admin {

    private static final long serialVersionUID = -4730185793489105233L;

    private Long createTimestamp;

    private String createTimeStr;

    private String[] roleIds;

    @ApiModelProperty(value = "登录用户具有的接口权限集合")
    @TableField(exist = false)
    private HashSet<Interface> authorizeInterfaces;

}

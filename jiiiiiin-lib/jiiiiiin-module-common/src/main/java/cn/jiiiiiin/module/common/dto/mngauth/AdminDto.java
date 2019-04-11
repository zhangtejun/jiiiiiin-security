package cn.jiiiiiin.module.common.dto.mngauth;

import cn.jiiiiiin.data.orm.util.View;
import cn.jiiiiiin.module.common.entity.mngauth.Admin;
import cn.jiiiiiin.module.common.entity.mngauth.Interface;
import cn.jiiiiiin.module.common.validation.Groups;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;

/**
 * @author jiiiiiin
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AdminDto extends Admin {

    private static final long serialVersionUID = -8768149378834759936L;
    @JsonView(View.SimpleView.class)
    private String createTimeStr;

    @JsonView(View.DetailView.class)
    @NotNull
    @Size(min = 1, groups = {Groups.Create.class})
    private String[] roleIds;

    @ApiModelProperty(value = "登录用户具有的接口权限集合")
    @TableField(exist = false)
    private HashSet<Interface> authorizeInterfaces;

}

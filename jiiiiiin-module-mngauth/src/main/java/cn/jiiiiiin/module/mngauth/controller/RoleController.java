package cn.jiiiiiin.module.mngauth.controller;


import cn.jiiiiiin.module.common.entity.mngauth.Role;
import cn.jiiiiiin.module.common.enums.common.ChannelEnum;
import cn.jiiiiiin.module.mngauth.service.IRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.jiiiiiin.module.common.controller.BaseController;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author jiiiiiin
 * @since 2018-09-27
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Autowired
    private IRoleService roleService;

    @GetMapping("{channel}/{current}/{size}")
    public R<IPage<Role>> list(@PathVariable ChannelEnum channel, @PathVariable Long current, @PathVariable Long size){
        return R.ok(roleService.page(new Page<Role>(current, size), new QueryWrapper<Role>().eq(Role.CHANNEL, channel)));
    }

    @GetMapping("{channel}/{current}/{size}/{authorityName}")
    public R<IPage<Role>> search(@PathVariable ChannelEnum channel, @PathVariable Long current, @PathVariable Long size, @PathVariable String authorityName){
        return R.ok(roleService.page(new Page<Role>(current, size), new QueryWrapper<Role>().eq(Role.CHANNEL, channel).eq(Role.AUTHORITY_NAME, authorityName)));
    }

}

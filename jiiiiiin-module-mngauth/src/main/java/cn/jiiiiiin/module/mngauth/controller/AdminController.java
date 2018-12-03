package cn.jiiiiiin.module.mngauth.controller;


import cn.jiiiiiin.module.common.controller.BaseController;
import cn.jiiiiiin.module.common.entity.mngauth.Admin;
import cn.jiiiiiin.module.common.enums.common.ChannelEnum;
import cn.jiiiiiin.module.mngauth.service.IAdminService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author jiiiiiin
 * @since 2018-09-27
 */
@RestController
@RequestMapping("/admin")
public class AdminController extends BaseController {

    @Autowired
    private IAdminService adminService;

    @GetMapping("{channel}/{current}/{size}")
    public R<IPage<Admin>> list(@PathVariable ChannelEnum channel, @PathVariable Long current, @PathVariable Long size) {
        return R.ok(adminService.page(new Page<Admin>(current, size), new QueryWrapper<Admin>().eq(Admin.CHANNEL, channel)));
    }

    @PostMapping("{channel}/{current}/{size}")
    public R<IPage<Admin>> search(@PathVariable ChannelEnum channel, @PathVariable Long current, @PathVariable Long size, @RequestBody Admin admin) {
        val qw = new QueryWrapper<Admin>()
                .eq(Admin.CHANNEL, channel);
        if (StringUtils.isNotEmpty(admin.getUsername())) {
            qw.like(Admin.USERNAME, admin.getUsername());
        }
        if (StringUtils.isNotEmpty(admin.getPhone())) {
            qw.like(Admin.PHONE, admin.getPhone());
        }
        if (StringUtils.isNotEmpty(admin.getEmail())) {
            qw.like(Admin.EMAIL, admin.getEmail());
        }
        return R.ok(adminService.page(new Page<Admin>(current, size), qw));
    }

    @PostMapping
    public R<Admin> create(@RequestBody Admin admin) {
        adminService.saveAdminAndRelationRecords(admin);
        return success(admin);
    }

    @PutMapping
    public R<Admin> update(@RequestBody Admin admin) {
        adminService.updateAdminAndRelationRecords(admin);
        return success(admin);
    }

    @DeleteMapping("{channel}/{id}")
    public R<Boolean> del(@PathVariable ChannelEnum channel, @PathVariable Long id) {
        return success(adminService.delAdminAndRelationRecords(id, channel));
    }

}

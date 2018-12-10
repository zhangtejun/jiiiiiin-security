package cn.jiiiiiin.module.mngauth.controller;


import cn.jiiiiiin.module.common.controller.BaseController;
import cn.jiiiiiin.module.common.dto.mngauth.AdminDto;
import cn.jiiiiiin.module.common.entity.mngauth.Admin;
import cn.jiiiiiin.module.common.enums.common.ChannelEnum;
import cn.jiiiiiin.module.common.exception.BusinessErrException;
import cn.jiiiiiin.module.mngauth.component.MngUserDetails;
import cn.jiiiiiin.module.mngauth.service.IAdminService;
import cn.jiiiiiin.security.rbac.component.dict.RbacDict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private SimpleGrantedAuthority adminSimpleGrantedAuthority;

    @ApiOperation(value = "用户记录分页查询接口", httpMethod = "GET")
    @GetMapping("{channel}/{current}/{size}")
    public R<IPage<AdminDto>> list(@PathVariable ChannelEnum channel, @PathVariable Long current, @PathVariable Long size) {
        return R.ok(adminService.pageAdminDto(new Page<>(current, size), channel, null));
    }

    @PostMapping("search/dto/{channel}/{current}/{size}")
    public R<IPage<AdminDto>> searchAdminDto(@PathVariable ChannelEnum channel, @PathVariable Long current, @PathVariable Long size, @RequestBody AdminDto admin) {
        return R.ok(adminService.pageAdminDto(new Page<AdminDto>(current, size), channel, admin));
    }

    @PostMapping("search/{channel}/{current}/{size}")
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


    /**
     * 获取当前登录的管理员信息
     */
    @GetMapping("/me")
    public R<AdminDto> me(@AuthenticationPrincipal UserDetails user) {
        return success((AdminDto) new AdminDto().setUsername(user.getUsername()));
    }

    @GetMapping("{id}")
    public R<AdminDto> getAdminAndRelationRecords(@PathVariable Long id) {
        return success(adminService.getAdminAndRelationRecords(id));
    }

    /**
     * 关联的角色记录，必须传递到{@link AdminDto#roleIds}字段中
     *
     * @param admin
     * @return
     */
    @PostMapping
    public R<AdminDto> create(@RequestBody AdminDto admin) {
        if (adminService.saveAdminAndRelationRecords(admin)) {
            return success(admin);
        } else {
            throw new BusinessErrException("添加用户失败");
        }
    }

    /**
     * 关联的角色记录，必须传递到{@link AdminDto#roleIds}字段中
     *
     * @param admin
     * @return
     */
    @PutMapping
    public R<AdminDto> update(@RequestBody AdminDto admin) {
        if (adminService.updateAdminAndRelationRecords(admin)) {
            return success(admin);
        } else {
            throw new BusinessErrException("修改用户记录失败");
        }
    }

    @PutMapping("pwd")
    public R<AdminDto> updatePwd(@RequestBody AdminDto admin, @AuthenticationPrincipal UserDetails user) {
        if (user.getAuthorities().stream().anyMatch(p -> p.equals(adminSimpleGrantedAuthority))) {
            adminService.updatePwd(admin);
        }
        return success(admin);
    }

    @DeleteMapping("dels/{ids}")
    public R<Boolean> dels(@PathVariable String ids) {
        _checkHasSelf(ids.split(","));
        return success(adminService.removeAdminsAndRelationRecords(ids));
    }

    /**
     * 检测待操作的用户是否包含登录用户自身，如果存在不允许操作
     *
     * @param ids
     */
    private void _checkHasSelf(String[] ids) {
        val userDetails = (MngUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (ArrayUtils.contains(ids, String.valueOf(userDetails.getAdmin().getId()))) {
            throw new BusinessErrException("当前接口服务，不允许操作登录用户自己");
        }
    }

    @DeleteMapping("{id}")
    public R<Boolean> del(@PathVariable Long id) {
        _checkHasSelf(new String[]{String.valueOf(id)});
        return success(adminService.removeAdminAndRelationRecord(id));
    }

}

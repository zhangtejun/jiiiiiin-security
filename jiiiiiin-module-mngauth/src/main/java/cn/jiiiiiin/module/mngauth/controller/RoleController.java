package cn.jiiiiiin.module.mngauth.controller;


import cn.jiiiiiin.module.common.entity.mngauth.Admin;
import cn.jiiiiiin.module.common.entity.mngauth.Resource;
import cn.jiiiiiin.module.common.entity.mngauth.Role;
import cn.jiiiiiin.module.common.enums.common.ChannelEnum;
import cn.jiiiiiin.module.mngauth.service.IRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.jiiiiiin.module.common.controller.BaseController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author jiiiiiin
 * @since 2018-09-27
 */
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Autowired
    private IRoleService roleService;

    @GetMapping("{channel}/{current}/{size}")
    public R<IPage<Role>> list(@PathVariable ChannelEnum channel, @PathVariable Long current, @PathVariable Long size) {
        return R.ok(roleService.page(new Page<Role>(current, size), new QueryWrapper<Role>().eq(Role.CHANNEL, channel)));
    }

    @GetMapping("{channel}/{current}/{size}/{authorityName}")
    public R<IPage<Role>> search(@PathVariable ChannelEnum channel, @PathVariable Long current, @PathVariable Long size, @PathVariable String authorityName) {
        return R.ok(roleService.page(new Page<Role>(current, size), new QueryWrapper<Role>().eq(Role.CHANNEL, channel).eq(Role.AUTHORITY_NAME, authorityName)));
    }

    @GetMapping("{id}")
    public R<Role> searchRoleAndRelationRecords(@PathVariable Long id) {
        return R.ok(roleService.getRoleAndRelationRecords(id));
    }

    private Long[] parseResourceIds(@NonNull Role role){
        val resourceIdsSet = new HashSet<Long>();
        role.getResources().forEach(resource -> {
            val pids = resource.getPids().split(",");
            for (String pid : pids) {
                if (NumberUtils.isCreatable(pid)) {
                    val temp = Long.valueOf(pid);
                    if (!temp.equals(Resource.IS_ROOT_MENU)) {
                        resourceIdsSet.add(temp);
                    }
                }
            }
            resourceIdsSet.add(resource.getId());
        });
        Long[] resourceIds = new Long[resourceIdsSet.size()];
        return resourceIdsSet.toArray(resourceIds);
    }

    /**
     * @param role 其中可能包含{@link Role#getResources()}，因为前端控件的原因，下面将会添加资源对应的父节点到需要关联的记录中
     * @return
     */
    @PostMapping
    public R<Role> create(@RequestBody Role role) {
        roleService.save(role, parseResourceIds(role));
        return success(role);
    }

    @PutMapping
    public R<Role> update(@RequestBody Role role) {
        roleService.update(role, parseResourceIds(role));
        return success(role);
    }

    @DeleteMapping
    public R<Boolean> del(@RequestBody Admin data) {
        val roles = data.getRoles();
        if (roles.size() > 0) {
            val idList = new HashSet<Long>();
            roles.forEach(item -> idList.add(item.getId()));
            return success(roleService.remove(idList));
        } else {
            throw new IllegalArgumentException("未传递要删除的角色记录");
        }
    }

}

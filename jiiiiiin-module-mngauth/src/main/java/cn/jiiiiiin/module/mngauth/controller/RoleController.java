package cn.jiiiiiin.module.mngauth.controller;


import cn.jiiiiiin.module.common.dto.mngauth.RoleDto;
import cn.jiiiiiin.module.common.entity.mngauth.Admin;
import cn.jiiiiiin.module.common.entity.mngauth.Resource;
import cn.jiiiiiin.module.common.entity.mngauth.Role;
import cn.jiiiiiin.module.common.enums.common.ChannelEnum;
import cn.jiiiiiin.module.mngauth.service.IRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.jiiiiiin.module.common.controller.BaseController;

import java.util.ArrayList;
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

    @GetMapping("list/{channel}")
    public R<List<Role>> page(@PathVariable ChannelEnum channel) {
        return R.ok(roleService.list(new QueryWrapper<Role>().eq(Role.CHANNEL, channel)));
    }

    @GetMapping("{channel}/{current}/{size}")
    public R<IPage<Role>> page(@PathVariable ChannelEnum channel, @PathVariable Long current, @PathVariable Long size) {
        return R.ok(roleService.page(new Page<>(current, size), new QueryWrapper<Role>().eq(Role.CHANNEL, channel)));
    }

    @PostMapping("search/{channel}/{current}/{size}")
    public R<IPage<Role>> searchPage(@PathVariable ChannelEnum channel, @PathVariable Long current, @PathVariable Long size, @RequestBody Role role) {
        return R.ok(roleService.page(new Page<>(current, size), new QueryWrapper<Role>().eq(Role.CHANNEL, channel).like(Role.AUTHORITY_NAME, role.getAuthorityName())));
    }

    @GetMapping("eleui/{channel}/{current}/{size}")
    public R<IPage<RoleDto>> eleuiTableList(@PathVariable ChannelEnum channel, @PathVariable Long current, @PathVariable Long size) {
        return R.ok(roleService.pageDto(new Page<>(current, size), channel, null));
    }

    @PostMapping("search/eleui/{channel}/{current}/{size}")
    public R<IPage<RoleDto>> eleuiTableSearch(@PathVariable ChannelEnum channel, @PathVariable Long current, @PathVariable Long size, @RequestBody Role role) {
        return R.ok(roleService.pageDto(new Page<>(current, size), channel, role));
    }

    @GetMapping("{id}")
    public R<RoleDto> getRoleAndRelationRecords(@PathVariable Long id) {
        return R.ok(roleService.getRoleAndRelationRecords(id));
    }

    /**
     * 查询角色记录及其关联的element-ui树形控件选择的资源记录
     *
     * @param id
     * @return
     */
    @GetMapping("eleui/{id}")
    public R<RoleDto> getRoleAndRelationEleUiResourceRecords(@PathVariable Long id) {
        return R.ok(roleService.getRoleAndRelationEleUiResourceRecords(id));
    }

    /**
     * 因为前端`element-ui`树形控件只能将选中的子节点和完整的父节点（其下节点全部选中的情况）的数据传递上来
     * 故这里需要依赖{@link Resource#pids}属性，将管理的父节点收集起来
     * <p>
     * 1.更新role自己的resources列表，存储真正的资源记录
     * 2.返回element-ui用户选择的资源记录ids
     *
     * @param role
     * @return
     */
    private Long[] _parseResourceIds(@NonNull Role role) {
        val resourceIdsSet = new HashSet<Long>();
        val newResList = new ArrayList<Resource>();
        role.getResources().forEach(resource -> {
            val pidsStr = resource.getPids();
            if (StringUtils.isNotEmpty(pidsStr)) {
                val pids = pidsStr.split(",");
                for (val pid : pids) {
                    if (NumberUtils.isCreatable(pid)) {
                        val temp = Long.valueOf(pid);
                        val res = new Resource().setId(temp);
                        // 非根节点 && 非已经存在（防止前端传递重复的记录）
                        if (!temp.equals(Resource.IS_ROOT_MENU) && !newResList.contains(res)) {
                            newResList.add((Resource) res);
                        }
                    }
                }
            }
            resourceIdsSet.add(resource.getId());
        });
        role.getResources().addAll(newResList);
        Long[] resourceIds = new Long[resourceIdsSet.size()];
        return resourceIdsSet.toArray(resourceIds);
    }

    /**
     * @param role 其中可能包含{@link Role#getResources()}，因为前端控件的原因，下面将会添加资源对应的父节点到需要关联的记录中
     * @return
     */
    @PostMapping
    public R<Role> create(@RequestBody Role role) {
        if (Role.isRootRole(role)) {
            throw new IllegalArgumentException("不能创建和系统管理员角色相同角色标识的记录");
        } else {
            roleService.save(role, _parseResourceIds(role));
            return success(role);
        }
    }

    @PutMapping
    public R<Role> update(@RequestBody Role role) {
        if (Role.isRootRole(role)) {
            throw new IllegalArgumentException("系统管理员角色不允许修改");
        } else {
            roleService.update(role, _parseResourceIds(role));
            return success(role);
        }
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

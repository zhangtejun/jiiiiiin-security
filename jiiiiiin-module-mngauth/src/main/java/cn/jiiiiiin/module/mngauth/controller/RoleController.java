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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api
public class RoleController extends BaseController {

    @Autowired
    private IRoleService roleService;

    @ApiOperation(value = "角色记录列表查询", notes = "查询对应渠道所有角色记录", httpMethod = "GET")
    @GetMapping("list/{channel}")
    public R<List<Role>> page(@PathVariable ChannelEnum channel) {
        return R.ok(roleService.list(new QueryWrapper<Role>().eq(Role.CHANNEL, channel)));
    }

    @ApiOperation(value = "角色记录分页查询", notes = "分页查询对应渠道角色记录", httpMethod = "GET")
    @GetMapping("{channel}/{current}/{size}")
    public R<IPage<Role>> page(@PathVariable ChannelEnum channel, @PathVariable Long current, @PathVariable Long size) {
        return R.ok(roleService.page(new Page<>(current, size), new QueryWrapper<Role>().eq(Role.CHANNEL, channel)));
    }

    @ApiOperation(value = "角色记录分页检索", notes = "分页检索对应渠道角色记录", httpMethod = "GET")
    @PostMapping("search/{channel}/{current}/{size}")
    public R<IPage<Role>> searchPage(@PathVariable ChannelEnum channel, @PathVariable Long current, @PathVariable Long size, @RequestBody Role role) {
        return R.ok(roleService.page(new Page<>(current, size), new QueryWrapper<Role>().eq(Role.CHANNEL, channel).like(Role.AUTHORITY_NAME, role.getAuthorityName())));
    }

    @ApiOperation(value = "角色记录（适配eleui）分页查询", notes = "分页查询对应渠道角色记录，适配前端element-ui数据格式要求，提供展开功能所属数据而定义", httpMethod = "GET")
    @GetMapping("eleui/{channel}/{current}/{size}")
    public R<IPage<RoleDto>> eleuiTableList(@PathVariable ChannelEnum channel, @PathVariable Long current, @PathVariable Long size) {
        return R.ok(roleService.pageDto(new Page<>(current, size), channel, null));
    }

    @ApiOperation(value = "角色记录（适配eleui）分页检索", notes = "分页检索对应渠道角色记录，适配前端element-ui数据格式要求，提供展开功能所属数据而定义", httpMethod = "GET")
    @PostMapping("search/eleui/{channel}/{current}/{size}")
    public R<IPage<RoleDto>> eleuiTableSearch(@PathVariable ChannelEnum channel, @PathVariable Long current, @PathVariable Long size, @RequestBody Role role) {
        return R.ok(roleService.pageDto(new Page<>(current, size), channel, role));
    }

    @ApiOperation(value = "角色记录查询", notes = "根据路径参数角色id查询其详细数据", httpMethod = "GET")
    @GetMapping("{id}")
    public R<RoleDto> getRoleAndRelationRecords(@PathVariable Long id) {
        return R.ok(roleService.getRoleAndRelationRecords(id));
    }

    @ApiOperation(value = "角色记录（适配eleui）查询", notes = "根据路径参数角色id查询其详细数据，查询角色记录及其关联的element-ui树形控件选择的资源记录", httpMethod = "GET")
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
    @ApiOperation(value = "新增角色", notes = "添加角色和其管理的资源记录", httpMethod = "POST")
    @PostMapping
    public R<Role> create(@RequestBody Role role) {
        roleService.save(role, _parseResourceIds(role));
        return success(role);
    }

    @ApiOperation(value = "更新角色信息", notes = "更新角色信息和其管理的资源记录", httpMethod = "PUT")
    @PutMapping
    public R<Role> update(@RequestBody Role role) {
        roleService.update(role, _parseResourceIds(role));
        return success(role);
    }

    @ApiOperation(value = "批量删除角色", notes = "批量删除角色角色和其管理的资源记录", httpMethod = "DELETE")
    @DeleteMapping
    public R<Boolean> dels(@RequestBody Admin data) {
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

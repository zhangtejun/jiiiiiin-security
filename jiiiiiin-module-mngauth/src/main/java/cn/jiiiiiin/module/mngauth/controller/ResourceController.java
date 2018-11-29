package cn.jiiiiiin.module.mngauth.controller;


import cn.jiiiiiin.module.common.controller.BaseController;
import cn.jiiiiiin.module.common.entity.mngauth.Resource;
import cn.jiiiiiin.module.common.enums.common.StatusEnum;
import cn.jiiiiiin.module.common.enums.mngauth.ResourceChannelEnum;
import cn.jiiiiiin.module.mngauth.service.IResourceService;
import com.baomidou.mybatisplus.extension.api.R;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限资源表 前端控制器
 * </p>
 *
 * TODO 增加校验器
 *
 * @author jiiiiiin
 * @since 2018-09-27
 */
@Slf4j
@RestController
@RequestMapping("/resource")
public class ResourceController extends BaseController {

    @Autowired
    private IResourceService resourceService;

    /**
     * 获取资源树
     *
     * @return
     */
    @GetMapping(value = "search/{channel}/{status}")
    public R<List<Resource>> searchTree(@PathVariable ResourceChannelEnum channel, @PathVariable StatusEnum status) {
        // 前端需要一个【根节点】才好于进行前端逻辑控制
        val tree = new ArrayList<Resource>();
        tree.add(Resource
                .getRootMenu(channel)
                .setChildren(resourceService.searchTreeAllChildrenNode(Resource.IS_ROOT_MENU, channel, status))
        );
        return success(tree);
    }

    /**
     * 获取资源树
     *
     * @return
     */
    @GetMapping(value = "{channel}")
    public R<List<Resource>> rootTree(@PathVariable ResourceChannelEnum channel) {
        // 前端需要一个【根节点】才好于进行前端逻辑控制
        val tree = new ArrayList<Resource>();
        tree.add(Resource
                .getRootMenu(channel)
                .setChildren(resourceService.treeAllChildrenNode(Resource.IS_ROOT_MENU, channel))
        );
        return success(tree);
    }


    /**
     * 查询对应`id`的节点
     *
     * @return
     */
    @GetMapping(value = "{channel}/{id}")
    public R<List<Resource>> qryTree(@PathVariable ResourceChannelEnum channel, @PathVariable Long id) {
        return success(resourceService.treeAllChildrenNode(id, channel));
    }

    /**
     * 创建资源
     *
     * @param resource
     * @return
     */
    @PostMapping
    public R<Resource> create(@RequestBody Resource resource) {
        resourceService.saveAndSortNum(resource, resource.getChannel());
        return success(resource.setChildren(new ArrayList<>()));
    }

    /**
     * 创建资源
     *
     * @param resource
     * @return
     */
    @PutMapping
    public R<Resource> update(@RequestBody Resource resource) {
        resourceService.updateAndSortNum(resource, resource.getChannel());
        return success(resource);
    }

    /**
     * 删除资源
     *
     * @param channel
     * @param id
     * @return
     */
    @DeleteMapping(value = "{channel}/{id}")
    public R<Boolean> del(@PathVariable ResourceChannelEnum channel, @PathVariable Long id) {
        return success(resourceService.delOnlyIsLeafNode(id, channel));
    }

    /**
     * 解决接口`path variable`转换枚举问题
     * https://www.devglan.com/spring-boot/enums-as-request-parameters-in-spring-boot-rest
     *
     * @param webdataBinder
     */
    @InitBinder
    public void initBinder(final WebDataBinder webdataBinder) {
        webdataBinder.registerCustomEditor(ResourceChannelEnum.class, new ResourceChannelEnum.ResourceChannelEnumConverter());
    }


}

package cn.jiiiiiin.module.mngauth.controller;


import cn.jiiiiiin.module.common.entity.mngauth.Resource;
import cn.jiiiiiin.module.mngauth.service.IResourceService;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import cn.jiiiiiin.module.common.controller.BaseController;

/**
 * <p>
 * 权限资源表 前端控制器
 * </p>
 *
 * @author jiiiiiin
 * @since 2018-09-27
 */
@RestController
@RequestMapping("/resource")
public class ResourceController extends BaseController {

    @Autowired
    private IResourceService resourceService;

    /**
     * 创建资源
     * @param resource
     * @return
     */
    @PostMapping
    public R<Resource> create(@RequestBody Resource resource){
        resource.insert();
        return success(resource);
    }

}

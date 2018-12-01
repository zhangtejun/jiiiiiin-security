package cn.jiiiiiin.module.mngauth.service.impl;

import cn.jiiiiiin.module.common.entity.mngauth.Role;
import cn.jiiiiiin.module.common.enums.common.ChannelEnum;
import cn.jiiiiiin.module.common.mapper.mngauth.RoleMapper;
import cn.jiiiiiin.module.mngauth.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author jiiiiiin
 * @since 2018-09-27
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Boolean remove(Collection<? extends Serializable> idList) {
        roleMapper.deleteBatchIds(idList);
        return null;
    }
}

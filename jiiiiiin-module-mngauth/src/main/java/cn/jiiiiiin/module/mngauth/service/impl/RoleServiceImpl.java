package cn.jiiiiiin.module.mngauth.service.impl;

import cn.jiiiiiin.module.common.entity.mngauth.Resource;
import cn.jiiiiiin.module.common.entity.mngauth.Role;
import cn.jiiiiiin.module.common.mapper.mngauth.RoleMapper;
import cn.jiiiiiin.module.mngauth.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Boolean save(Role role, Long[] resourceIds) {
        role.insert();
        val resList = role.getResources();
        for (Long id : resourceIds) {
            resList.add((Resource) new Resource().setId(id));
        }
        return SqlHelper.retBool(roleMapper.insertRelationResourceRecords(role));
    }

    @Transactional
    @Override
    public Boolean remove(Collection<? extends Serializable> idList) {
        roleMapper.deleteRelationAdminRecords(idList);
        roleMapper.deleteRelationResourceRecords(idList);
        return SqlHelper.delBool(roleMapper.deleteBatchIds(idList));
    }

}

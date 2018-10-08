package cn.jiiiiiin.module.common.mapper.mngauth;

import cn.jiiiiiin.module.common.entity.mngauth.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Set;

/**
 * <p>
 * 权限资源表 Mapper 接口
 * </p>
 *
 * @author jiiiiiin
 * @since 2018-09-27
 */
public interface ResourceMapper extends BaseMapper<Resource> {

    /**
     * 查询role关联的资源集合
     *
     * @param roleId
     * @return
     */
    Set<Resource> selectByRoleId(Long roleId);
}

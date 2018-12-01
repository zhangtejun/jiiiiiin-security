package cn.jiiiiiin.module.mngauth.service;

import cn.jiiiiiin.module.common.entity.mngauth.Role;
import cn.jiiiiiin.module.common.enums.common.ChannelEnum;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author jiiiiiin
 * @since 2018-09-27
 */
public interface IRoleService extends IService<Role> {

    Boolean remove(Collection<? extends Serializable> idList);
}

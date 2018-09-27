package cn.jiiiiiin.module.mngauth.service.impl;

import cn.jiiiiiin.module.common.entity.mngauth.Admin;
import cn.jiiiiiin.module.common.mapper.mngauth.AdminMapper;
import cn.jiiiiiin.module.mngauth.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author jiiiiiin
 * @since 2018-09-27
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

}

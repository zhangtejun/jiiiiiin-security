package cn.jiiiiiin.module.mngauth.service.impl;

import cn.jiiiiiin.module.common.dto.mngauth.AdminDto;
import cn.jiiiiiin.module.common.entity.mngauth.Admin;
import cn.jiiiiiin.module.common.enums.common.ChannelEnum;
import cn.jiiiiiin.module.common.mapper.mngauth.AdminMapper;
import cn.jiiiiiin.module.common.mapper.mngauth.ResourceMapper;
import cn.jiiiiiin.module.mngauth.service.IAdminService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author jiiiiiin
 * @since 2018-09-27
 */
@Service
@Slf4j
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    ResourceMapper resourceMapper;

    @Override
    public Admin signInByUsername(@NonNull String username, ChannelEnum channel) {
        log.debug("登录用户名 {} {}", channel, username);
        val res = adminMapper.selectByUsername(username, channel);
        res.getRoles().forEach(role -> role.setResources(resourceMapper.selectByRoleId(role.getId(), channel)));
        return res;
    }

    @Override
    public boolean saveRelationRoleRecords(Admin admin) {
        adminMapper.deleteRelationRoleAdminRecord(admin.getId());
        return SqlHelper.retBool(adminMapper.insertRelationRoleRecords(admin));
    }

    @Transactional
    @Override
    public Boolean saveAdminAndRelationRecords(Admin admin) {
        admin.setCreateTime(LocalDateTime.now());
        val res = SqlHelper.retBool(adminMapper.insert(admin));
        saveRelationRoleRecords(admin);
        return res;
    }

    @Transactional
    @Override
    public Boolean updateAdminAndRelationRecords(Admin admin) {
        val currentRecord = adminMapper.selectById(admin.getId());
        currentRecord.setUsername(admin.getUsername());
        currentRecord.setPassword(admin.getPassword());
        currentRecord.setPhone(admin.getPhone());
        currentRecord.setEmail(admin.getEmail());
        saveRelationRoleRecords(admin);
        return SqlHelper.retBool(adminMapper.updateById(currentRecord));
    }

    @Transactional
    @Override
    public Boolean removeAdminAndRelationRecords(Long id, ChannelEnum channel) {
        val currentRecord = adminMapper.selectById(id);
        adminMapper.deleteRelationRoleAdminRecord(id);
        return SqlHelper.delBool(adminMapper.deleteById(currentRecord));
    }

    @Override
    public Boolean removeAdminsAndRelationRecords(String idList, ChannelEnum channel) {
        val ids = Arrays.asList(idList.split(","));
        adminMapper.deleteRelationRoleAdminRecords(ids);
        return SqlHelper.delBool(adminMapper.deleteBatchIds(ids));
    }

    @Override
    public IPage<AdminDto> pageAdminDto(Page<AdminDto> page, ChannelEnum channel, AdminDto adminDto) {
        return adminMapper.selectPageAdminDto(page, channel, adminDto);
    }
}

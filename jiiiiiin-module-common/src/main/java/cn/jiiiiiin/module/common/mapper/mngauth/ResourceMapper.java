package cn.jiiiiiin.module.common.mapper.mngauth;

import cn.jiiiiiin.module.common.entity.mngauth.Resource;
import cn.jiiiiiin.module.common.enums.mngauth.ResourceChannelEnum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;
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
    List<Resource> selectByRoleId(@Param("roleId") Long roleId, @Param("channel") ResourceChannelEnum channel);

    /**
     * 获取`pid`对应的下一级子节点
     *
     * @param pid
     * @param channel
     * @return
     */
    List<Resource> selectChildren(@Param("pid") Long pid, @Param("channel") ResourceChannelEnum channel);

    /**
     * 获取`pid`其下的所有子节点
     *
     * @param pid
     * @return
     */
    List<Resource> selectAllChildrenNode(@Param("pid") Long pid, @Param("channel") ResourceChannelEnum channel);

    /**
     * 查询给定资源下还存在多少一级子节点
     *
     * @param pid
     * @return
     */
    Integer selectCountChildren(@Param("pid") Long pid, @Param("channel") ResourceChannelEnum channel);
}

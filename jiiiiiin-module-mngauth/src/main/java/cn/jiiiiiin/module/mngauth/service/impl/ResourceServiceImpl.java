package cn.jiiiiiin.module.mngauth.service.impl;

import cn.jiiiiiin.module.common.entity.mngauth.Resource;
import cn.jiiiiiin.module.common.entity.mngauth.Role;
import cn.jiiiiiin.module.common.enums.common.StatusEnum;
import cn.jiiiiiin.module.common.enums.common.ChannelEnum;
import cn.jiiiiiin.module.common.mapper.mngauth.ResourceMapper;
import cn.jiiiiiin.module.common.mapper.mngauth.RoleMapper;
import cn.jiiiiiin.module.mngauth.service.IResourceService;
import cn.jiiiiiin.module.mngauth.service.IRoleService;
import cn.jiiiiiin.security.rbac.component.dict.RbacDict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.val;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static cn.jiiiiiin.module.common.entity.mngauth.Resource.IS_ROOT_MENU;

/**
 * <p>
 * 权限资源表 服务实现类
 * </p>
 *
 * @author jiiiiiin
 * @since 2018-09-27
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private IRoleService roleService;

    private List<Resource> _parseTreeNode(Long pid, List<Resource> nodes) {
        val menus = new ArrayList<Resource>();
        nodes.forEach(resource -> {
            // 过滤一级节点
            if (resource.getPid().equals(pid)) {
                val node = Resource.parserMenu(resource, nodes);
                menus.add(node);
            }
        });
        menus.sort(Comparator.comparingInt(Resource::getNum));
        return menus;
    }

    @Override
    public List<Resource> treeAllChildrenNode(Long pid, ChannelEnum channel) {
        val nodes = resourceMapper.selectAllChildrenNode(pid, channel, null);
        return _parseTreeNode(pid, nodes);
    }

    @Override
    public List<Resource> searchTreeAllChildrenNode(Long pid, ChannelEnum channel, StatusEnum status) {
        val nodes = resourceMapper.selectAllChildrenNode(pid, channel, status);
        return _parseTreeNode(pid, nodes);
    }

    @Transactional
    @Override
    public Boolean saveAndSortNum(Resource resource, ChannelEnum channel) {
        var res = false;
        val pid = resource.getPid();
        if (pid.equals(IS_ROOT_MENU)) {
            // 添加一级菜单
            resource.setPids(String.valueOf(IS_ROOT_MENU));
        } else {
            val pNode = resourceMapper.selectById(pid);
            // 设置新增资源的`pids`
            resource.setPids(pNode.getPids().concat(",").concat(String.valueOf(pid)));
        }
        final List<Resource> children = resourceMapper.selectChildren(pid, channel);
        val size = children.size();
        val addNodeNum = resource.getNum();
        if (addNodeNum > size || size == 0) {
            res = SqlHelper.retBool(resourceMapper.insert(resource));
        } else {
            final boolean[] update = {false};
            // 需要排序
            children.forEach(item -> {
                val itemNum = item.getNum();
                if (itemNum.equals(addNodeNum)) {
                    update[0] = true;
                }
                if (update[0]) {
                    item.setNum(itemNum + 1);
                }
            });
            children.add(addNodeNum, resource);
            res = this.saveOrUpdateBatch(children);
        }

        if (res) {
            // 新增资源都默认加挂到`ROLE_ADMIN`
            val adminRole = roleService.getOne(new QueryWrapper<Role>().eq(Role.AUTHORITY_NAME, RbacDict.ROLE_ADMIN_AUTHORITY_NAME));
            val resList = new ArrayList<Resource>();
            resList.add(resource);
            adminRole.setResources(resList);
            roleService.insertRelationResourceRecords(adminRole);
        }
        return res;
    }

    @Transactional
    @Override
    public Boolean updateAndSortNum(Resource resource, ChannelEnum channel) {
        val currentNode = resourceMapper.selectById(resource.getId());
        val currentNum = currentNode.getNum();
        val modifyNum = resource.getNum();
        // 设置可更新属性
        currentNode
                .setNum(modifyNum)
                .setName(resource.getName())
                .setStatus(resource.getStatus())
                .setPath(resource.getPath())
                .setIcon(resource.getIcon());
        // 如果没有修改排序
        if (currentNum.equals(modifyNum)) {
            return SqlHelper.retBool(resourceMapper.updateById(currentNode));
        } else {
            final List<Resource> children = resourceMapper.selectChildren(currentNode.getPid(), channel);
            // 需要排序
            val childrenLength = children.size();
            var idx = -1;
            for (int i = 0; i < childrenLength; i++) {
                val item = children.get(i);
                if (!item.getId().equals(resource.getId())) {
                    val itemNum = item.getNum();
                    if (itemNum > currentNum && itemNum <= modifyNum) {
                        // 处理加大当前待修改节点的排序
                        item.setNum(itemNum - 1);
                    } else if (itemNum >= modifyNum && itemNum < currentNum) {
                        // 处理减小当前待修改节点的排序
                        item.setNum(itemNum + 1);
                    }
                } else {
                    idx = i;
                }
            }
            children.remove(idx);
            children.add(idx, currentNode);
            return this.saveOrUpdateBatch(children);
        }
    }

    @Transactional
    @Override
    public Boolean delOnlyIsLeafNode(Long id, ChannelEnum channel) {
        // 查询自身是否还存在子节点
        val numb = resourceMapper.selectCountChildren(id, channel);
        Boolean temp;
        if (numb > 0) {
            throw new IllegalStateException("待删除的资源还存在子节点，请先清空其下的子节点在进行改操作！");
        } else {
            val node = resourceMapper.selectById(id);
            final List<Resource> children = resourceMapper.selectChildren(node.getPid(), channel);
            if (node.getNum() == children.size()) {
                // 删除关联资源
                resourceMapper.deleteRelationRoleRecords(id);
                // 删除最后一个节点
                temp = SqlHelper.delBool(resourceMapper.deleteById(id));
            } else {
                final boolean[] update = {false};
                val size = children.size();
                var idx = -1;
                for (int i = 0; i < size; i++) {
                    val item = children.get(i);
                    if (item.getId().equals(id)) {
                        update[0] = true;
                        idx = i;
                        continue;
                    }
                    if (update[0]) {
                        item.setNum(item.getNum() - 1);
                    }
                }
                if (idx != -1) {
                    children.remove(idx);
                    if (!children.isEmpty()) {
                        this.saveOrUpdateBatch(children);
                    }
                }
                temp = SqlHelper.delBool(resourceMapper.deleteById(id));
            }
        }
        return temp;
    }

}

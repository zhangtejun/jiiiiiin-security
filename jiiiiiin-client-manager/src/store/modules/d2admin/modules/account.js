import util from '@/libs/util.js';
import { AccountLogin } from '@/api/sys/login';
import _ from 'lodash';

/**
 * 删除空`children`节点
 * @param menus
 * @returns {Array}
 * @private
 */
function _delEmptyChildren(menus) {
  const res = [];
  menus.forEach((item, idx) => {
    if (_.isEmpty(item.children)) {
      delete item.children;
    } else {
      // 递归删除
      item.children = _delEmptyChildren(item.children);
    }
    // 删除多余的字段
    delete item.num
    res.push(item);
  });
  return res;
}

const _parseAuthorizePaths = function(resources) {
  const res = {};
  const paths = [];
  const aliasArr = [];
  resources.forEach(resource => {
    const path = resource.path;
    const alias = resource.alias;
    if (!_.isEmpty(path)) {
      paths.push(path);
    }
    if (!_.isEmpty(alias)) {
      aliasArr.push(alias);
    }
  });
  res.paths = paths
  res.alias = aliasArr
  return res;
}

const _parseAuthorizeInterfaces = function(interfaes) {
  const res = [];
  interfaes.forEach(({ url, method } = {}) => {
    if (!_.isEmpty(url) && !_.isEmpty(method)) {
      res.push({
        url, method
      });
    }
  });
  return res;
}

const _parseUserRoleIsSuperAdminStatus = function(roles) {
  let res = false;
  for (let i = 0; i < roles.length; i++) {
    const role = roles[i];
    res = role.authorityName === 'ADMIN';
    if (res) {
      break;
    }
  }
  return res;
}

export default {
  namespaced: true,
  actions: {
    /**
     * @description 登录
     * @param {Object} param context
     * @param {Object} param vm {Object} vue 实例
     * @param {Object} param username {String} 用户账号
     * @param {Object} param password {String} 密码
     * @param {Object} param route {Object} 登录成功后定向的路由对象
     */
    login({ commit, dispatch }, {
      vm,
      username,
      password,
      imageCode,
      route = {
        name: 'index'
      }
    }) {
      // 开始请求登录接口
      AccountLogin(vm.$vp, {
        username,
        password,
        imageCode
      })
        .then(async res => {
          // 设置 cookie 一定要存 uuid
          // 整个系统依赖这两个数据进行校验和存储
          // uuid 是用户身份唯一标识 用户注册的时候确定 并且不可改变 不可重复
          util.cookies.set('uuid', `${res.principal.admin.username}-uuid`);
          // 修改用户登录状态
          vm.$vp.modifyLoginState(true)
          const menus = _delEmptyChildren(res.principal.admin.menus);
          const authorizeResources = _parseAuthorizePaths(res.principal.admin.authorizeResources);
          // console.log('resources', res.principal.admin.authorizeResources)
          // console.log('authorizeResources', authorizeResources)
          vm.$vp.rabcUpdateAuthorizedPaths(authorizeResources.paths)
          vm.$vp.rabcUpdateAuthorizeResourceAlias(authorizeResources.alias)
          const authorizeInterfaces = _parseAuthorizeInterfaces(res.principal.admin.authorizeInterfaces);
          console.log('res.principal.admin.authorizeInterfaces', res.principal.admin.authorizeInterfaces)
          console.log('authorizeInterfaces', authorizeInterfaces)
          vm.$vp.rabcUpdateAuthorizeInterfaces(authorizeInterfaces)
          const isSuperAdminStatus = _parseUserRoleIsSuperAdminStatus(res.principal.admin.roles);
          vm.$vp.rabcUpdateSuperAdminStatus(isSuperAdminStatus)
          // 设置顶栏菜单
          // commit('d2admin/menu/headerSet', menus, { root: true });
          // 设置侧边栏菜单
          commit('d2admin/menu/asideSet', menus, { root: true });
          // 初始化菜单搜索功能
          commit('d2admin/search/init', menus, { root: true });
          vm.$vp.cacheSaveToSessionStore('menus', menus)
          // 设置 vuex 用户信息
          await dispatch('d2admin/user/set', {
            name: res.name
          }, { root: true });
          // 用户登录后从持久化数据加载一系列的设置
          await dispatch('load');
          commit('d2admin/gray/set', false, { root: true });
          vm.$vp.toast('登录成功', {
            type: 'success'
          })
          // 更新路由 尝试去获取 cookie 里保存的需要重定向的页面完整地址
          const path = util.cookies.get('redirect');
          // 根据是否存有重定向页面判断如何重定向
          vm.$router.replace(path ? { path } : route);
          // 删除 cookie 中保存的重定向页面
          util.cookies.remove('redirect');
        })
        .catch(err => {
          console.log('err: ', err);
        });
    },
    /**
     * @description 注销用户并返回登录页面
     * @param {Object} param context
     * @param {Object} param vm {Object} vue 实例
     * @param {Object} param confirm {Boolean} 是否需要确认
     */
    logout({ commit }, { vm, confirm = false }) {
      /**
       * @description 注销
       */
      function logout() {
        // 删除cookie
        util.cookies.remove('uuid');
        // 跳转路由
        this.psPageReplace('/login');
        // 修改用户登录状态
        this.modifyLoginState(false)
      }

      // 判断是否需要确认
      if (confirm) {
        commit('d2admin/gray/set', true, { root: true });
        vm.$confirm('注销当前账户吗?  打开的标签页和用户设置将会被保存。', '确认操作', {
          confirmButtonText: '确定注销',
          cancelButtonText: '放弃',
          type: 'warning'
        })
          .then(() => {
            vm.$vp.ajaxGet('/signOut')
              .finally(() => {
                vm.$vp::logout();
              });
          })
          .catch(() => {
            commit('d2admin/gray/set', false, { root: true });
            vm.$message('放弃注销用户');
          });
      } else {
        if (_.has(vm, '$vp')) {
          vm.$vp::logout();
        } else {
          vm::logout();
        }
      }
    },
    /**
     * @description 用户登录后从持久化数据加载一系列的设置
     * @param {Object} state vuex state
     */
    load({ commit, dispatch }) {
      return new Promise(async resolve => {
        // DB -> store 加载用户名
        await dispatch('d2admin/user/load', null, { root: true });
        // DB -> store 加载主题
        await dispatch('d2admin/theme/load', null, { root: true });
        // DB -> store 加载页面过渡效果设置
        await dispatch('d2admin/transition/load', null, { root: true });
        // DB -> store 持久化数据加载上次退出时的多页列表
        await dispatch('d2admin/page/openedLoad', null, { root: true });
        // DB -> store 持久化数据加载侧边栏折叠状态
        await dispatch('d2admin/menu/asideCollapseLoad', null, { root: true });
        // DB -> store 持久化数据加载全局尺寸
        await dispatch('d2admin/size/load', null, { root: true });
        // end
        resolve();
      });
    }
  }
};

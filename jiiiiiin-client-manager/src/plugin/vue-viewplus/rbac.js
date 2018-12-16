/**
 * RBAC权限控制模块
 */
import _ from 'lodash';

let _installed, _publicPaths, _authorizedPaths, _onLoginStateCheckFail, _authorizeInterfaces;

console.log('_authorizeInterfaces', _authorizeInterfaces)

const _compare = function(rule, path) {
  let temp = false
  if (_.isRegExp(rule)) {
    temp = rule.test(path)
  } else {
    temp = _.isEqual(path, rule)
  }
  return temp
}

/**
 * 检测登录用户是否具有访问对应页面的权限
 * 1.校验是否登录
 * 2.校验带访问的页面是否在`loginStateCheck#authorizedPaths`授权`paths`集合中
 * @param to
 * @param from
 * @param next
 * @private
 */
const _rbacPathCheck = function(to, from, next) {
  console.log('_rbacPathCheck', to.path)
  // 默认认为所有资源都需要进行权限控制
  let isAllow = false
  const path = to.path;
  // 先检测公共页面集合
  const publicPathsLength = _publicPaths.length
  for (let i = publicPathsLength; i--;) {
    const rule = _publicPaths[i];
    isAllow = _compare(rule, path)
    if (isAllow) {
      break;
    }
  }
  // 非公共页面 && 已经登录
  if (!isAllow && this.isLogin()) {
    // 检测已授权页面集合
    const authorizedPathsLength = _authorizedPaths.length;
    for (let i = authorizedPathsLength; i--;) {
      const rule = _authorizedPaths[i];
      isAllow = _compare(rule, path);
      if (isAllow) {
        break;
      }
    }
  }

  if (isAllow) {
    next();
  } else {
    if (_.isFunction(_onLoginStateCheckFail)) {
      console.error(`[v+] RBAC模块检测：用户无权访问【${path}】，回调onLoginStateCheckFail钩子`);
      this::_onLoginStateCheckFail(to, from, next);
    } else {
      next(new Error('check_authorize_paths_fail'));
    }
  }
};

const rbacModel = {
  /**
   * 向`LoginStateCheck#authorizedPaths`中添加授权路径集合
   * 如：登录完成之后，将用户被授权可以访问的页面`paths`添加到`LoginStateCheck#authorizedPaths`中
   * @param paths
   */
  rabcAddAuthorizedPaths(paths) {
    _authorizedPaths = _.concat(_authorizedPaths, paths);
  },
  /**
   * 更新`LoginStateCheck#authorizedPaths`授权路径集合
   * @param paths
   */
  rabcUpdateAuthorizedPaths(paths) {
    _authorizedPaths = paths;
  },
  /**
   * 更新`LoginStateCheck#authorizeInterfaces`授权接口集合
   * @param interfaces
   */
  rabcUpdateAuthorizeInterfaces(interfaces) {
    _authorizeInterfaces = interfaces;
  },
  install(Vue, {
    router = null,
    installed = null,
    /**
     * 用户拥有访问权限的路由path路径集合
     * {Array<Object>}
     * <p>
     * 数组中的item，必须要是一个**正则表达式字面量**，如`[/^((\/Interbus)(?!\/SubMenu)\/.+)$/]`
     * <p>
     * 匹配规则：如果在`LoginStateCheck#authorizedPaths`**需要身份认证规则集**中，那么就需要查看用户是否登录，如果没有登录就拒绝访问
     */
    authorizedPaths = [],
    publicPaths = [],
    authorizeInterfaces = [],
    /**
     * [*] `$vp#onLoginStateCheckFail(to, from, next)`
     * <p>
     * 身份认证检查失败时被回调
     */
    onLoginStateCheckFail = null
  } = {}) {
    _installed = installed
    _publicPaths = publicPaths;
    _authorizedPaths = authorizedPaths;
    _onLoginStateCheckFail = onLoginStateCheckFail;
    _authorizeInterfaces = authorizeInterfaces;
    router.beforeEach((to, from, next) => {
      this::_rbacPathCheck(to, from, next);
    });
  },
  installed() {
    if (_.isFunction(_installed)) {
      this::_installed()
    }
  }
};

export default rbacModel;

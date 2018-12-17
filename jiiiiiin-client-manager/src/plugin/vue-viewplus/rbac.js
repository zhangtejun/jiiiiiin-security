/**
 * RBAC权限控制模块
 */
import _ from 'lodash';

let _debug, _errorHandler, _installed, _onLoginStateCheckFail, _modifyLoginState
let _publicPaths = []
let _authorizedPaths = []
let _authorizeInterfaces = []

/**
 * 是否是【超级管理员】
 * 如果登录用户是这个`角色`，那么就无需进行各种授权控制检测
 * @type {boolean}
 * @private
 */
let _superAdminStatus = false

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
  console.log('_rbacPathCheck', to.path, _authorizedPaths)
  if (_superAdminStatus) {
    next();
    return;
  }
  try {
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
    console.log('this.isLogin()', this.isLogin(), this)
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
        if (_debug) {
          console.error(`[v+] RBAC模块检测：用户无权访问【${path}】，回调onLoginStateCheckFail钩子`);
        }
        this::_onLoginStateCheckFail(to, from, next);
      } else {
        next(new Error('check_authorize_paths_fail'));
      }
    }
  } catch (e) {
    if (_debug) {
      console.error(`[v+] RBAC模块检测出错: ${e.message}`);
    }
    if (_.isFunction(_errorHandler)) {
      this::_errorHandler(e)
    }
  }
};

const _restoreState = function() {
  _publicPaths = this.cacheLoadFromSessionStore('PUBLIC_PATHS', [])
  _authorizedPaths = this.cacheLoadFromSessionStore('AUTHORIZED_PATHS', [])
  _authorizeInterfaces = this.cacheLoadFromSessionStore('AUTHORIZED_INTERFACES', [])
  _superAdminStatus = this.cacheLoadFromSessionStore('AUTHORIZED_SUPER_ADMIN_STATUS', false)
}

/**
 * 校验给定接口列表是否包含于用户授权接口集合中，如果是则返回`true`标识权限校验通过
 * @param urls
 * @returns {boolean}
 * @private
 */
const _checkPermissionByUrl = function(urls) {
  let voter = []
  urls.forEach(url => {
    voter.push(_authorizeInterfaces.includes(url))
  })
  return !voter.includes(false)
}

const _checkPermissionByAlias = function(urls) {
}

/**
 * v-access:url.[disable]="['admin', 'admin/*']"
 * value: ['admin', 'admin/*']
 * arg: url
 * @param Vue
 * @private
 */
const _createRBACDirective = function(Vue) {
  Vue.directive('access', {
    bind: function(el, { value, arg, modifiers }) {
      // console.log(value, arg, modifiers)
      let isAllow = false
      switch (arg) {
        case 'url':
          isAllow = _checkPermissionByUrl(value)
          break
        default:
          isAllow = _checkPermissionByAlias(value)
      }

      if (!isAllow) {
        if (_debug) {
          console.error(`[v+] RBAC access权限检测不通过：用户无权访问【${value}】`);
        }
        if (_.has(modifiers, 'disable')) {
          el.disabled = true;
          el.style.opacity = '0.5'
        } else {
          el.style.display = 'none';
        }
      }
    }
  })
}

const rbacModel = {
  /**
   * 代理`$vp#login-state-check`模块的同名方法，以实现在登出、会话超时踢出的时候清理本模块维护的登录之后设置的状态
   * @param status
   */
  modifyLoginState(status = false) {
    this::_modifyLoginState(status)
    if (!status) {
      this::rbacModel.rabcUpdateSuperAdminStatus(false)
      this::rbacModel.rabcUpdateAuthorizedPaths([])
      this::rbacModel.rabcUpdateAuthorizeInterfaces([])
      this::rbacModel.rabcUpdatePublicPaths([])
    }
  },
  rabcUpdateSuperAdminStatus(status) {
    _superAdminStatus = status;
    this.cacheSaveToSessionStore('AUTHORIZED_SUPER_ADMIN_STATUS', _superAdminStatus)
  },
  /**
   * 添加授权路径集合
   * 如：登录完成之后，将用户被授权可以访问的页面`paths`添加到`LoginStateCheck#authorizedPaths`中
   * @param paths
   */
  rabcAddAuthorizedPaths(paths) {
    this::rbacModel.rabcUpdateAuthorizedPaths(_.concat(_authorizedPaths, paths))
  },
  /**
   * 更新授权路径集合
   * @param paths
   */
  rabcUpdateAuthorizedPaths(paths) {
    _authorizedPaths = [...new Set(paths)]
    this.cacheSaveToSessionStore('AUTHORIZED_PATHS', _authorizedPaths)
  },
  /**
   * 更新授权接口集合
   * @param interfaces
   */
  rabcUpdateAuthorizeInterfaces(interfaces) {
    _authorizeInterfaces = [...new Set(interfaces)]
    this.cacheSaveToSessionStore('AUTHORIZED_INTERFACES', _authorizeInterfaces)
  },
  /**
   * 添加授权接口集合
   * @param paths
   */
  rabcAddAuthorizeInterfaces(paths) {
    this::rbacModel.rabcUpdateAuthorizeInterfaces(_.concat(_authorizeInterfaces, paths))
  },
  /**
   * 更新公共路径集合
   * @param paths
   */
  rabcUpdatePublicPaths(paths) {
    _publicPaths = [...new Set(paths)];
    this.cacheSaveToSessionStore('PUBLIC_PATHS', _publicPaths)
  },
  /**
   * 添加公共路径集合
   * @param paths
   */
  rabcAddPublicPaths(paths) {
    this::rbacModel.rabcUpdatePublicPaths(_.concat(_publicPaths, paths))
  },
  install(Vue, {
    debug = false,
    errorHandler = null,
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
    _debug = debug
    _errorHandler = errorHandler
    _installed = installed
    _modifyLoginState = this.modifyLoginState
    // 恢复模块状态
    this:: _restoreState()
    this::rbacModel.rabcAddPublicPaths(publicPaths)
    this::rbacModel.rabcAddAuthorizedPaths(authorizedPaths)
    this::rbacModel.rabcAddAuthorizeInterfaces(authorizeInterfaces)
    _onLoginStateCheckFail = onLoginStateCheckFail;
    router.beforeEach((to, from, next) => {
      this::_rbacPathCheck(to, from, next);
    });
    this::_createRBACDirective(Vue)
  },
  installed() {
    if (_.isFunction(_installed)) {
      this::_installed()
    }
  }
};

export default rbacModel;

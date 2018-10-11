import router from '@/router/index'

const _env = process.env.NODE_ENV
export const baseUrl = `http://192.168.3.43:9000`
// export const baseUrl = `http://192.168.1.116:9000`
export const serverUrl = `${baseUrl}/mng`

export const mixinConfig = {
  install() {},
  baseUrl,
  serverUrl
}

export default {
  router,
  env: 'BROWSER',
  debug: _env !== 'production',
  appUrl: baseUrl,
  errorHandler(err) {
    console.error('vp errorHandler', err)
    if (err && err instanceof Error) {
      switch (err.code) {
        // case 'RUN_EVN_NOT_SUPPORT':
        //   store.commit(JS_BRIDGE_CAN_USE_STATUS, false)
        //   break
        default:
          alert(err.message)
      }
    }
  },
  loginStateCheck: {
    isLogined: false,
    checkPaths: [
      /^(\/admin.+)$/
    ],
    onLoginStateCheckFaild(to, from, next) {
      alert('您尚未登录，请先登录', to, from)
    }
  },
  utilHttp: {
    baseURL: serverUrl,
    timeout: '100000',
    mode: 'POST',
    headers: {
      Accept: 'application/json'
    },
    // 适配后端`com.baomidou.mybatisplus.extension.api.R`接口
    statusCodeKey: 'code',
    statusCode: '0',
    msgKey: 'msg',
    defShowLoading: true,
    onSendAjaxRespHandle: (response) => {
      return response
    },
    onReqErrPaserMsg: (response, errMsg) => {
      return `${errMsg} [服务端]`
    },
    noNeedDialogHandlerErr: [],
    errDialog(content = '错误消息未定义', {
      action,
      title = '错误提示',
      hideOnBlur = false
    } = {}) {
      alert(`${title} ${content}`)
      return this
    },
    loading(_showLoading) {
      if (_showLoading) {
        console.log('need loading')
      }
    },
    hideLoading() {
      console.log('need un loading')
    },
    accessRules: {
      sessionTimeOut: ['role.invalid_user'],
      onSessionTimeOut(response) {
        // TODO 待测试
        alert('example on onSessionTimeOut call', response)
      },
      unauthorized: ['core_error_unauthorized'],
      onUnauthorized(response) {
        // TODO 待测试
        alert('example on onUnauthorized call', response)
      }
    }
  }
}

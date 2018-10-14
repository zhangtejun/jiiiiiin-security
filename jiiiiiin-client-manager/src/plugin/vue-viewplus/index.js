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
          this.uiDialog(err instanceof Error ? err.message : JSON.stringify(err), {
            title: '捕获到全局错误'
          })
      }
    }
  },
  loginStateCheck: {
    isLogined: false,
    checkPaths: [
      /^(\/admin.+)$/
    ],
    onLoginStateCheckFaild(to, from, next) {
      this.uiToast('您尚未登录，请先登录', {
        type: 'warning'
      })
      next('/login')
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
    msgKey: 'msg',
    dataKey: 'data',
    errInfoOutDataObj: true,
    defShowLoading: true,
    onParseServerResp(response) {
      return response.data.code !== 0
    },
    onSendAjaxRespHandle(response) {
      return response
    },
    onReqErrPaserMsg(response, errMsg) {
      return `${errMsg} [服务端]`
    },
    noNeedDialogHandlerErr: [],
    errDialog(content = '错误消息未定义', {
      title = '错误提示',
      hideOnBlur = false
    } = {}) {
      this.uiDialog(content, {
        title,
        hideOnBlur
      })
      return this
    },
    loading(_showLoading) {
      if (_showLoading) {
        this.uiLoading()
      }
    },
    hideLoading() {
      this.uiHideLoading()
    },
    accessRules: {
      sessionTimeOut: ['role.invalid_user'],
      onSessionTimeOut(response) {
        this.uiToast('会话超时，请重新登录', {
          type: 'warning'
        })
        this.psPageReplace('/login')
      },
      unauthorized: ['core_error_unauthorized'],
      onUnauthorized(response) {
        console.error(`onUnauthorized ${response}`)
        this.uiToast('您尚未登录，请先登录', {
          type: 'error'
        })
      }
    }
  }
}

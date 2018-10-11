import router from '@/router/index'

const _env = process.env.NODE_ENV
export const baseUrl = `http://192.168.1.116:9000`
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
      /^((\/Interbus)(?!\/(SubMenu|ExchangeRateQry))\/.+)|(\/AccountManagement.+)|(\/Loan.+)|(\/QrPay.+)|(\/InvestmentFinance(?!\/(FinanceCalc|FinanceSubMenu|MyFinance|ProductDetail|Fund\/FundProductDetail|Fund\/FundTradingRules|Fund\/FundArchives|Fund\/FundAllocation|Fund\/FundAllocationzc|Fund\/FundManager|Fund\/NetWorthDetail))\/.+)|(\/TransferMoney.+)|(\/CustomerAgent.+)|(\/CreditCard.+)|(\/PersonalCenter(?!\/(OnlineRegisterPre|OnlineRegisterConf|OnlineRegisterRes|OnlineRegisterForEbankUserPre|OnlineRegisterForEbankUserConf|OnlineRegisterForEbankUserRes|VersionHome|VersionDescription|EntryInformation|ForgetPasswordPre|ForgetPasswordRes|AnswerForQuestion|QAHelp))\/.+)|(\/Others(?!\/(AnnouncementList|AnnouncementDetails|HomeSubMenu|LiveSubMenu|SMSBankIOS|SMSBankAndroid|WonderfulLife|AccountInsurance|FundChange|ActivityList|EInDevelopmentPre|RInDevelopmentPre|ExcitingActivities|ExcitingActivityDet|Download))\/.+)$/
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
    statusCodeKey: 'STATUS_CODE_KEY',
    statusCode: 'STATUS_CODE',
    msgKey: 'MSG_KEY',
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

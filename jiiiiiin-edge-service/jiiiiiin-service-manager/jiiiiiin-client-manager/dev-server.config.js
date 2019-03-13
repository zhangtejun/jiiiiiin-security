module.exports = {
  proxy: {
    '/mng': {
      target: 'http://jiiiiiin-server-manager:9090',
      pathRewrite: {
        '^/mng': '/mng'
      },
      changeOrigin: true
    }
  },
  open: false,
  quiet: false,
  disableHostCheck: true
}

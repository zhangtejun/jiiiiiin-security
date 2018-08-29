var baseConfig = {
    baseURL: 'http://localhost:8080',
}

var instance = axios.create({
    baseURL: baseConfig.baseURL,
    method: 'post',
    timeout: 30000,
    headers: {
        // 'Content-Type': 'application/x-www-form-urlencoded',
        'Content-Type': 'application/json',
        Accept: 'application/json, text/plain, */*',
    },
    params: {
    },
    // paramsSerializer: function (params) {
    //     return Qs.stringify(params, {arrayFormat: 'brackets'})
    // }
})

Vue.prototype['$vp'] = instance
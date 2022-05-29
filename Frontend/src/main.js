import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './plugins/element.js'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import store from './store'

import './assets/css/global.css'
// 设置反向代理，前端请求默认发送到 http://localhost:8085/api
let axios = require('axios')
// axios.defaults.baseURL = 'http://47.100.197.182:8085/api'
axios.defaults.baseURL = 'http://localhost:8085/api'
// 前端请求带上cookie信息
axios.defaults.withCredentials = true
// 全局注册，之后可在其他组件中通过 this.$axios 发送数据
Vue.prototype.$axios = axios
Vue.config.productionTip = false
Vue.use(mavonEditor)

// 路由守卫
router.beforeEach((to,from,next) =>{
  const isLogin = !!localStorage.user ;
  let user = window.localStorage.getItem('user') ==null?'':JSON.parse(window.localStorage.getItem('user'||'[]'))

  // 公开页面
  if(to.path ==="/login" || to.path ==="/reset" || to.path ==="/admin"){
    next();
  }
  // 未登录不得访问
  else if(!isLogin){
    next("/login")
  }
  // 只有管理员账户可以访问
  else if(to.path.startsWith('/adminindex')&&user.role.id!==1){
    next("/admin")
  }
  else{
    next()
  }
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

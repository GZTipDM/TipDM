// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import 'babel-polyfill'
import Vue from 'vue'
import App from './App'
import store from './store'
import router from './router'
import ElementUI from 'element-ui'
import './assets/theme-chalk/index.css'
import Echarts from 'echarts'
import Jquery from 'jquery'
import VueClipboards from 'vue-clipboards'
import VueDND from 'awe-dnd'

Vue.use(ElementUI);
Vue.use(Echarts);
Vue.use(VueClipboards);
Vue.use(VueDND);
Vue.prototype.$echarts = Echarts;
Vue.prototype.$ = Jquery;
Vue.config.productionTip = false;

/* eslint-disable no-new */
new Vue({
  el: '#app',
  store,
  router,
  components: {App},
  template: '<App/>'
})

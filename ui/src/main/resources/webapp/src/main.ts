import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import Axios from './lib/axios';
import Date from './lib/date';
import Utils from './lib/utils';
import Iconfont from './lib/iconfont';
import Particles from 'vue-particles';
import AntDesign from 'ant-design-vue';
import './style/index.less';

Vue.use(Axios);
Vue.use(Date);
Vue.use(Utils);
Vue.use(Iconfont);
Vue.use(Particles);
Vue.use(AntDesign);

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');

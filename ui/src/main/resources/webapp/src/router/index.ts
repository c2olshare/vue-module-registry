import Vue from 'vue';
import VueRouter from 'vue-router';
import routes from './routes';
import store, { State } from '@/store/index';
import utils from '@/lib/utils/utils';
import { AUTH_HEADER } from '@/lib/global/constant';

Vue.use(VueRouter);

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

router.beforeEach((to, from, next) => {
  const accessToken = (store.state as State).accessToken;
  if (accessToken) {
    if (to.name === 'login') {
      next({ path: '/' });
    } else {
      next();
    }
  } else {
    if (to.name === 'login') {
      next();
    } else {
      next({ name: 'login' });
    }
  }
});

// 刷新页面设置token
const accessToken = utils.getSessionStorageItem(AUTH_HEADER);
if (accessToken) {
  store.commit('setAccessToken', accessToken);
}

export default router;

import axios, { AxiosInstance } from 'axios';
import router from '@/router';
import { AUTH_HEADER, TOKEN_HEADER } from '@/lib/global/constant';
import utils from '@/lib/utils/utils';
import store from '@/store';

const formHeader = {
  'Content-Type': 'application/x-www-form-urlencoded',
  'X-Requested-With': 'XMLHttpRequest'
};

const jsonHeader = {
  'Content-Type': 'application/json; charset=UTF-8',
  'X-Requested-With': 'XMLHttpRequest'
};

const instance: AxiosInstance = axios.create({
  // 超时时间5分钟
  timeout: 5 * 60 * 1000,
  headers: jsonHeader
});

instance.interceptors.request.use(
  config => {
    // 添加全局Token
    const accessToken = utils.getSessionStorageItem(AUTH_HEADER);
    if (accessToken) {
      config.headers[AUTH_HEADER] = `Bearer ${accessToken}`;
    }
    // Get请求添加时间戳
    if (config.method === 'get') {
      config.params = {
        ...config.params,
        _timestamp: new Date().getTime()
      };
    }
    return config;
  },
  err => {
    return Promise.reject(err);
  }
);

instance.interceptors.response.use(
  response => {
    const token = response.headers[TOKEN_HEADER];
    if (token) {
      store.commit('setAccessToken', token);
    }
    return response;
  },
  error => {
    if (error.response) {
      const token = error.response.headers[TOKEN_HEADER];
      if (token) {
        store.commit('setAccessToken', token);
      }

      if (error.response.status === 401) {
        store.commit('removeAccessToken');
        const currentRoute = router.currentRoute;
        if (currentRoute.path !== '/login') {
          router.replace({
            path: '/login'
          });
        }
      }
    }
    return Promise.reject(error);
  }
);

export { jsonHeader, formHeader };

export default instance;

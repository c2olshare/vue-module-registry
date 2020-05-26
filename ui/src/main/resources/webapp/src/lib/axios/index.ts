import _Vue, { PluginObject } from 'vue';
import instance from './axios';

const plugin: PluginObject<any> = {
  install(Vue: typeof _Vue) {
    Vue.prototype.$axios = instance;
  }
};

export default plugin;

import _Vue, { PluginObject } from 'vue';
import UtilsInstance from './utils';

const plugin: PluginObject<any> = {
  install(Vue: typeof _Vue) {
    Vue.prototype.$utils = UtilsInstance;
  }
};

export default plugin;

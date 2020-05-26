import _Vue, { PluginObject } from 'vue';
import IconfontInstance from './icon';

const plugin: PluginObject<any> = {
  install(Vue: typeof _Vue) {
    Vue.prototype.$iconfont = new IconfontInstance();
  }
};

export default plugin;

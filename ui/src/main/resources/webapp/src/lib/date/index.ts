import _Vue, { PluginObject } from 'vue';
import DateObjectInstance from './date';

const plugin: PluginObject<any> = {
  install(Vue: typeof _Vue) {
    const date = new DateObjectInstance();
    Vue.prototype.$date = date;
    Vue.filter('timestamp', date.format);
  }
};

export default plugin;

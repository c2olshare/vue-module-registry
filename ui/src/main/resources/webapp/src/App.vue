<template>
  <div id="app">
    <a-config-provider :locale="zhCn">
      <router-view />
    </a-config-provider>
  </div>
</template>
<script>
import zhCn from 'ant-design-vue/lib/locale-provider/zh_CN';
import moment from 'moment';
import 'moment/locale/zh-cn';

moment.locale('zh-cn');
export default {
  data() {
    return {
      zhCn
    };
  }
};
</script>
<style lang="less">
html,
body,
#app {
  height: 100%;
  width: 100%;
}

::-webkit-scrollbar {
  width: 6px;
  height: 8px;
  background-color: #f5f5f5;
}

::-webkit-scrollbar-track {
  -webkit-box-shadow: inset 0 0 6px rgba(192, 192, 192, 0.9);
  border-radius: 10px;
  background-color: #f5f5f5;
}

::-webkit-scrollbar-thumb {
  border-radius: 10px;
  -webkit-box-shadow: inset 0 0 6px rgba(210, 210, 210, 0.9);
  background-color: rgba(190, 190, 190, 0.9);
}
</style>

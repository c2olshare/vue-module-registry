<template>
  <div class="vmr-message-box">
    <img class="image" src="../assets/404.svg" alt="404" />
    <p class="status">404</p>
    <p class="message">访问的页面不存在, {{ counter }}秒后自动回到首页</p>
    <p class="action"><a-button type="primary" @click="handleGoHomeClick">回到首页</a-button></p>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

@Component({})
export default class NotFound extends Vue {
  counter = 10;
  interval = -1;

  handleGoHomeClick() {
    this.$router.replace({
      path: '/'
    });
  }

  beforeDestroy() {
    clearInterval(this.interval);
  }

  mounted() {
    this.interval = setInterval(() => {
      if (this.counter-- <= 1) {
        clearInterval(this.interval);
        this.handleGoHomeClick();
      }
    }, 1000);
  }
}
</script>

<style lang="less" scoped>
.vmr-message-box {
  height: 100%;
  text-align: center;
  padding-top: 150px;

  .image {
    width: 360px;
  }

  .status {
    padding: 0;
    margin: 15px 0 0 0;
    font-size: 16px;
    font-weight: bold;
  }

  .message {
    padding: 0;
    margin: 10px 0 0 0;
    font-size: 14px;
  }

  .action {
    padding: 0;
    margin: 25px 0 0 0;
  }
}
</style>

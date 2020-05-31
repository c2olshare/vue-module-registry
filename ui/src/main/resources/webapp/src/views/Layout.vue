<template>
  <div class="vmr-layout" ref="vmr-layout">
    <div class="vmr-layout-header">
      <div class="vmr-header-logo" @click="handleClickLogo">
        <img src="../assets/logo.png" alt="logo" />
      </div>
      <div class="vmr-header-title">VMR</div>
      <div class="vmr-header-place"></div>
      <a-dropdown
        class="vmr-header-trigger"
        placement="bottomRight"
        :style="{ width: '100px' }"
        :overlayStyle="{ width: '100px', minWidth: '100px' }"
      >
        <div>
          <a-icon type="user" :style="{ fontSize: '16px' }" />
          <span :style="{ marginLeft: '5px' }">Admin</span>
        </div>
        <a-menu slot="overlay" @click="handleDropdownClick">
          <a-menu-item key="exit">
            <a-icon type="export" />
            <span>退出</span>
          </a-menu-item>
        </a-menu>
      </a-dropdown>
    </div>
    <div class="vmr-layout-content">
      <router-view></router-view>
    </div>
    <div class="vmr-layout-footer">
      <p class="vmr-footer-power">Powered by <a href="https://c2olshare.com" target="_blank">C2olShare</a> / 0.2.0</p>
    </div>
  </div>
</template>
<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Mutation } from 'vuex-class';

@Component({})
export default class Layout extends Vue {
  @Mutation('removeAccessToken') removeAccessToken!: Function;

  handleClickLogo() {
    if (this.$route.path !== '/') {
      this.$router.push({ path: '/' });
    } else {
      location.reload();
    }
  }

  handleDropdownClick({ key = '' }) {
    if (key === 'exit') {
      this.$message.success('退出成功');
      setTimeout(() => {
        this.removeAccessToken();
        this.$router.replace({
          name: 'login'
        });
      }, 500);
    }
  }
}
</script>
<style scoped lang="less">
@import '../style/index';

.vmr-layout {
  height: 100%;
  width: 100%;

  .vmr-layout-header {
    display: flex;
    height: 60px;
    padding: 0 60px;
    line-height: 60px;
    background: #001529;
    color: #cfcfcf;

    .vmr-header-logo {
      height: 100%;
      width: 36px;
      cursor: pointer;
      display: flex;
      justify-content: center;
      align-items: center;

      img {
        width: 100%;
      }
    }

    .vmr-header-title {
      margin-left: 15px;
      font-size: 22px;
    }

    .vmr-header-place {
      flex: 1;
    }

    .vmr-header-trigger {
      display: flex;
      justify-content: center;
      align-items: center;
      cursor: pointer;
      padding: 0 15px;
      min-width: 80px;
      transition: all 0.2s ease-in-out;

      &:hover {
        color: #fff;
      }
    }
  }
  .vmr-layout-content {
    margin: 10px 0;
    padding: 5px 60px;
    height: calc(~'100% - 146px');
  }
  .vmr-layout-footer {
    height: 66px;
    padding: 0 60px;
    background-color: #f9f9f9;

    .vmr-footer-power {
      height: 66px;
      line-height: 66px;
      text-align: center;
      margin: 0;
    }
  }
}
</style>

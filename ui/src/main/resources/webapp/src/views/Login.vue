<template>
  <div class="vmr-login">
    <div class="vmr-login-content">
      <div class="vmr-login-wrapper">
        <a-card class="vmr-login-content-box" :hoverable="true" :bordered="false">
          <div class="vmr-login-content-header">
            <h1>Vue Module Registry</h1>
          </div>
          <a-form-model ref="passwordForm" :model="passwordForm" :rules="passwordRules" @submit="handlePasswordLogin">
            <a-form-model-item prop="username">
              <a-input v-model="passwordForm.username" placeholder="用户名">
                <a-icon slot="prefix" type="user" style="color: rgba(0,0,0,.25)" />
              </a-input>
            </a-form-model-item>
            <a-form-model-item prop="password">
              <a-input-password v-model="passwordForm.password" placeholder="密码" :visibilityToggle="true">
                <a-icon slot="prefix" type="lock" style="color: rgba(0,0,0,.25)" />
              </a-input-password>
            </a-form-model-item>
            <a-button type="primary" html-type="submit" :loading="loginLoading" :block="true">
              登录
            </a-button>
          </a-form-model>
        </a-card>
      </div>
    </div>
    <div class="vmr-login-footer">
      <p>Powered by <a href="https://c2olshare.com" target="_blank">C2olShare</a> / 0.1.0</p>
    </div>
    <vue-particles class="vmr-login-background"></vue-particles>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Mutation } from 'vuex-class';
import { formHeader } from '@/lib/axios/axios';
import qs from 'qs';

@Component
export default class Login extends Vue {
  $refs!: { passwordForm: HTMLFormElement; mobileForm: HTMLFormElement };
  passwordForm = {
    username: '',
    password: ''
  };
  passwordRules = {
    username: [{ required: true, message: '请输入用户名!' }],
    password: [{ required: true, message: '请输入密码!' }]
  };
  loginLoading = false;

  @Mutation('setAccessToken') setAccessToken!: Function;

  /**
   * 使用账号密码登录
   */
  handlePasswordLogin(e: any) {
    e.preventDefault();
    this.$refs.passwordForm.validate((validate: boolean) => {
      if (validate) {
        this.loginLoading = true;
        this.$axios
          .post('vmr/auth/login', qs.stringify(this.passwordForm), { headers: formHeader })
          .then(({ data = {} }) => {
            this.$message.success('登录成功');
            setTimeout(() => {
              this.handleLoginSuccess(data.token);
            }, 500);
          })
          .catch((error: any) => {
            this.handleLoginFailed(error);
          })
          .finally(() => {
            this.loginLoading = false;
          });
      }
    });
  }

  handleLoginSuccess(accessToken: string) {
    this.setAccessToken(accessToken);
    this.$router.replace({
      path: '/'
    });
  }

  handleLoginFailed(error: any) {
    const message = this.$utils.obtainMessage(error, '登录失败');
    this.$message.error(message);
  }
}
</script>

<style lang="less" scoped>
@import '../style/color';
.vmr-login {
  height: 100%;
  width: 100%;

  /*禁止复制文本*/
  -moz-user-select: none; /*火狐*/
  -webkit-user-select: none; /*webkit浏览器*/
  -ms-user-select: none; /*IE10*/
  user-select: none;

  .vmr-login-content {
    position: absolute;
    top: 20%;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;

    .vmr-login-wrapper {
      .vmr-login-content-box {
        width: 380px;
        background-color: rgba(255, 255, 255, 0.1);
        padding-bottom: 20px;

        &:hover {
          background-color: rgba(255, 255, 255, 0.6);
        }

        .vmr-login-content-header {
          display: flex;
          justify-content: center;
          align-items: center;
          margin-bottom: 26px;

          h1 {
            color: @heading-color;
            font-weight: 500;
            font-size: 26px;
            margin: 0;
            padding: 0;
          }
        }

        .vmr-login-password-forgot {
          float: right;
        }
      }
    }
  }

  .vmr-login-footer {
    position: absolute;
    bottom: 0;
    height: 64px;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .vmr-login-background {
    width: 100%;
    height: 100%;
  }
}
</style>
<style lang="less">
.vmr-login {
  .ant-tabs-bar {
    border: none;
  }

  .ant-tabs-nav-scroll {
    text-align: center;
  }
  .ant-card-hoverable {
    cursor: auto;
  }
}
</style>

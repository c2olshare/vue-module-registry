<template>
  <div class="vmr-home">
    <div class="vmr-home-header">
      <div class="vmr-home-title">
        <a-icon type="appstore" :style="{ fontSize: '20px' }" />
        <span>{{ title }}</span>
      </div>
      <div class="vmr-home-namespace">
        <a-tooltip class="vmr-namespace-info" placement="left">
          <template slot="title">
            <span>命名空间</span>
          </template>
          <a-icon type="info-circle" />
        </a-tooltip>
        <a-select class="vmr-namespace-select" v-model="namespaceCode" @change="handleNamespaceChange">
          <a-select-option v-for="item in namespaceList" :key="item.id" :value="item.code">
            <span>{{ item.name }}</span>
            <span>[{{ item.code }}]</span>
          </a-select-option>
        </a-select>
        <a-button class="vmr-namespace-button" type="primary" icon="plus" @click="handleShowAddNamespace">
          新增
        </a-button>
        <a-button
          class="vmr-namespace-button"
          type="danger"
          icon="delete"
          :disabled="currentNamespace ? currentNamespace.preset : false"
          @click="handleShowDeleteNamespace"
        >
          删除
        </a-button>
      </div>
    </div>
    <div class="vmr-home-view" ref="vmr-home-view">
      <router-view />
    </div>

    <!--新增命名空间-->
    <a-modal
      title="新增命名空间"
      :visible="addNamespaceModal"
      :confirm-loading="addNamespaceLoading"
      okText="确认"
      cancelText="取消"
      @ok="handleAddNamespaceOk"
      @cancel="handleAddNamespaceCancel"
    >
      <a-form-model
        ref="addNamespaceForm"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 18 }"
        :model="addNamespaceForm"
        :rules="addNamespaceFormRules"
      >
        <a-form-model-item label="名称" prop="name">
          <a-input v-model="addNamespaceForm.name" />
        </a-form-model-item>
        <a-form-model-item label="代码" prop="code">
          <a-input v-model="addNamespaceForm.code" />
        </a-form-model-item>
      </a-form-model>
    </a-modal>

    <!--删除命名空间-->
    <a-modal
      title="删除命名空间"
      :visible="deleteNamespaceModal"
      :confirm-loading="deleteNamespaceLoading"
      okText="确认"
      cancelText="取消"
      @ok="handleDeleteNamespaceOk"
      @cancel="handleDeleteNamespaceCancel"
    >
      <p>确认删除：{{ currentNamespace ? `${currentNamespace.name}[${currentNamespace.code}]` : '' }}？</p>
      <p style="color: #f5222d">注意：删除命名空间将会删除该命名空间下的所有资源！</p>
    </a-modal>
  </div>
</template>
<script lang="ts">
import { Component, Provide, Vue, Watch } from 'vue-property-decorator';
import { FormModel } from 'ant-design-vue';
import { Mutation } from 'vuex-class';
import { DEFAULT_NAMESPACE, EMPTY_NAMESPACE } from '@/lib/global/constant';

@Component({})
export default class Home extends Vue {
  title = '首页';

  currentNamespace = EMPTY_NAMESPACE;
  namespaceCode = DEFAULT_NAMESPACE.code;
  namespaceList = [DEFAULT_NAMESPACE];

  $refs!: { addNamespaceForm: FormModel };

  addNamespaceModal = false;
  addNamespaceLoading = false;
  addNamespaceForm = {
    name: '',
    code: ''
  };
  addNamespaceFormRules = {
    name: [{ required: true, message: '请输入名称!' }],
    code: [{ required: true, message: '请输入代码!' }]
  };

  deleteNamespaceModal = false;
  deleteNamespaceLoading = false;

  @Mutation('updateNamespace') updateNamespace!: Function;

  @Provide('handleChangeTitle') handleChangeTitleFunction = this.handleChangeTitle;

  @Watch('currentNamespace', { deep: true })
  onNamespaceChange(value: string) {
    this.updateNamespace(value);
  }

  handleChangeTitle(title: string) {
    this.title = title;
  }

  handleQueryNamespace() {
    return this.$axios
      .get('/vmr/namespace/list')
      .then((res: any) => {
        this.namespaceList = res.data;
        return res;
      })
      .catch((err: any) => {
        this.$message.error('查询命名空间失败');
        return err;
      });
  }

  handleNamespaceChange(code: string) {
    const namespace = this.namespaceList.find(item => item.code === code);
    if (namespace) {
      this.currentNamespace = namespace;
    }
  }

  handleShowAddNamespace() {
    this.addNamespaceModal = true;
  }
  handleAddNamespaceOk() {
    this.$refs.addNamespaceForm.validate((valid: boolean) => {
      if (valid) {
        this.addNamespaceLoading = true;
        this.$axios
          .post('/vmr/namespace', this.addNamespaceForm)
          .then(async (res: any) => {
            this.$message.success('新增成功');
            this.addNamespaceLoading = false;
            this.handleAddNamespaceCancel();
            await this.handleQueryNamespace();
            this.$nextTick(() => {
              this.namespaceCode = res.data.code;
            });
          })
          .catch((e: any) => {
            const message = this.$utils.obtainMessage(e, '新增失败');
            this.$message.error(message);
            this.addNamespaceLoading = false;
          });
      }
    });
  }
  handleAddNamespaceCancel() {
    this.addNamespaceModal = false;
    this.$refs.addNamespaceForm.resetFields();
  }

  handleShowDeleteNamespace() {
    if (this.currentNamespace) {
      this.deleteNamespaceModal = true;
    } else {
      this.$message.warn('请先创建命名空间');
    }
  }
  handleDeleteNamespaceOk() {
    this.deleteNamespaceLoading = true;
    this.$axios
      .delete(`/vmr/namespace/${this.currentNamespace.id}`)
      .then(async () => {
        this.$message.success('删除成功');
        this.deleteNamespaceLoading = false;
        const index = this.namespaceList.findIndex((item: any) => item.id === this.currentNamespace.id);
        this.handleDeleteNamespaceCancel();
        await this.handleQueryNamespace();
        this.currentNamespace = this.namespaceList[index - 1];
      })
      .catch(() => {
        this.$message.error('删除失败');
        this.deleteNamespaceLoading = false;
      });
  }
  handleDeleteNamespaceCancel() {
    this.deleteNamespaceModal = false;
  }

  async mounted() {
    await this.handleQueryNamespace();
    const namespace = this.namespaceList[0];
    this.currentNamespace = namespace;
    this.namespaceCode = namespace.code;
  }
}
</script>
<style scoped lang="less">
.vmr-home {
  height: 100%;

  .vmr-home-header {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    border-bottom: 1px solid #e8e8e8;
    padding-bottom: 5px;

    .vmr-home-title {
      flex: 1;
      font-size: 16px;
      display: flex;
      justify-content: flex-start;
      align-items: center;
    }

    .vmr-home-namespace {
      display: flex;
      justify-content: flex-end;
      align-items: center;

      .vmr-namespace-info {
        font-size: 20px;
        margin-right: 10px;
        cursor: pointer;
      }

      .vmr-namespace-select {
        width: 260px;
      }

      .vmr-namespace-button {
        margin-left: 15px;
      }
    }
  }

  .vmr-home-view {
    margin-top: 15px;
    height: calc(~'100% - 53px');
    overflow-y: auto;
    overflow-x: hidden;
  }
}
</style>

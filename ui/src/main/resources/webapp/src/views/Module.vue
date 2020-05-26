<template>
  <div class="vmr-module">
    <a-input-group class="vmr-module-action" compact>
      <a-select default-value="name" v-model="search.type">
        <a-select-option value="name">
          名称
        </a-select-option>
        <a-select-option value="code">
          代码
        </a-select-option>
      </a-select>
      <a-input-search placeholder="请输入模块名称或代码" v-model="search.value" @search="handleSearchModule">
        <a-button slot="enterButton" icon="search">查询</a-button>
      </a-input-search>
    </a-input-group>
    <div class="vmr-module-data">
      <a-spin :spinning="moduleLoading">
        <a-row class="vmr-module-list" :gutter="[16, 16]">
          <a-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6" :xxl="4">
            <a-card class="vmr-module-card vmr-module-add" :hoverable="true" @click="handleShowAddModule">
              <a-icon type="plus" :style="{ fontSize: '26px' }" />
            </a-card>
          </a-col>
          <a-col v-for="item in moduleList" :key="item.id" :xs="24" :sm="12" :md="8" :lg="6" :xl="6" :xxl="4">
            <a-card
              class="vmr-module-card vmr-module-info"
              :hoverable="true"
              :bodyStyle="{ padding: '5px 20px' }"
              @click="handleShowModuleDetail(item)"
            >
              <div class="vmr-module-detail">
                <div class="vmr-module-detail-title">{{ item.name }}</div>
                <p class="vmr-module-detail-item vmr-module-detail-code">
                  <span>模块代码：</span><span>{{ item.code }}</span>
                </p>
                <p class="vmr-module-detail-item vmr-module-detail-version">
                  <span>在用版本：</span><span>{{ item.current || '无' }}</span>
                </p>
                <p class="vmr-module-detail-item vmr-module-detail-time">
                  <span>更新时间：</span><span>{{ item.updateTime | timestamp }}</span>
                </p>
                <p class="vmr-module-detail-item vmr-module-detail-desc">
                  <span>模块描述：</span>
                  <span>{{ item.description }}</span>
                </p>
              </div>
              <template slot="actions">
                <a-icon key="delete" type="delete" @click="handleShowDeleteModule($event, item)" />
                <a-icon key="edit" type="edit" @click="handleShowModifyModule($event, item)" />
              </template>
            </a-card>
          </a-col>
        </a-row>
      </a-spin>
    </div>
    <div class="vmr-module-page">
      <a-pagination
        :total="moduleCount"
        :current="pageIndex"
        :pageSize="pageSize"
        show-less-items
        @change="handlePageIndexChange"
        @showSizeChange="handlePageSizeChange"
      />
    </div>

    <!--新增模块-->
    <a-modal
      title="新增模块"
      :visible="addModuleModal"
      :confirm-loading="addModuleLoading"
      okText="确认"
      cancelText="取消"
      @ok="handleAddModuleOk"
      @cancel="handleAddModuleCancel"
    >
      <a-form-model
        ref="addModuleForm"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 18 }"
        :model="addModuleForm"
        :rules="addModuleFormRules"
      >
        <a-form-model-item label="名称" prop="name">
          <a-input v-model="addModuleForm.name" />
        </a-form-model-item>
        <a-form-model-item label="代码" prop="code">
          <a-input v-model="addModuleForm.code" />
        </a-form-model-item>
        <a-form-model-item label="描述" prop="description">
          <a-textarea v-model="addModuleForm.description" :rows="4" />
        </a-form-model-item>
      </a-form-model>
    </a-modal>

    <!--修改模块-->
    <a-modal
      title="修改模块"
      :visible="modifyModuleModal"
      :confirm-loading="modifyModuleLoading"
      okText="确认"
      cancelText="取消"
      @ok="handleModifyModuleOk"
      @cancel="handleModifyModuleCancel"
    >
      <a-form-model
        ref="modifyModuleForm"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 18 }"
        :model="modifyModuleForm"
        :rules="modifyModuleFormRules"
      >
        <a-form-model-item label="名称" prop="name">
          <a-input v-model="modifyModuleForm.name" />
        </a-form-model-item>
        <a-form-model-item label="代码" prop="code">
          <a-input v-model="modifyModuleForm.code" :disabled="true" />
        </a-form-model-item>
        <a-form-model-item label="描述" prop="description">
          <a-textarea v-model="modifyModuleForm.description" :rows="4" />
        </a-form-model-item>
      </a-form-model>
    </a-modal>

    <!--删除模块-->
    <a-modal
      title="删除模块"
      :visible="deleteModuleModal"
      :confirm-loading="deleteModuleLoading"
      okText="确认"
      cancelText="取消"
      @ok="handleDeleteModuleOk"
      @cancel="handleDeleteModuleCancel"
    >
      <p>确认删除：{{ currentModule ? `${currentModule.name}[${currentModule.code}]` : '' }}？</p>
    </a-modal>
  </div>
</template>
<script lang="ts">
import { Component, Inject, Vue, Watch } from 'vue-property-decorator';
import { Getter } from 'vuex-class';
import { FormModel } from 'ant-design-vue';
import { Namespace } from '@/lib/types/data';
@Component({})
export default class Module extends Vue {
  $refs!: { addModuleForm: FormModel; modifyModuleForm: FormModel };

  search = {
    type: 'name',
    value: ''
  };

  moduleList = [];
  moduleCount = 0;
  moduleLoading = false;

  currentModule = {
    id: '',
    name: '',
    code: ''
  };

  addModuleModal = false;
  addModuleLoading = false;
  addModuleForm = {
    name: '',
    code: '',
    description: ''
  };
  addModuleFormRules = {
    name: [{ required: true, message: '请输入名称!' }],
    code: [{ required: true, message: '请输入代码!' }],
    description: [{ required: true, message: '请输入描述!' }]
  };

  modifyModuleModal = false;
  modifyModuleLoading = false;
  modifyModuleForm = {
    id: '',
    name: '',
    code: '',
    description: ''
  };
  modifyModuleFormRules = {
    name: [{ required: true, message: '请输入名称!' }],
    code: [{ required: true, message: '请输入代码!' }],
    description: [{ required: true, message: '请输入描述!' }]
  };

  deleteModuleModal = false;
  deleteModuleLoading = false;

  pageIndex = 1;
  pageSize = 20;

  @Getter('getCurrentNamespace') currentNamespace!: Namespace;

  @Inject('handleChangeTitle') handleChangeTitle!: Function;

  @Watch('currentNamespace', { deep: true })
  onNamespaceChange() {
    this.handleQueryModule();
  }

  handleQueryModule(): Promise<any> {
    if (!this.currentNamespace.code) {
      return Promise.resolve();
    }

    const params = {
      name: '',
      code: '',
      page: this.pageIndex - 1,
      size: this.pageSize,
      namespaceCode: this.currentNamespace.code
    };

    Object.defineProperty(params, this.search.type, { value: this.search.value });

    this.moduleLoading = true;
    return this.$axios
      .get('/vmr/module/page', { params: params })
      .then((res: any) => {
        if (res.data['content'].length > 0 || this.pageIndex === 1) {
          this.moduleList = res.data['content'];
          this.moduleCount = res.data['totalElements'];
        } else {
          this.pageIndex--;
          return this.handleQueryModule();
        }
        return res;
      })
      .catch((err: any) => {
        this.$message.error('查询模块失败');
        return err;
      })
      .finally(() => {
        this.moduleLoading = false;
      });
  }

  handleSearchModule() {
    this.handleQueryModule();
  }

  handleShowAddModule() {
    this.addModuleModal = true;
  }
  handleAddModuleOk() {
    this.$refs.addModuleForm.validate((valid: boolean) => {
      if (valid) {
        this.addModuleLoading = true;
        const newModule = Object.assign({ namespaceCode: this.currentNamespace.code }, this.addModuleForm);
        this.$axios
          .post('/vmr/module', newModule)
          .then(async () => {
            this.$message.success('新增成功');
            this.addModuleLoading = false;
            this.handleAddModuleCancel();
            await this.handleQueryModule();
          })
          .catch((e: any) => {
            const message = this.$utils.obtainMessage(e, '新增失败');
            this.$message.error(message);
            this.addModuleLoading = false;
          });
      }
    });
  }
  handleAddModuleCancel() {
    this.addModuleModal = false;
    this.$refs.addModuleForm.resetFields();
  }

  handleShowModifyModule(e: Event, item: any) {
    e.preventDefault();
    e.stopPropagation();
    this.modifyModuleModal = true;
    this.modifyModuleForm = Object.assign({}, item);
  }
  handleModifyModuleOk() {
    this.$refs.modifyModuleForm.validate((valid: boolean) => {
      if (valid) {
        this.modifyModuleLoading = true;
        this.$axios
          .put(`/vmr/module/${this.modifyModuleForm.id}`, this.modifyModuleForm)
          .then(async () => {
            this.$message.success('修改成功');
            this.modifyModuleLoading = false;
            this.handleModifyModuleCancel();
            await this.handleQueryModule();
          })
          .catch((e: any) => {
            const message = this.$utils.obtainMessage(e, '修改失败');
            this.$message.error(message);
            this.modifyModuleLoading = false;
          });
      }
    });
  }
  handleModifyModuleCancel() {
    this.modifyModuleModal = false;
    this.$refs.modifyModuleForm.resetFields();
  }

  handleShowDeleteModule(e: Event, item: any) {
    e.preventDefault();
    e.stopPropagation();
    this.currentModule = item;
    this.deleteModuleModal = true;
  }
  handleDeleteModuleOk() {
    this.deleteModuleLoading = true;
    this.$axios
      .delete(`/vmr/module/${this.currentModule.id}`)
      .then(() => {
        this.$message.success('删除成功');
        this.deleteModuleLoading = false;
        this.handleDeleteModuleCancel();
        this.handleQueryModule();
      })
      .catch(() => {
        this.$message.error('删除失败');
        this.deleteModuleLoading = false;
      });
  }
  handleDeleteModuleCancel() {
    this.deleteModuleModal = false;
  }

  handleShowModuleDetail(item: any) {
    this.$router.push({
      name: 'detail',
      params: { code: item.code }
    });
  }

  handlePageIndexChange(pageIndex: number) {
    this.pageIndex = pageIndex;
    this.handleQueryModule();
  }

  handlePageSizeChange(pageSize: number) {
    this.pageSize = pageSize;
    this.handleQueryModule();
  }

  mounted() {
    this.handleChangeTitle('模块列表');
    this.handleQueryModule();
  }
}
</script>
<style scoped lang="less">
@import '../style/index';

.vmr-module {
  height: 100%;

  .vmr-module-action {
    display: flex;
    margin-bottom: 10px;
  }

  .vmr-module-data {
    height: calc(~'100% - 85px');
    overflow-y: auto;
    overflow-x: hidden;

    .vmr-module-list {
      .vmr-module-card {
        &.vmr-module-add {
          height: 207px;
          display: flex;
          justify-content: center;
          align-items: center;
        }

        &.vmr-module-info {
          &::before {
            content: '';
            display: block;
            height: 6px;
            width: 100%;
            border-radius: 2px;
            transition: background-color 0.6s;
          }
          &:hover::before {
            background-color: @primary-color;
            opacity: 0.8;
          }
        }

        .vmr-module-detail {
          .vmr-module-detail-title {
            font-size: 16px;
            font-weight: bold;
            margin: 5px 0 10px 0;
          }

          .vmr-module-detail-item {
            margin-bottom: 5px;

            &.vmr-module-detail-desc {
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }
          }
        }
      }
    }
  }

  .vmr-module-page {
    margin: 5px 0;
    text-align: right;
  }
}
</style>

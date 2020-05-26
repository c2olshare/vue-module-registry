<template>
  <div class="vmr-detail" ref="vmr-detail">
    <div class="vmr-detail-left">
      <div class="vmr-package-list">
        <a-spin :spinning="packageLoading">
          <a-timeline v-if="packageList.length > 0">
            <a-timeline-item
              v-for="item in packageList"
              :key="item.id"
              :color="currentModule.current === item.version ? 'green' : 'gray'"
            >
              <a-icon
                v-if="currentModule.current === item.version"
                slot="dot"
                type="check-circle"
                style="font-size: 16px;"
              />
              <div class="vmr-package-item" @click="handleShowPackageFile($event, item)">
                <div class="vmr-package-version">{{ item.version }}</div>
                <div class="vmr-package-description">{{ item.description }}</div>
                <div class="vmr-package-info">
                  <div>
                    <a-icon type="clock-circle" />
                    <span> {{ item.createTime | timestamp }}</span>
                  </div>
                </div>
                <div class="vmr-package-action">
                  <a-tooltip placement="bottom">
                    <template slot="title">
                      <span>切换到该版本</span>
                    </template>
                    <a-button
                      type="link"
                      icon="check"
                      :loading="modifyModuleLoading"
                      :disabled="currentModule.current === item.version"
                      @click="handleShowChangeVersion($event, item)"
                    ></a-button>
                  </a-tooltip>
                  <a-tooltip placement="bottom">
                    <template slot="title">
                      <span>删除该版本</span>
                    </template>
                    <a-button
                      type="link"
                      icon="delete"
                      :disabled="currentModule.current === item.version"
                      :loading="deletePackageLoading"
                      @click="handleShowDeletePackage($event, item)"
                    ></a-button>
                  </a-tooltip>
                </div>
              </div>
            </a-timeline-item>
          </a-timeline>
          <div v-else>暂无数据</div>
        </a-spin>
      </div>
      <div class="vmr-package-page">
        <a-pagination
          :total="packageCount"
          :current="pageIndex"
          :pageSize="pageSize"
          show-less-items
          @change="handlePageIndexChange"
          @showSizeChange="handlePageSizeChange"
        />
      </div>
    </div>
    <div class="vmr-detail-right">
      <a-affix :target="vmrContentView">
        <div class="vmr-package-ops">
          <div class="vmr-module-title">{{ currentModule.name }}</div>
          <div class="vmr-module-description">{{ currentModule.description }}</div>
          <div class="vmr-package-ud">
            <div class="vmr-package-upload">
              <a-tooltip placement="bottom">
                <template slot="title">
                  <span>上传模块包</span>
                </template>
                <a-button size="large" shape="circle" type="primary" @click="handleShowAddPackage">
                  <a-icon type="cloud-upload" />
                </a-button>
              </a-tooltip>
            </div>
            <!--<div class="vmr-package-download">
              <a-tooltip placement="bottom">
                <template slot="title">
                  <span>下载模块包</span>
                </template>
                <a-button size="large" shape="circle"> <a-icon type="cloud-download"/></a-button>
              </a-tooltip>
            </div>-->
            <div class="vmr-package-download">
              <a-tooltip placement="bottom">
                <template slot="title">
                  <span>刷新列表</span>
                </template>
                <a-button size="large" shape="circle" @click="handleQueryPackage">
                  <a-icon type="sync" />
                </a-button>
              </a-tooltip>
            </div>
          </div>
        </div>
      </a-affix>
    </div>

    <!--上传模块包-->
    <a-modal
      title="上传模块包"
      :visible="addPackageModal"
      :confirm-loading="addPackageLoading"
      okText="确认"
      cancelText="取消"
      @ok="handleAddPackageOk"
      @cancel="handleAddPackageCancel"
    >
      <a-form-model
        ref="addPackageForm"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 18 }"
        :model="addPackageForm"
        :rules="addPackageFormRules"
      >
        <a-form-model-item label="文件" prop="file">
          <a-upload
            name="file"
            accept=".zip"
            :fileList="addPackageFileList"
            :beforeUpload="handleBeforeUploadPackage"
            :remove="handleRemoveUploadPackage"
          >
            <a-button type="dashed"> <a-icon type="upload" /> 点击选择文件或者拖拽文件到此处 </a-button>
          </a-upload>
        </a-form-model-item>
        <a-form-model-item label="版本" prop="version">
          <a-input v-model="addPackageForm.version" />
        </a-form-model-item>
        <a-form-model-item label="描述" prop="description">
          <a-textarea v-model="addPackageForm.description" :rows="4" />
        </a-form-model-item>
        <a-form-model-item label="激活" prop="active">
          <a-switch v-model="addPackageForm.active">
            <a-icon slot="checkedChildren" type="check" />
            <a-icon slot="unCheckedChildren" type="close" />
          </a-switch>
        </a-form-model-item>
      </a-form-model>
    </a-modal>

    <!--删除模块包-->
    <a-modal
      title="删除模块包"
      :visible="deletePackageModal"
      :confirm-loading="deletePackageLoading"
      okText="确认"
      cancelText="取消"
      @ok="handleDeletePackageOk"
      @cancel="handleDeletePackageCancel"
    >
      <p>确认删除：{{ currentPackage ? `${currentModule.name}[${currentPackage.version}]` : '' }}？</p>
    </a-modal>

    <!--切换版本-->
    <a-modal
      title="切换版本"
      :visible="changeVersionModal"
      :confirm-loading="changeVersionLoading"
      okText="确认"
      cancelText="取消"
      @ok="handleChangeVersionOk"
      @cancel="handleChangeVersionCancel"
    >
      <p>确认切换到：{{ currentPackage ? `${currentModule.name}[${currentPackage.version}]` : '' }}？</p>
    </a-modal>

    <!--文件列表-->
    <a-modal
      :visible="packageFileModal"
      cancelText="关闭"
      @cancel="handlePackageFileCancel"
      :ok-button-props="{ style: { display: 'none' } }"
    >
      <template slot="title">
        <div style="display: flex">
          <span><a-icon type="file"/></span>
          <span>{{ currentPackage ? `${currentModule.name}[${currentPackage.version}]` : '' }}</span>
        </div>
      </template>
      <div>
        <div style="display: flex; margin-bottom: 10px">
          <div v-if="packageFilePath.length > 0">
            <a @click="handlePackageFilePreClick">上一级</a>
            <span> | </span>
            <a @click="handlePackageFileAllClick">全部&gt;</a>
          </div>
          <div
            style="flex: 1; overflow: hidden; white-space:nowrap;
            text-overflow:ellipsis; text-align: left;"
          >
            <span v-for="(item, index) in packageFilePath" :key="index + item">
              <a @click="handlePackageFilePathClick(index)" v-if="index < packageFilePath.length - 1">
                {{ item }}&gt;
              </a>
            </span>
            <span>{{ packageFilePath[packageFilePath.length - 1] }}</span>
          </div>
          <div>已全部加载，共{{ packageFileList.length }}个</div>
        </div>
        <a-table
          :columns="packageFileColumns"
          :data-source="packageFileList"
          :pagination="false"
          :loading="packageFileLoading"
          :customRow="packageFileCustomRow"
          :scroll="{ y: 300 }"
          rowKey="name"
          size="small"
        >
          <span
            slot="name"
            slot-scope="text, record"
            style="display: flex; justify-content: flex-start; align-items: center;"
          >
            <i class="vmr vmr-folder" v-if="record.directory"></i>
            <i :class="`${$iconfont.getClass(record.extension)}`" v-else-if="$iconfont.getIcon(record.extension)"></i>
            <a-icon type="file-unknown" v-else />
            <span style="display: block; margin-left: 5px" :title="text">{{ text }}</span>
          </span>
          <span slot="length" slot-scope="text, record">
            <span v-if="record.directory">-</span>
            <span v-else>{{ Math.round(text / 1024) }} KB</span>
          </span>
          <span slot="time" slot-scope="text">{{ text | timestamp }}</span>
        </a-table>
      </div>
    </a-modal>
  </div>
</template>
<script lang="ts">
import { Component, Inject, Vue, Watch } from 'vue-property-decorator';
import { Getter } from 'vuex-class';
import { FormModel } from 'ant-design-vue';
import { Namespace } from '@/lib/types/data';

@Component({})
export default class Detail extends Vue {
  $refs!: { addPackageForm: FormModel };

  currentModule = {
    id: '',
    code: '',
    name: '',
    current: '',
    description: '',
    namespaceCode: '',
    lockVersion: ''
  };
  modifyModuleLoading = false;

  packageList = [];
  packageCount = 0;
  packageLoading = true;

  currentPackage = {
    id: '',
    version: '',
    moduleCode: '',
    namespaceCode: ''
  };

  addPackageModal = false;
  addPackageLoading = false;
  addPackageForm = {
    file: '',
    version: '',
    active: false,
    description: ''
  };
  addPackageFile!: null | File;
  addPackageFileList: Array<File> = [];
  addPackageFormRules = {
    file: [{ required: true, message: '请选择文件!' }],
    version: [{ required: true, message: '请输入版本!' }],
    description: [{ required: true, message: '请输入描述!' }],
    active: [{ required: true, message: '请选择是否激活!' }]
  };

  deletePackageModal = false;
  deletePackageLoading = false;

  changeVersionModal = false;
  changeVersionLoading = false;

  packageFileList = [];
  packageFileColumns = [
    {
      title: '文件名',
      dataIndex: 'name',
      scopedSlots: { customRender: 'name' },
      ellipsis: true
    },
    {
      title: '大小',
      dataIndex: 'length',
      scopedSlots: { customRender: 'length' },
      width: 100,
      ellipsis: true
    },
    {
      title: '时间',
      dataIndex: 'modifyTime',
      scopedSlots: { customRender: 'time' },
      width: 150,
      ellipsis: true
    }
  ];
  packageFileModal = false;
  packageFilePath: Array<string> = [];
  packageFileLoading = false;

  pageIndex = 1;
  pageSize = 20;

  @Getter('getCurrentNamespace') currentNamespace!: Namespace;

  @Inject('handleChangeTitle') handleChangeTitle!: Function;

  @Watch('currentNamespace')
  onNamespaceChange() {
    this.handleInitModuleInfo();
  }

  vmrContentView() {
    return this.$parent.$refs['vmr-home-view'];
  }

  handleInitModuleInfo() {
    const code = this.$route.params.code;
    const params = {
      namespaceCode: this.currentNamespace.code
    };
    return this.$axios
      .get(`/vmr/module/code/${code}`, { params: params })
      .then((res: any) => {
        this.currentModule = res.data;
        return res;
      })
      .catch((error: any) => {
        this.$router.replace({ name: 'module' });
        return error;
      });
  }

  handleQueryPackage(): Promise<any> {
    const params = {
      page: this.pageIndex - 1,
      size: this.pageSize,
      moduleCode: this.currentModule.code,
      namespaceCode: this.currentModule.namespaceCode,
      sort: 'createTime,desc'
    };

    this.packageLoading = true;
    return this.$axios
      .get('/vmr/package/page', { params: params })
      .then((res: any) => {
        if (res.data['content'].length > 0 || this.pageIndex === 1) {
          this.packageList = res.data['content'];
          this.packageCount = res.data['totalElements'];
        } else {
          this.pageIndex--;
          return this.handleQueryPackage();
        }
        return res;
      })
      .catch((err: any) => {
        this.$message.error('查询模块包失败');
        return err;
      })
      .finally(() => {
        this.packageLoading = false;
      });
  }

  handleShowAddPackage() {
    this.addPackageModal = true;
  }
  handleBeforeUploadPackage(file: File) {
    const types = ['application/zip', 'application/x-zip-compressed'];
    if (types.indexOf(file.type) > -1) {
      this.addPackageForm.file = file.name;
      this.addPackageFile = file;
      this.addPackageFileList = [file];
    } else {
      this.addPackageForm.file = '';
      this.addPackageFile = null;
      this.addPackageFileList = [];
    }

    return false;
  }
  handleRemoveUploadPackage() {
    this.addPackageForm.file = '';
    this.addPackageFile = null;
    this.addPackageFileList = [];

    return true;
  }
  handleAddPackageOk() {
    this.$refs.addPackageForm.validate((valid: boolean) => {
      if (valid) {
        this.addPackageLoading = true;
        const formData = new FormData();
        formData.append('file', this.addPackageFile as Blob);
        formData.append('version', this.addPackageForm.version);
        formData.append('description', this.addPackageForm.description);
        formData.append('active', `${this.addPackageForm.active}`);
        formData.append('moduleCode', this.currentModule.code);
        formData.append('namespaceCode', this.currentNamespace.code);

        this.$axios
          .post('/vmr/package/publish', formData)
          .then(async () => {
            this.$message.success('上传成功');
            this.addPackageLoading = false;
            this.handleAddPackageCancel();
            await this.handleInitModuleInfo();
            await this.handleQueryPackage();
          })
          .catch((e: any) => {
            const message = this.$utils.obtainMessage(e, '上传失败');
            this.$message.error(message);
            this.addPackageLoading = false;
          });
      }
    });
  }
  handleAddPackageCancel() {
    this.addPackageModal = false;
    this.$refs.addPackageForm.resetFields();

    // clear file
    this.addPackageForm.file = '';
    this.addPackageFile = null;
    this.addPackageFileList = [];
  }

  handleShowDeletePackage(e: Event, item: any) {
    e.preventDefault();
    e.stopPropagation();
    this.currentPackage = item;
    this.deletePackageModal = true;
  }
  handleDeletePackageOk() {
    this.deletePackageLoading = true;
    this.$axios
      .delete(`/vmr/package/${this.currentPackage.id}`)
      .then(() => {
        this.$message.success('删除成功');
        this.deletePackageLoading = false;
        this.handleDeletePackageCancel();
        this.handleQueryPackage();
      })
      .catch(() => {
        this.$message.error('删除失败');
        this.deletePackageLoading = false;
      });
  }
  handleDeletePackageCancel() {
    this.deletePackageModal = false;
  }

  handleShowChangeVersion(e: Event, item: any) {
    e.preventDefault();
    e.stopPropagation();
    this.currentPackage = item;
    this.changeVersionModal = true;
  }
  handleChangeVersionOk() {
    const newModule = {
      id: this.currentModule.id,
      current: this.currentPackage.version,
      lockVersion: this.currentModule.lockVersion
    };

    this.changeVersionLoading = true;
    this.$axios
      .put(`/vmr/module/${newModule.id}`, newModule)
      .then(() => {
        this.$message.success('切换成功');
        this.changeVersionLoading = false;
        this.handleChangeVersionCancel();
        this.handleInitModuleInfo();
      })
      .catch((e: any) => {
        const message = this.$utils.obtainMessage(e, '切换失败');
        this.$message.error(message);
        this.changeVersionLoading = false;
      });
  }
  handleChangeVersionCancel() {
    this.changeVersionModal = false;
  }

  handleShowPackageFile(e: Event, item: any) {
    e.preventDefault();
    e.stopPropagation();
    this.currentPackage = item;
    this.packageFileModal = true;
    this.handleQueryPackageFile();
  }
  packageFileCustomRow(record: any) {
    return {
      style: {
        cursor: 'pointer'
      },
      on: {
        click: () => this.handlePackageFileClick(record)
      }
    };
  }
  handleQueryPackageFile() {
    this.packageFileLoading = true;
    const params = {
      namespaceCode: this.currentNamespace.code
    };
    this.$axios
      .get(
        `/vmr/package/files/${this.currentModule.code}/${this.currentPackage.version}/${this.packageFilePath.join(
          '/'
        )}`,
        { params: params }
      )
      .then((res: any) => {
        this.packageFileList = res.data;
      })
      .finally(() => {
        this.packageFileLoading = false;
      });
  }
  handlePackageFileClick(record: any) {
    if (record.directory) {
      this.packageFilePath.push(record.name);
      this.handleQueryPackageFile();
    } else {
      window.open(record.url, '_blank');
    }
  }
  handlePackageFilePreClick() {
    this.packageFilePath.pop();
    this.handleQueryPackageFile();
  }
  handlePackageFileAllClick() {
    this.packageFilePath = [];
    this.handleQueryPackageFile();
  }
  handlePackageFilePathClick(index: number) {
    this.packageFilePath = this.packageFilePath.slice(0, index + 1);
    this.handleQueryPackageFile();
  }
  handlePackageFileCancel() {
    this.packageFileModal = false;
    this.packageFileList = [];
    this.packageFilePath = [];
  }

  handlePageIndexChange(pageIndex: number) {
    this.pageIndex = pageIndex;
    this.handleQueryPackage();
  }

  handlePageSizeChange(pageSize: number) {
    this.pageSize = pageSize;
    this.handleQueryPackage();
  }

  async mounted() {
    this.handleChangeTitle('版本列表');
    await this.handleInitModuleInfo();
    await this.handleQueryPackage();
  }
}
</script>
<style scoped lang="less">
@import '../style/index';

.vmr-detail {
  min-height: 100%;
  display: flex;

  .vmr-detail-left {
    flex: 1;
    padding: 5px;
    position: relative;
  }

  .vmr-detail-right {
    border-left: 1px solid #e8e8e8;
    min-width: 300px;
    max-width: 500px;
    width: 30%;
    position: relative;
  }

  .vmr-package-list {
    margin-bottom: 50px;

    .vmr-package-item {
      cursor: pointer;
      position: relative;
      padding: 0 10px;

      .vmr-package-version {
        font-size: 18px;
        font-weight: bold;
        margin-bottom: 10px;
      }

      .vmr-package-description {
        margin-bottom: 10px;
      }

      .vmr-detail-info {
        display: flex;
      }

      .vmr-package-action {
        display: none;
        position: absolute;
        top: -2px;
        right: 0;
      }

      &:hover {
        .vmr-package-action {
          display: block;
        }
      }
    }
  }

  .vmr-package-page {
    width: 100%;
    text-align: center;
    margin-bottom: 10px;
    position: absolute;
    bottom: 0;
  }

  .vmr-package-ops {
    padding: 0 10px;

    .vmr-module-title {
      font-size: 18px;
      font-weight: bold;
      margin-bottom: 5px;
    }

    .vmr-module-description {
      color: @text-color-secondary;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      margin-bottom: 20px;
    }

    .vmr-package-ud {
      display: flex;

      .vmr-package-upload {
        margin-right: 10px;
      }

      .vmr-package-download {
        margin-right: 10px;
      }
    }
  }
}
</style>
<style lang="less">
@import '../style/color';
.vmr-detail {
  .ant-timeline-item {
    &:hover {
      .ant-timeline-item-head {
        border-color: @primary-color;
      }
    }
  }

  .ant-spin-dot {
    margin-top: 0 !important;
  }
}
</style>

<template>
  <d2-mng-page
          @qry-data="qryData"
          @create="onCreate"
          @update="onUpdate"
          @del="onDel"
          :page="page"
          :form-mode.sync="formMode"
          :dialog-form-visible.sync="dialogFormVisible"
          :select-rows="selectRows"
          :show-option-box="true">
    <el-form slot="search-inner-box" :inline="true" :model="searchForm" :rules="searchRules" ref="ruleSearchForm" class="demo-form-inline">
      <el-form-item label="渠道" prop="channel">
        <el-select size="small" v-model="searchForm.channel" placeholder="请选择" :required="true" @change="onChangeSearchChannel">
          <el-option
                  v-for="item in channelOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="用户名">
        <el-input
                size="small"
                placeholder="请输入用户名"
                v-model="searchForm.username">
        </el-input>
      </el-form-item>
      <el-form-item label="手机号">
        <el-input
                size="small"
                placeholder="请输入手机号"
                v-model="searchForm.phone">
        </el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input
                size="small"
                placeholder="请输入邮箱"
                v-model="searchForm.email">
        </el-input>
      </el-form-item>
      <el-form-item class="search-inner-btn-box">
        <el-button size="small" type="primary" icon="el-icon-search" @click="onSearch">查询</el-button>
        <el-button size="small" icon="el-icon-refresh" @click="onCancelSubmit">重置</el-button>
      </el-form-item>
    </el-form>

    <ul slot="hint-msg-box">
      <li>`角色名称和角色标识`必须唯一</li>
      <li>不能创建和系统管理员角色相同角色标识的记录</li>
      <li>系统管理员角色不允许修改</li>
    </ul>

    <el-table
            ref="table"
            :data="page.records"
            stripe
            :height="listHeight"
            border
            @selection-change="handleSelectionChange"
            @expand-change="handleExpandChangge"
            style="width: 100%">
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="top" inline class="demo-table-expand">
            <el-form-item label="资源授权">
              <el-tree
                      :data="resources"
                      :props="treeProps"
                      node-key="id"
                      :default-expanded-keys="props.row.expandedKeys"
                      :default-checked-keys="props.row.checkedKeys"
                      @check-change="handleTableRowCheckChange"
                      show-checkbox></el-tree>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column
              type="selection"
              :width="tableSelectionWidth">
      </el-table-column>
      <el-table-column
              prop="username"
              label="用户名">
      </el-table-column>
      <el-table-column
              prop="phone"
              label="手机号">
      </el-table-column>
      <el-table-column
              prop="email"
              label="邮箱">
      </el-table-column>
      <el-table-column
              prop="createTimeStr"
              label="创建时间">
      </el-table-column>
    </el-table>

    <el-form slot="form" :model="form" :rules="rules" ref="form" @submit.native.prevent>
      <div class="dialog-form-submit-container">

        <d2-el-form-item label="用户名" :required="true" prop="username">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </d2-el-form-item>

        <d2-el-form-item label="密码" :required="true" prop="password">
          <el-input v-model="form.password" autocomplete="off"></el-input>
        </d2-el-form-item>

        <d2-el-form-item label="手机号">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </d2-el-form-item>

        <d2-el-form-item label="邮箱">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </d2-el-form-item>

        <d2-el-form-item label="资源授权">
        </d2-el-form-item>
        <div class="dialog-form-submit-inner-container">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" native-type="submit" @click="onSubmitForm">确 定</el-button>
        </div>
      </div>
    </el-form>
  </d2-mng-page>
</template>

<script>
import _ from 'lodash'
import { mapState } from 'vuex'
export default {
  name: 'mngauth-role',
  data () {
    return {
      formMode: 'add',
      dialogFormVisible: false,
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
        ],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      form: {
        username: '',
        password: '',
        phone: '',
        email: ''
      },
      formTmpl: {},
      selectRows: [],
      page: {
        records: [],
        total: 0,
        size: this.$store.state.d2admin.page.defSize,
        current: this.$store.state.d2admin.page.defCurrent
      },
      channel: this.$store.state.d2admin.page.defChannel,
      channelOptions: this.$store.state.d2admin.page.channelOptions,
      searchRules: {
        channel: [
          { required: true, message: '请选择渠道', trigger: 'change' }
        ]
      },
      searchForm: {
        channel: this.$store.state.d2admin.page.defChannel,
        username: '',
        phone: '',
        email: ''
      }
    }
  },
  computed: {
    ...mapState('d2admin', {
      listHeight: state => state.page.listHeight,
      tableSelectionWidth: state => state.page.tableSelectionWidth
    })
  },
  methods: {
    handleTableRowCheckChange(data, checked, indeterminate) {
      // 首次展开会通知，data为根节点，这时不做处理
      // if (data.id !== '0') {
      //   if (checked) {
      //     this.form.resources.push(data)
      //   } else {
      //     _.remove(this.form.resources, item => {
      //       return item.id === data.id;
      //     });
      //   }
      //   const params = _.clone(this.form);
      //   this.$vp.ajaxPut('role', {
      //     params
      //   }).then(res => {
      //     this.$vp.toast('授权修改成功', { type: 'success' });
      //   })
      // }
    },
    qryData() {
      this.$vp.ajaxGet(`admin/${this.channel}/${this.page.current}/${this.page.size}`).then(res => { this.page = res })
    },
    handleExpandChangge(row, expandedRows) {
      // this.$vp.ajaxAll([
      //   {
      //     url: `resource/${this.channel}`,
      //     mode: 'GET'
      //   }, {
      //     url: `role/eleui/${row.id}`,
      //     mode: 'GET'
      //   }
      // ])
      //   .then(resArr => {
      //     // 这里需要应用手动把axios的data属性解析掉
      //     const res = _.map(resArr, (item) => {
      //       return item.data;
      //     })
      //     // 设置`表单 资源树`
      //     this.resources = res[0].data
      //     // 更新记录，主要是expandedKeys和checkedKeys
      //     this._copyRoleDto(row, res[1].data)
      //     this.form = _.clone(row)
      //   })
    },
    _copyRoleDto(current, orig) {
      // current.name = orig.name
      // current.authorityName = orig.authorityName
      // current.channel = orig.channel
      // current.resources = orig.resources
      // current.expandedKeys = orig.expandedKeys
      // current.checkedKeys = orig.checkedKeys
    },
    handleSelectionChange(rows) {
      this.selectRows = rows
    },
    onChangeSearchChannel(idx) {
      this.channel = this.channelOptions[idx].value
      this.qryData()
    },
    onSearch() {
      this.$refs.ruleSearchForm.validate((valid) => {
        if (valid) {
          this.$vp.ajaxPostJson(`admin/search/${this.channel}/${this.page.current}/${this.page.size}`, { params: this.searchForm }).then(res => { this.page = res })
        }
      });
    },
    onCancelSubmit() {
      this.searchForm = _.clone({
        channel: this.channel
      })
      this.qryData()
    },
    onCreate() {
      this.form = _.clone(this.formTmpl)
    },
    onUpdate(item) {
      // this.$vp.ajaxAll([
      //   {
      //     url: `resource/${this.channel}`,
      //     mode: 'GET'
      //   }, {
      //     url: `role/eleui/${item.id}`,
      //     mode: 'GET'
      //   }
      // ])
      //   .then(resArr => {
      //     // 这里需要应用手动把axios的data属性解析掉
      //     const res = _.map(resArr, (item) => {
      //       return item.data;
      //     })
      //     // 设置`表单 资源树`
      //     this.resources = res[0].data
      //     this.form = res[1].data
      //     this.dialogFormVisible = true
      //   })
    },
    onDel() {
      const records = _.clone(this.selectRows)
      const ids = []
      records.forEach(item => {
        ids.push(item.id)
      })
      this.$vp.ajaxDel(`admin`, {
        params: {
          ids
        }
      }).then(res => {
        this.qryData();
      }).finally(this.selectRows = []);
    },
    _submitFinally() {
      this.form = _.clone(this.formTmpl)
      this.dialogFormVisible = false
    },
    onSubmitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          const params = _.clone(this.form);
          if (this.formMode === 'add') {
            params.channel = this.channel
            this.$vp.ajaxPostJson('admin', {
              params
            }).then(res => {
              this.qryData()
            }).finally(() => {
              this._submitFinally()
            });
          } else {
            this.$vp.ajaxPut('admin', {
              params
            }).then(res => {
              this.qryData()
            }).finally(() => {
              this._submitFinally()
            });
          }
        }
      })
    }
  },
  created() {
    this.formTmpl = _.clone(this.form)
    this.onCancelSubmit()
  }
}
</script>

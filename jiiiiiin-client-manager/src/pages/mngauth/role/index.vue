<template>
  <d2-mng-page
          @qry-data="qryData"
          @create="onCreate"
          @update="onUpdate"
          @del="onDel"
          :page="page"
          :dialog-form-visible="dialogFormVisible"
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
      <el-form-item label="标识">
        <el-input
                size="small"
                placeholder="请输入标识"
                v-model="searchForm.authorityName">
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" icon="el-icon-search" @click="onSearch">查询</el-button>
        <el-button size="small" icon="el-icon-refresh" @click="onCancelSubmit">重置</el-button>
      </el-form-item>
    </el-form>

    <ul slot="hint-msg-box">
      <li>`角色标识`必须唯一</li>
    </ul>

    <el-table
            :data="page.records"
            stripe
            :height="listHeight"
            border
            @selection-change="handleSelectionChange"
            style="width: 100%">
      <el-table-column
              type="selection"
              :width="tableSelectionWidth">
      </el-table-column>
      <el-table-column
              prop="name"
              label="角色名称">
      </el-table-column>
      <el-table-column
              prop="authorityName"
              label="角色标识"
              >
      </el-table-column>
    </el-table>

    <el-form slot="form" :model="form" :rules="rules" ref="form" @submit.native.prevent>
      <div class="dialog-form-submit-container">

        <d2-el-form-item label="角色名称" :required="true" prop="name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </d2-el-form-item>

        <d2-el-form-item label="角色标识" :required="true" prop="authorityName">
          <el-input v-model="form.authorityName" autocomplete="off"></el-input>
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
      dialogFormVisible: false,
      rules: {
        name: [
          { required: true, message: '请输入角色名称', trigger: 'blur' },
          { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
        ],
        authorityName: [
          { required: true, message: '请输入角色标识', trigger: 'blur' },
          { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
        ]
      },
      form: {
        name: '',
        authorityName: ''
      },
      formTempl: {
        name: '',
        authorityName: ''
      },
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
        authorityName: 'ADMIN'
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
    qryData() {
      this.$vp.ajaxGet(`role/${this.channel}/${this.page.current}/${this.page.size}`).then(res => { this.page = res })
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
          this.$vp.ajaxGet(`role/${this.channel}/${this.page.current}/${this.page.size}/${this.searchForm.authorityName}`).then(res => { this.page = res })
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
      this.dialogFormVisible = true
    },
    onUpdate() {
    },
    onDel() {
      const item = this.selectRows[0]
      this.$vp.ajaxDel(`role/${this.channel}/${item.id}`)
        .then(res => {
          this.qryData()
        })
    },
    onSubmitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          let params = _.clone(this.form);
          params.channel = this.channel
          this.$vp.ajaxPostJson('role', {
            params
          }).then(res => {
            this.qryData()
          });
          this.dialogFormVisible = false
        }
      })
    }
  },
  created() {
    this.onCancelSubmit()
  }
}
</script>

<template>
  <d2-container class="mng-list">
      <el-row>
        <el-col :span="23">
          <el-form :inline="true" :model="searchForm" :rules="searchRules" ref="ruleSearchForm" class="demo-form-inline">
            <el-form-item label="渠道" prop="channel">
              <el-select v-model="searchForm.channel" placeholder="请选择" :required="true" @change="onChangeSearchChannel">
                <el-option
                        v-for="item in channelOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button size="small" type="primary" icon="el-icon-search" @click="onSearch" >查询</el-button>
              <el-button size="small" icon="el-icon-refresh" @click="onCancelSubmit">重置</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="1">
          <el-popover
                  placement="top-start"
                  title="温馨提示"
                  width="400"
                  trigger="hover">
            <ul>
              <li>只可以删除叶子节点</li>
              <li>点击`重置按钮`可以恢复列表初始化状态</li>
              <li>直接点击记录的`状态切换开关`可以直接修改节点状态</li>
            </ul>
            <el-button slot="reference" size="small" icon="el-icon-info" class="mng-list-title-hint-btn">操作提示</el-button>
          </el-popover>
        </el-col>
      </el-row>
    <el-table
            :data="page.records"
            stripe
            :height="listHeight"
            border
            style="width: 100%">

      <el-table-column
              type="selection"
              width="55">
      </el-table-column>
      <el-table-column
              prop="name"
              label="角色名称"
              width="180">
      </el-table-column>
      <el-table-column
              prop="authorityName"
              label="角色名称"
              width="180">
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
                  size="mini"
                  @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button
                  size="mini"
                  type="danger"
                  @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <template slot="footer">
      <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="current"
              :page-sizes="pageSizes"
              :page-size="page.size"
              layout="total, sizes, prev, pager, next, jumper"
              :total="page.total">
      </el-pagination>
    </template>
  </d2-container>
</template>

<script>
import _ from 'lodash'
import { mapState } from 'vuex'
export default {
  name: 'mngauth-role',
  data () {
    return {
      channelOptions: [
        {
          value: '0',
          label: '内管'
        }
      ],
      searchRules: {
        channel: [
          { required: true, message: '请选择渠道', trigger: 'change' }
        ]
      },
      searchForm: {
        // TODO 同步为`state.page.defCurrent`
        channel: '0'
      },
      page: {
        records: [],
        total: 0,
        size: 1,
        current: 1
      }
    }
  },
  computed: {
    ...mapState('d2admin', {
      listHeight: state => state.page.listHeight,
      channel: state => state.page.defChannel,
      current: state => state.page.defCurrent,
      size: state => state.page.defSize,
      pageSizes: state => state.page.pageSizes
    })
  },
  methods: {
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
    },
    handleEdit(index, row) {
      console.log(index, row);
    },
    handleDelete(index, row) {
      console.log(index, row);
    },
    onChangeSearchChannel() {
      // TODO 待具有其他渠道 这里需要 同步到 `this.channel`状态
    },
    onSearch() {
      this.onCancelSubmit()
    },
    onCancelSubmit() {
      this.searchForm = _.clone({
        channel: this.channel
      })
      this.$vp.ajaxGet(`role/${this.channel}/${this.current}/${this.size}`).then(res => { this.page = res })
    }
  },
  created() {
    this.onCancelSubmit()
  }
}
</script>

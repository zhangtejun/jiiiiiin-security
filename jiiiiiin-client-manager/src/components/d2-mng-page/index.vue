<template>
  <d2-container class="mng-list">
    <slot name="search-box">
        <el-row>
            <el-col :span="showOptionBox? 24 : 23">
                <slot name="search-inner-box">
                </slot>
            </el-col>
            <el-col :span="1" v-if="!showOptionBox">
                <el-popover
                        placement="top-start"
                        title="温馨提示"
                        width="400"
                        trigger="hover">
                    <slot name="hint-msg-box"></slot>
                    <el-button slot="reference" size="small" icon="el-icon-info" class="mng-list-title-hint-btn">操作提示</el-button>
                </el-popover>
            </el-col>
        </el-row>
    </slot>

    <slot name="option-box" v-if="showOptionBox">
      <el-row class="mng-list-option-box">
        <el-col :span="23">
            <el-button size="small" type="primary" icon="el-icon-edit" @click="$emit('create')">新增</el-button>
            <el-button size="small" icon="el-icon-share" @click="onClickUpdate">修改</el-button>
            <el-button size="small" type="danger" icon="el-icon-delete" @click="onClickDel">删除</el-button>
        </el-col>
        <el-col :span="1">
        <el-popover
                placement="top-start"
                title="温馨提示"
                width="400"
                trigger="hover">
          <slot name="hint-msg-box"></slot>
          <el-button slot="reference" size="small" icon="el-icon-info" class="mng-list-title-hint-btn">操作提示</el-button>
        </el-popover>
        </el-col>
      </el-row>
    </slot>

    <slot>
      <el-table
              :data="page.records"
              stripe
              :height="listHeight"
              border
              style="width: 100%">
        <!--实现自己需要定义table-->
      </el-table>
    </slot>

    <template slot="footer">
      <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="page.current"
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
  name: 'd2-mng-page',
  props: {
    showOptionBox: false,
    selectRows: {
      type: Array,
      default() {
        return []
      }
    },
    page: {
      type: Object,
      default() {
        return {
          records: [],
          total: 0,
          size: this.$store.state.d2admin.page.defSize,
          current: this.$store.state.d2admin.page.defCurrent
        }
      }
    }
  },
  data () {
    return {
      channelOptions: [
        {
          value: '0',
          label: '内管'
        }
      ]
    }
  },
  computed: {
    ...mapState('d2admin', {
      listHeight: state => state.page.listHeight,
      pageSizes: state => state.page.pageSizes
    })
  },
  methods: {
    handleSizeChange(val) {
      this.page.size = val
      // 混合组件实现
      this.qryData()
    },
    handleCurrentChange(val) {
      this.page.current = val
      // 混合组件实现
      this.qryData()
    },
    onClickUpdate() {
      if (!_.isEmpty(this.selectRows) || this.selectRows.length === 1) {
        this.$emit('update')
      } else {
        this.$vp.toast('请选择一条需要编辑的记录', { type: 'warning' })
      }
    },
    onClickDel() {
      if (!_.isEmpty(this.selectRows) || this.selectRows.length === 1) {
        this.$emit('del')
      } else {
        this.$vp.toast('请选择一条需要删除的记录', { type: 'warning' })
      }
    },
    qryData() {
      this.$emit('qry-data')
    }
  },
  created() {
    console.log('components page', this.page)
  }
}
</script>

<style lang="scss" scoped>
  .mng-list-option-box {
    margin-bottom: 10px;
  }
</style>

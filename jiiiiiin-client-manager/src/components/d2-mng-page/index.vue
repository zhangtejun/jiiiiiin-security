<template>
  <d2-container class="mng-list">
    <slot name="search-box">
      <el-row>
        <el-col :span="23">
          <slot name="search-inner-box">
          </slot>
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
import { mapState } from 'vuex'

export default {
  name: 'd2-mng-page',
  props: {
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
    qryData() {
      console.log('components qry data')
      this.$emit('qry-data')
    }
  },
  created() {
    console.log('components page', this.page)
  }
}
</script>

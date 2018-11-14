<template>
    <d2-container>
        <zk-table
                ref="table"
                sum-text="sum"
                index-text="#"
                :data="data"
                empty-text="暂无数据"
                :columns="columns"
                :stripe="true"
                :border="true"
                :show-index="false"
                :tree-type="true"
                :is-fold="true"
                :expand-type="false"
                :selection-type="false">
            <template slot="icon" slot-scope="scope">
                <d2-icon v-if="scope.row.icon" :name="scope.row.icon"/>
                <span v-else>空</span>
            </template>
            <template slot="ismenu" slot-scope="scope">
                <el-tag size="mini" type="[scope.row.ismenu ? '': 'success']"> {{ scope.row.ismenu ? '菜单' : '按钮' }}</el-tag>
            </template>
            <template slot="status" slot-scope="scope">
                <el-tag size="mini" type="[scope.row.status ? '': 'success']"> {{ scope.row.ismenu ? '启用' : '停用' }}</el-tag>
            </template>
            <template slot="option" slot-scope="scope">
                <el-button type="primary" plain size="mini">新增</el-button>
                <el-button plain size="mini">修改</el-button>
                <!--根节点才可以删除-->
                <el-button type="danger" plain size="mini" v-if="!scope.row.children">删除</el-button>
            </template>
        </zk-table>
    </d2-container>
</template>

<script>
export default {
  name: 'resource',
  data() {
    return {
      data: [],
      columns: [
        {
          label: '名称',
          prop: 'name',
          minWidth: '50px'
        },
        {
          label: '图标',
          prop: 'icon',
          type: 'template',
          template: 'icon',
          width: '50px',
          align: 'center'
        },
        {
          label: '类型',
          prop: 'ismenu',
          type: 'template',
          template: 'ismenu',
          width: '70px',
          align: 'center'
        },
        {
          label: '排序',
          prop: 'num',
          width: '50px',
          align: 'center'
        },
        {
          label: '路由',
          prop: 'path'
        },
        {
          label: '接口',
          prop: 'url'
        },
        {
          label: '接口类型',
          prop: 'method',
          width: '75px',
          align: 'center'
        },
        {
          label: '状态',
          prop: 'status',
          type: 'template',
          template: 'status',
          width: '70px',
          align: 'center'
        },
        {
          label: '操作',
          width: '215px',
          type: 'template',
          template: 'option'
        }
      ]
    };
  },
  methods: {
    onClickAdd() {
      alert('add')
    }
  },
  created() {
    this.$vp.ajaxGet('resource')
      .then(res => {
        this.data = res
      })
  }
};
</script>

<style lang="scss" scoped>
    @import '~@/assets/style/public.scss';
    .icon-card {
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
        height: 50px;
        &:hover {
            .icon {
                transform: scale(1.1);
            }
            .icon-title {
                color: $color-text-main;
            }
        }
    }
    .icon {
        height: 30px;
        width: 30px;
        transition: all .3s;
        cursor: pointer;
    }
    .icon-title {
        font-size: 12px;
        margin-top: 3px;
        color: $color-text-normal;
    }
</style>

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
            <template slot="type" slot-scope="scope">
                <el-tag size="mini" type="[scope.row.type ? '': 'success']"> {{ scope.row.type ? '菜单' : '按钮' }}</el-tag>
            </template>
            <template slot="status" slot-scope="scope">
                <el-tag size="mini" type="[scope.row.status ? '': 'success']"> {{ scope.row.status ? '启用' : '停用' }}</el-tag>
            </template>
            <template slot="option" slot-scope="scope">
                <el-button type="primary" plain size="mini" @click="onClickAdd(scope.row)">新增</el-button>
                <el-button plain size="mini" @click="onClickUpdate(scope.row)">修改</el-button>
                <!--根节点才可以删除-->
                <el-button type="danger" plain size="mini" v-if="!scope.row.children" @click="onClickDel(scope.row)">删除</el-button>
            </template>
        </zk-table>

        <el-dialog
                :title="formMode === 'edit' ? '编辑' : '新增'"
                :visible.sync="dialogFormVisible">
            <el-form :model="form">
                <el-form-item label="类型" :label-width="formLabelWidth">
                    <el-radio v-model="form.type" label="1">菜单</el-radio>
                    <el-radio v-model="form.type" label="0">按钮</el-radio>
                </el-form-item>
                <el-form-item label="活动名称" :label-width="formLabelWidth">
                    <el-input v-model="form.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="活动区域" :label-width="formLabelWidth">
                    <el-select v-model="form.region" placeholder="请选择活动区域">
                        <el-option label="区域一" value="shanghai"></el-option>
                        <el-option label="区域二" value="beijing"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
            </div>
        </el-dialog>
    </d2-container>
</template>

<script>
export default {
  name: 'resource',
  data() {
    return {
      dialogFormVisible: false,
      formLabelWidth: '120px',
      formMode: 'add',
      form: {
        name: '',
        region: ''
      },
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
          prop: 'type',
          type: 'template',
          template: 'type',
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
    onClickAdd(pMenu) {
      console.log('pMenu', pMenu)
      this.formMode = 'add'
      this.dialogFormVisible = true
    },
    onClickUpdate() {
      this.formMode = 'edit'
      this.dialogFormVisible = true
    },
    onClickDel() {}
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

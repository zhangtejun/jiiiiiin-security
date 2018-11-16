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
                <el-tag size="mini" type="[scope.row.status === 1 ? '': 'success']"> {{ scope.row.status === 1 ? '启用' : '停用' }}</el-tag>
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
                :visible.sync="dialogFormVisible"
                width="70%"
                :modal="true">
            <el-form :model="form">
                <el-form-item label="类型" :label-width="formLabelWidth">
                    <el-radio-group v-model="form.type" @change="onChangeType">
                        <el-radio label="MENU">菜单</el-radio>
                        <el-radio label="BTN">按钮</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="名称" :label-width="formLabelWidth" :required="true">
                    <el-input v-model="form.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="父级菜单" :label-width="formLabelWidth">
                    <el-input v-model="form.pname" autocomplete="off" disabled="disabled"></el-input>
                </el-form-item>
                <el-form-item label="状态" :label-width="formLabelWidth">
                    <!--！注意label需要类型匹配-->
                    <el-switch v-model="form.status" :inactive-value="0" :active-value="1"></el-switch>
                </el-form-item>
                <el-form-item label="路由" :label-width="formLabelWidth">
                    <el-input v-model="form.path" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="接口" :label-width="formLabelWidth">
                    <el-input v-model="form.url" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="接口类型" :label-width="formLabelWidth">
                    <el-input v-model="form.method" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="同级排序" :label-width="formLabelWidth">
                    <el-input-number v-model="form.num" @change="onChangeNum" :min="1" :max="numMax" label="排序"></el-input-number>
                </el-form-item>
                <el-form-item label="图标" :label-width="formLabelWidth" v-show="visibleMenuFromField">
                    <el-select v-model="form.icon" filterable placeholder="请选择">
                        <el-option
                                v-for="item in iconOptions"
                                :key="item.key"
                                :value="item.value"
                                :disabled="item.disabled">
                            <div v-if="item.disabled">
                                <span style="float: left">{{ item.label }}</span>
                            </div>
                            <div v-else>
                                <d2-icon :name="item.value" style="width: 30px"/>&nbsp;&nbsp;&nbsp;&nbsp;{{ item.value }}
                            </div>
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="onSubmitForm()">确 定</el-button>
            </div>
        </el-dialog>
    </d2-container>
</template>

<script>
import icon from './data/index'
import _ from 'lodash'

export default {
  name: 'resource',
  data() {
    return {
      icon,
      visibleMenuFromField: true,
      dialogFormVisible: false,
      formLabelWidth: '80px',
      formMode: 'add',
      numMax: 1,
      form: {
        channel: '',
        icon: '',
        id: -1,
        levels: 1,
        method: '',
        name: '',
        num: 1,
        path: '',
        pid: 0,
        pname: '',
        status: 1,
        type: 'MENU',
        url: ''
      },
      formTempl: {
        channel: '',
        icon: '',
        id: -1,
        levels: 1,
        method: '',
        name: '',
        num: 1,
        path: '',
        pid: 0,
        pname: '',
        status: 1,
        type: 'MENU',
        url: ''
      },
      data: [],
      columns: [
        {
          label: '名称',
          prop: 'name',
          minWidth: '100px'
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
  computed: {
    iconOptions () {
      const res = []
      let idx = 0
      this.icon.forEach(item => {
        res.push({
          label: item.title,
          disabled: true
        })
        item.icon.forEach((icon) => {
          idx += 1
          res.push({
            key: idx,
            label: icon,
            value: icon
          })
        })
      })
      return res
    }
  },
  methods: {
    _preHandlerAddOrUpdate(mode, pMenu) {
      this.formMode = mode
      this.numMax = _.isEmpty(pMenu.children) ? 1 : pMenu.children.length + 1
      if (mode === 'add') {
        this.form.num = this.numMax
      }
      this.dialogFormVisible = true
    },
    onClickAdd(pMenu) {
      this.form = _.clone(this.formTempl)
      this.form.channel = pMenu.channel
      this.form.pid = pMenu.id
      this.form.pname = pMenu.name
      this.form.level = pMenu.level + 1
      this._preHandlerAddOrUpdate('add', pMenu)
    },
    onClickUpdate(pMenu) {
      this.form = pMenu
      this._preHandlerAddOrUpdate('edit', pMenu)
    },
    onChangeType(value) {
      this.visibleMenuFromField = value === 'MENU'
    },
    onChangeNum() {},
    onSubmitForm() {
      // if (this.formMode === 'add') {
      //   this.$vp.ajaxPost('resource', {
      //     params: this.form
      //   }).then(res => {
      //     // TODO 追加记录到 this.data
      //   });
      // }
      // else {
      //   // TODO 添加 ajax update
      // }
      this.dialogFormVisible = false
      console.log('submit', this.form)
    },
    onClickDel() {}
  },
  created() {
    console.log('created form', this.form)
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

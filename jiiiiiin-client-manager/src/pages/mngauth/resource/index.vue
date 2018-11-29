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
                :is-fold="false"
                :expand-type="false"
                :selection-type="false">
            <template slot="icon" slot-scope="scope">
                <d2-icon v-if="scope.row.icon" :name="scope.row.icon"/>
                <span v-else>空</span>
            </template>
            <template slot="type" slot-scope="scope">
                <el-tag v-if="scope.row.id !== '0'" size="mini" type="[scope.row.type ? '': 'success']"> {{ scope.row.type ? '菜单' : '按钮' }}</el-tag>
            </template>
            <template slot="status" slot-scope="scope">
                <el-switch v-if="scope.row.id !== '0'" v-model="scope.row.status" inactive-value="STOP" active-value="ENABLE" @change="onTableItemStatusChange(scope.row)"></el-switch>
                <!--<el-tag v-else size="mini" type="[scope.row.status === 1 ? '': 'success']"> {{ scope.row.status === 'ENABLE' ? '启用' : '停用' }}</el-tag>-->
            </template>
            <template slot="option" slot-scope="scope">
                <el-button type="primary" plain size="mini" @click="onClickAdd(scope.row)">新增</el-button>
                <el-button plain size="mini" @click="onClickUpdate(scope.row)" v-if="scope.row.id !== '0'">修改</el-button>
                <!--根节点才可以删除-->
                <el-button type="danger" plain size="mini" v-if="scope.row.id !== '0' && (!scope.row.children || (scope.row.children && scope.row.children.length === 0))" @click.stop.prevent="onClickDel(scope.row, $event)">删除</el-button>
            </template>
        </zk-table>

        <el-dialog
                :title="formMode === 'edit' ? '编辑' : '新增'"
                :visible.sync="dialogFormVisible"
                width="70%"
                :modal="true">
            <el-form :model="form" :rules="rules" ref="form" @submit.native.prevent>
                <d2-el-form-item label="类型" >
                    <el-radio-group v-model="form.type" @change="onChangeType" :disabled="formTypeRadioStatus">
                        <el-radio label="MENU">菜单</el-radio>
                        <el-radio label="BTN">按钮</el-radio>
                    </el-radio-group>
                </d2-el-form-item>
                <d2-el-form-item label="名称"  :required="true" prop="name">
                    <el-input v-model="form.name" autocomplete="off"></el-input>
                </d2-el-form-item>
                <d2-el-form-item label="父级菜单" >
                    <el-input v-model="form.pname" autocomplete="off" disabled="disabled"></el-input>
                </d2-el-form-item>
                <d2-el-form-item label="状态" >
                    <!--！注意label需要类型匹配-->
                    <el-switch v-model="form.status" inactive-value="STOP" active-value="ENABLE"></el-switch>
                </d2-el-form-item>
                <d2-el-form-item label="路由" >
                    <el-input v-model="form.path" autocomplete="off"></el-input>
                </d2-el-form-item>
                <d2-el-form-item label="接口" >
                    <el-input v-model="form.url" autocomplete="off"></el-input>
                </d2-el-form-item>
                <d2-el-form-item label="接口类型" >
                    <el-select v-model="form.method" filterable placeholder="请选择">
                        <el-option
                                v-for="item in methodOptions"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </d2-el-form-item>
                <d2-el-form-item label="同级排序" >
                    <el-input-number v-model="form.num" :min="1" :max="numMax" label="排序"></el-input-number>
                </d2-el-form-item>
                <d2-el-form-item label="图标"  v-show="visibleMenuFromField">
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
                </d2-el-form-item>
                <div class="dialog-form-submit-container">
                    <div class="dialog-form-submit-inner-container">
                        <el-button @click="dialogFormVisible = false">取 消</el-button>
                        <el-button type="primary" native-type="submit" @click="onSubmitForm">确 定</el-button>
                    </div>
                </div>
            </el-form>
        </el-dialog>
    </d2-container>
</template>

<script>
import icon from './data/index'
import _ from 'lodash'

export default {
  name: 'resource',
  components: {
    'd2-el-form-item': () => import('./components/d2-el-form-item')
  },
  data() {
    return {
      icon,
      // 默认为【内管】资源
      channel: '0',
      methodOptions: [
        {
          value: 'POST',
          label: '新增'
        },
        {
          value: 'GET',
          label: '查询'
        },
        {
          value: 'PUT',
          label: '更新'
        },
        {
          value: 'DELETE',
          label: '删除'
        }
      ],
      visibleMenuFromField: true,
      dialogFormVisible: false,
      formMode: 'add',
      formTypeRadioStatus: true,
      numMax: 1,
      selectNode: {},
      ruleForm: {
        name: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' },
          { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
        ]
      },
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
        status: 'ENABLE',
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
        status: 'ENABLE',
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
    onTableItemStatusChange(node) {
      this.$vp.ajaxPut('resource', {
        params: node
      }).then(res => {
        this.$vp.toast('修改成功')
      });
    },
    // 查找对应`item`节点对应的父节点等信息
    _findParentNode(item, treeData, pnode, callback) {
      const size = treeData.length
      for (let i = 0; i < size; i++) {
        const node = treeData[i]
        if (node.id === item.id) {
          if (_.isFunction(callback)) {
            this::callback(pnode, treeData, i, node)
          }
          return pnode
        } else {
          if (_.isEmpty(node.children)) {
            continue;
          } else {
            const temp = this._findParentNode(item, node.children, node, callback)
            if (!_.isEmpty(temp) || size === i) {
              return temp
            }
          }
        }
      }
    },
    _findNode(id, treeData, pnode, callback) {
      const size = treeData.length
      for (let i = 0; i < size; i++) {
        const node = treeData[i]
        if (node.id === id) {
          if (_.isFunction(callback)) {
            this::callback(node, pnode, treeData, i)
          }
          return pnode
        } else {
          if (_.isEmpty(node.children)) {
            continue;
          } else {
            const temp = this._findNode(id, node.children, node, callback)
            if (!_.isEmpty(temp) || size === i) {
              return temp
            }
          }
        }
      }
    },
    _preHandlerAddOrUpdate(mode, currentSelectNode) {
      this.formMode = mode
      // 新增currentSelectNode是父节点、更新currentSelectNode是当前节点
      this.selectNode = currentSelectNode
      // 业务：更新不可以调整资源的`type`
      this.formTypeRadioStatus = (mode !== 'add')
      this.dialogFormVisible = true
    },
    onClickAdd(node) {
      this.form = _.clone(this.formTempl)
      this.form.channel = node.channel
      this.form.pid = node.id
      this.form.pname = node.name
      this.form.levels = node.levels + 1
      this.numMax = _.isEmpty(node.children) ? 1 : node.children.length + 1
      this.form.num = this.numMax
      this._preHandlerAddOrUpdate('add', node)
    },
    onClickUpdate(node) {
      this.form = node
      const pnode = this._findParentNode(node, this.data)
      this.form.pname = pnode.name
      this.numMax = _.isEmpty(pnode.children) ? 1 : pnode.children.length + 1
      this._preHandlerAddOrUpdate('edit', node)
    },
    onChangeType(value) {
      this.visibleMenuFromField = value === 'MENU'
    },
    _onAddSuccessUpdateTreeData(res) {
      this._findNode(this.selectNode.id, this.data, null, (node, pnode, treeData, i) => {
        const addNodeNum = res.num;
        if (!_.isEmpty(node.children)) {
          const size = node.children.length;
          if (size > 1 && addNodeNum <= size) {
            // 需要检查父节点下面的子节点的排序
            let needUpdate = false;
            node.children.forEach((item, idx) => {
              const itemNum = item.num;
              if (itemNum === addNodeNum) {
                needUpdate = true;
              }
              if (needUpdate) {
                node.children[idx].num = itemNum + 1;
              }
            });
          }
        } else {
          this.$set(node, 'children', [])
        }
        node.children.splice(addNodeNum - 1, 0, _.clone(res))
      })
    },
    _copy(current, orig) {
      current.name = orig.name;
      current.icon = orig.icon;
      current.num = orig.num;
      current.path = orig.path;
      current.url = orig.url;
      current.method = orig.method;
      current.status = orig.status;
    },
    _updateParentChildrenNode(node) {
      this._findParentNode(node, this.data, null, (pnode, pnodeChildrenData, idx, node) => {
        this.$vp.ajaxGet(`resource/${this.channel}/${pnode.id}`)
          .then(res => {
            pnodeChildrenData.forEach((item, idx) => {
              this._copy(item, res[idx]);
            });
          });
      });
    },
    onSubmitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          let params = _.clone(this.form)
          delete params.pname
          if (this.formMode === 'add') {
            delete params.id
            this.$vp.ajaxPostJson('resource', {
              params
            }).then(res => {
              this._onAddSuccessUpdateTreeData(res)
            });
          } else {
            this.$vp.ajaxPut('resource', {
              params
            }).then(res => {
              // 修改当前节点信息
              this._updateParentChildrenNode(params)
            });
          }
          this.dialogFormVisible = false
        } else {
          return false;
        }
      });
    },
    /**
     * 查找`treeData`树下面`item`对应节点的父节点的`children`集合
     * @param item
     * @param treeData
     * @param callback
     * @returns {*}
     * @private
     */
    _findNodeParentChildren(item, treeData, callback) {
      const size = treeData.length
      for (let i = 0; i < size; i++) {
        const node = treeData[i]
        if (node.id === item.id) {
          if (_.isFunction(callback)) {
            this::callback(treeData, i, node)
          }
          return treeData
        } else {
          if (_.isEmpty(node.children)) {
            continue;
          } else {
            const temp = this._findNodeParentChildren(item, node.children, callback)
            if (!_.isEmpty(temp) || size === i) {
              return temp
            }
          }
        }
      }
    },
    onClickDel(item, event) {
      this.$vp.ajaxDel(`resource/${this.channel}/${item.id}`)
        .then(res => {
          if (res) {
            this._findNodeParentChildren(item, this.data, (pnodeChildrenData, idx) => {
              pnodeChildrenData.splice(idx, 1)
              // 查看父节点是否还有子节点，如果有需要进行`排序`字段修改，**在前台修改之前后台数据库已经被更新**
              if (_.isArray(pnodeChildrenData) && pnodeChildrenData.length > 0) {
                const delNum = item.num;
                const size = pnodeChildrenData.length;
                if (size > 0) {
                  for (let i = 0; i < size; i++) {
                    const node = pnodeChildrenData[i];
                    if (node.num > delNum) {
                      node.num = node.num - 1;
                    }
                  }
                }
              }
            })
            this.$vp.toast(`成功删除【${item.name}】`)
          }
        })
    }
  },
  created() {
    this.$vp.ajaxGet(`resource/${this.channel}`)
      .then(res => { this.data = res })
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

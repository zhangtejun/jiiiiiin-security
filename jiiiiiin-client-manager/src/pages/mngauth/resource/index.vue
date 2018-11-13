<template>
    <d2-container>
        <zk-table
                ref="table"
                sum-text="sum"
                index-text="#"
                :data="data"
                :columns="columns"
                :stripe="true"
                :border="true"
                :show-index="false"
                :tree-type="true"
                :is-fold="true"
                :expand-type="true"
                :selection-type="true">
            <template slot="$expand" slot-scope="scope">
                {{ `My name is ${scope.row.name}` }}
            </template>
            <template slot="likes" slot-scope="scope">
                {{ scope.row.likes.join(',') }}
            </template>
        </zk-table>
    </d2-container>
</template>

<script>
export default {
  name: 'resource',
  data() {
    return {
      props: {
        stripe: false,
        border: false,
        showHeader: true,
        showSummary: false,
        showRowHover: true,
        showIndex: false,
        treeType: true,
        isFold: true,
        expandType: false,
        selectionType: false
      },
      data: [
        {
          name: 'Jack',
          sex: 'male',
          likes: ['football', 'basketball'],
          score: 10,
          children: [
            {
              name: 'Ashley',
              sex: 'female',
              likes: ['football', 'basketball'],
              score: 20,
              children: [
                {
                  name: 'Ashley',
                  sex: 'female',
                  likes: ['football', 'basketball'],
                  score: 20
                },
                {
                  name: 'Taki',
                  sex: 'male',
                  likes: ['football', 'basketball'],
                  score: 10,
                  children: [
                    {
                      name: 'Ashley',
                      sex: 'female',
                      likes: ['football', 'basketball'],
                      score: 20
                    },
                    {
                      name: 'Taki',
                      sex: 'male',
                      likes: ['football', 'basketball'],
                      score: 10,
                      children: [
                        {
                          name: 'Ashley',
                          sex: 'female',
                          likes: ['football', 'basketball'],
                          score: 20
                        },
                        {
                          name: 'Taki',
                          sex: 'male',
                          likes: ['football', 'basketball'],
                          score: 10
                        }
                      ]
                    }
                  ]
                }
              ]
            },
            {
              name: 'Taki',
              sex: 'male',
              likes: ['football', 'basketball'],
              score: 10
            }
          ]
        },
        {
          name: 'Tom',
          sex: 'male',
          likes: ['football', 'basketball'],
          score: 20,
          children: [
            {
              name: 'Ashley',
              sex: 'female',
              likes: ['football', 'basketball'],
              score: 20,
              children: [
                {
                  name: 'Ashley',
                  sex: 'female',
                  likes: ['football', 'basketball'],
                  score: 20
                },
                {
                  name: 'Taki',
                  sex: 'male',
                  likes: ['football', 'basketball'],
                  score: 10
                }
              ]
            },
            {
              name: 'Taki',
              sex: 'male',
              likes: ['football', 'basketball'],
              score: 10,
              children: [
                {
                  name: 'Ashley',
                  sex: 'female',
                  likes: ['football', 'basketball'],
                  score: 20
                },
                {
                  name: 'Taki',
                  sex: 'male',
                  likes: ['football', 'basketball'],
                  score: 10
                }
              ]
            }
          ]
        },
        {
          name: 'Tom',
          sex: 'male',
          likes: ['football', 'basketball'],
          score: 20
        },
        {
          name: 'Tom',
          sex: 'male',
          likes: ['football', 'basketball'],
          score: 20,
          children: [
            {
              name: 'Ashley',
              sex: 'female',
              likes: ['football', 'basketball'],
              score: 20
            },
            {
              name: 'Taki',
              sex: 'male',
              likes: ['football', 'basketball'],
              score: 10
            }
          ]
        }
      ],
      columns: [
        {
          label: 'name',
          prop: 'name',
          width: '400px'
        },
        {
          label: 'sex',
          prop: 'sex',
          minWidth: '50px'
        },
        {
          label: 'score',
          prop: 'score'
        },
        {
          label: 'likes',
          prop: 'likes',
          minWidth: '200px',
          type: 'template',
          template: 'likes'
        }
      ]
    };
  },
  computed: {
    propList() {
      return Object.keys(this.props).map(item => ({
        name: item
      }));
    }
  },
  methods: {
    onClickAdd() {
      alert('add')
    }
  },
  created() {
    // this.$vp.ajaxGet('sys/resource')
    //   .then(res => {
    //     console.log(res)
    //   })
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

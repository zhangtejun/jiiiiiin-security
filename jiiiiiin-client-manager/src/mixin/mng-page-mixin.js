import { mapMutations } from 'vuex'

export default {
  data () {
    return {
    }
  },
  methods: {
    ...mapMutations('d2admin', {
      setInitAjaxNum: 'page/setInitAjaxNum'
    })
  }
}

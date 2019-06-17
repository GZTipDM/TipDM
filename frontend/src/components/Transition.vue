<!--页面切换过渡页-->
<template>
  <div :style="mComStyle" class="trans_content" v-loading="true" element-loading-text="拼命加载中...">
  </div>
</template>

<script>
  export default {
    name: 'Transition',
    data: function () {
      return {
        mComStyle: {
          width: "",
          height: ""
        }
      }
    },
    computed: {
      curMenuItem: function () {
        return this.$store.state.global.curMenuItem;
      }
    },
    created: function () {
      var w = document.documentElement.clientWidth;
      var h = document.documentElement.clientHeight;
      this.mComStyle.width = w - 60 + 'px';
      this.mComStyle.height = h - 55 + 'px';
      if (this.curMenuItem.linkTo) {
        this.$router.push({path: this.curMenuItem.linkTo});
      } else {
        this.$store.state.global.curMenuItem = this.$store.state.global.navMenu[0];
        this.$router.push({path: '/'});
      }
    }
  }
</script>

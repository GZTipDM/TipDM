<template>
  <li :class="{on:selectItem.on}" @click="menuClick"><a href="javascript:void(0)"><i class="fa" :class="item.class"></i><span>{{item.name}}</span></a>
  </li>
</template>
<script>
  export default {
    name: 'MenuBarItem',
    props: ["item"],
    data: function () {
      return {
        selectItem: {
          on: false
        }
      }
    },
    computed: {
      curMenuItem: function () {
        return this.$store.state.global.curMenuItem;
      }
    },
    watch: {
      curMenuItem: function (val) {
        this.changeClass(val);
      }
    },
    created: function () {
      this.changeClass(this.curMenuItem);
    },
    methods: {
      changeClass: function (val) {
        if (this.item.id == val.id) {
          this.selectItem.on = true;
        } else {
          this.selectItem.on = false;
        }
      },
      menuClick: function () {
        this.$store.state.global.curMenuItem = this.item;
        this.$router.push({path: 'trans'});
      }
    }
  }
</script>

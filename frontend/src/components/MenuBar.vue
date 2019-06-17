<!--主页左侧菜单栏-->
<template>
  <div class="mSide">
    <ul class="msList">
      <template v-for="item in menuItems">
        <MenuBarItem v-bind:item="item" v-if="checkPermission(item.permission)"></MenuBarItem>
      </template>
    </ul>
  </div>
</template>
<style>
</style>
<script>
  import MenuBarItem from './menu_bar/MenuBarItem.vue';
  export default {
    name: 'MenuBar',
    components: {
      MenuBarItem
    },
    computed: {
      permission: function() {
        return this.$store.state.global.permission;
      },
      menuItems: function() {
        return this.$store.state.global.navMenu;
      }
    },
    methods: {
      checkPermission(permission) {
        if(this.$.inArray(permission,this.permission.data)>-1) {
          return true;
        } else {
          return false;
        }
      }
    }
  }
</script>

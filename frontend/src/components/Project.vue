<!--工程页-->
<template>
  <div id="mCom" class="mCom" @click="clearPop" v-loading="showLoading" element-loading-text="拼命加载中...">
    <div id="sideTree" class="sideTree">
      <!--左侧菜单栏-->
      <MenuNav/>
    </div>
    <router-view></router-view>
  </div>
</template>
<script>
//  require("../assets/js/tip");
  import MenuNav from './project/MenuNav.vue'
  var interval; //定时器
  export default{
    name: 'Project',
    beforeMount: function() {//挂载前调用
      this.$store.dispatch("checkToken");
      var that = this;
      that.showLoading = false;
    },
    data: function() {
      return {
        dragStyle: {//拖拽属性
          conR:0
        },
        showLoading: true
      }
    },
    computed: {
      flowData: function() {
        return this.$store.state.project.flowData;
      },
      propsContentStyle: function() {
        return this.flowData.propsContentStyle;
      },
      username() {
        return this.$store.state.global.username;
      }
    },
    created: function() {
      this.clearPop();//清除右键菜单，如果存在
      var routerPath = this.$router.currentRoute.path;
      var curMenu = this.$store.state.global.navMenu.filter(node => node.linkTo == routerPath);
      this.$store.state.global.curMenuItem = curMenu[0];
      //页面加载完成创建定时器
      this.localFlowSave();
    },
    beforeDestroy: function() {
      clearInterval(interval);//先清理定时器
      this.localStorageSave(true);//提交前本地保存一次
    },
    components: {
      MenuNav
    },
    methods: {
      localFlowSave() {
        var that = this;
        interval = setInterval(function() {
          //自动保存过程中遇到保存流程时，跳过保存
          if(!that.flowData.projectChange) {
            that.localStorageSave(false);
          }
        },5000);
      },
      localStorageSave(bool) {
        var curProjectItem = this.flowData.currentProjectDetail;
        if(curProjectItem.id) {
          var json = {
            accessToken: localStorage.accessToken,
            projectId: curProjectItem.id,
            content: this.dealFlowJson()
          }
          localStorage.setItem(this.username+"_"+curProjectItem.id,JSON.stringify(json));
        }
        if(bool) {
          //提交流程到服务端
          this.$store.dispatch("sendLocalStorageToServer",{
            projectId: curProjectItem.id
          });
        }
      },
      clearPop: function() {//清理右键的弹框
        this.flowData.projectContextMenuMsg.display = "none";
        this.flowData.nodeRightContentMsg.display = "none";
        this.$store.state.project.modelMenu.contentMenuMessage.display = "none";
      },
      dealFlowJson: function() {
        var data={};
        var nodes = this.flowData.nodes;
        var links = this.flowData.links;
        data["style"] = this.flowData.contentStyle;
        data["nodes"] = nodes;
        data["links"] = links;
        return JSON.stringify(data);
      }
    }
  }
</script>

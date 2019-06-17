<!--系统组件页-->
<template>
  <div id="mCom" class="mCom" @click="clearPop" v-loading="showLoading"
       element-loading-text="拼命加载中...">
    <div class="sideTree">
      <div id="folder" style="padding-right: 2px;">
        <el-popover ref="popover5" placement="right" width="160" v-model="visible2">
          <el-input v-model="projectName" placeholder="添加分类"></el-input>
          <p>&nbsp;</p>
          <div style="text-align: right; margin: 0">
            <el-button size="mini" type="text" @click="visible2 = false">取消</el-button>
            <el-button type="primary" size="mini" @click="addRootClass">确定</el-button>
          </div>
        </el-popover>
        <a class="addP">系统组件</a>
        <el-button class="btn_add" type="text" title="添加分类" v-popover:popover5><i class="fa fa-plus-circle"></i>
        </el-button>
      </div>
      <div v-loading="componentsDeals.treeMenuLoading" class="subTree" id="gc" style="overflow-y:auto">
        <!--组件树形菜单-->
        <div class="tree-menu">
          <ul v-for="(menuItem,index) in componentsList[0].children" :key="menuItem.id">
            <SysCompTree :item="menuItem" :index="index" :parentItem="componentsList[0]"></SysCompTree>
          </ul>
        </div>
      </div>
    </div>
    <div class="drawingWork pComponent">
        <div id="cManage" class="h100p">
          <component v-bind:is="currentView">
            <!-- 组件在 vm.currentview 变化时改变！ -->
          </component>
        </div>
    </div>
    <RightMenu></RightMenu>
  </div>
</template>
<script>
  import SysCompTree from './system_component/SysCompTree.vue';
  import SysCompItem from './system_component/SysCompItem.vue';
  import ModuleDescription from './system_component/ModuleDescription.vue';
  import RightMenu from './system_component/sys_comp_tree/RightMenu.vue'

  export default {
    name: 'SystemComponent',
    beforeMount: function () {//挂载前调用
      this.$store.dispatch("checkToken");
      this.showLoading = false;
    },
    data: function () {
      return {
        projectName: "",
        visible2: false,
        showLoading: true
      }
    },
    created: function () {
      //初始化加载状态为正在加载
      this.componentsDeals.treeMenuLoading = true;
      //获取通用组件树树形根目录
      this.$store.dispatch("getMenuComponentsList", 0);
      this.clearPop();
      this.componentsDeals.currentView = "ModuleDescription";//组件重绘切换视图

      var routerPath = this.$router.currentRoute.path;
      var curMenu = this.$store.state.global.navMenu.filter(node => node.linkTo == routerPath);
      this.$store.state.global.curMenuItem = curMenu[0];
    },
    computed: {
      componentsList: function () {
        return this.$store.state.systemComponent.menuComponentsTree;
      },
      componentsDeals: function () {
        return this.$store.state.systemComponent.componentsDeals;
      },
      currentView: function () {
        return this.componentsDeals.currentView;
      },
      permission: function () {
        return this.$store.state.global.permission;
      }
    },
    components: {
      SysCompTree,
      SysCompItem,
      RightMenu,
      ModuleDescription
    },
    methods: {
      clearPop: function () {//清理右键的弹框
        this.componentsDeals.componentsContextMenuMsg.display = "none"
      },
      addRootClass: function () {//添加根分类
        var that = this;
        if (this.projectName == "" || this.projectName.trim().length == 0) {
          this.$notify({
            title: "提示",
            message: "添加分类名称不能为空",
            type: "warning"
          });
          return;
        }
        var data = {};
        data["name"] = this.projectName;
        data["parentId"] = 0;
        this.$.ajax({
          url: that.$store.state.global.httpServer + "/api/cat",
          method: "post",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          data: JSON.stringify(data),
          success: function (data) {
            if (data.status == "SUCCESS") {
              that.$notify({
                title: '成功',
                message: data.message,
                type: 'success'
              });
              that.projectName = "";//置空名称
              //获取添加子目录
              that.$store.dispatch("getMenuComponentsList", 0);
              that.visible2 = false;//隐藏当前pop
            } else if (data.status == "FAIL") {
              that.$notify.error({
                title: '失败',
                message: data.message,
              });
            }
          },
          error: function (response) {
            that.$store.commit("dealRequestError", response);
          }
        });
      }
    }
  }
</script>

<!--首页-->
<template>
  <div id="mCom" class="mCom" v-loading="showLoading" element-loading-text="拼命加载中...">
    <div class="homeMain">
      <el-row :gutter="20" id="indDiv1">
        <el-col :span="12">
          <el-card id="sq" class="tBlock">
            <div slot="header" class="hd clearfix">
              <h3><a href="#"><i class="fa fa-comments-o"></i>TipDM数据挖掘</a></h3>
            </div>
            <ul class="oLink">
              <li v-for="item in main.bbsList" :key="item.id"><a :href="[item.link]" target="_blank">{{item.name}}</a>
              </li>
            </ul>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card id="rm" class="tBlock">
            <div slot="header" class="hd clearfix">
              <h3><a href="#"><i class="fa fa-envira"></i>解决方案</a></h3>
            </div>
            <ul class="oLink">
              <li v-for="item in main.documents" :key="item.id"><a :href="[item.link]" target="_blank">{{item.name}}</a>
              </li>
            </ul>
          </el-card>
        </el-col>
      </el-row>
      <el-card id="sl" class="tBlock">
        <div slot="header" class="hd clearfix">
          <el-input placeholder="输入模版名称过滤" v-model="filterStr" suffix-icon="el-icon-search" class="search" :maxlength="30">
          </el-input>
          <h3><a href="#"><i class="fa fa-codepen"></i>模板</a></h3>
        </div>
        <el-row :gutter="20" id="" class="pList">
          <el-col :span="4">
            <a href="javascript:void(0)" @click="fastAddProject" class="addProject">
              <div class="apCom"><i class="fa fa-plus"></i><span>新建工程</span></div>
            </a>
          </el-col>
          <el-col :span="4" v-for="item in templateList" :key="item.id">
            <MainTemplateItem :item="item"></MainTemplateItem>
          </el-col>
        </el-row>
      </el-card>
      <div class="moreList"><a href="javascript:void(0)" @click="loadMore" title="更多模板" class="animated"><i
        class="fa fa-angle-double-down"></i></a></div>
    </div>
    <CopyTemplateAsProject></CopyTemplateAsProject>
    <FastCreateProjectDialog></FastCreateProjectDialog>
  </div>
</template>
<style scoped>
  .el-card {
    border: 0;
  }
</style>
<script>
  import FastCreateProjectDialog from './project/project_menu/project_tree/FastCreateProjectDialog.vue';
  import CopyTemplateAsProject from './project/project_menu/project_tree/CopyTemplateAsProject.vue';
  import MainTemplateItem from './main/MainTemplateItem.vue'

  export default {
    name: "Main",
    data: function () {
      return {
        // mComStyle: {//组件绘制时动态设置容器的长宽
        //   width: "",
        //   height: ""
        // },
        filterStr: "",
        showLoading: true
      }
    },
    beforeMount: function () {//挂载前调用
      this.$store.dispatch("checkToken");
      this.showLoading = false;
    },
    created: function () {
      // var w = document.documentElement.clientWidth;
      // var h = document.documentElement.clientHeight;
      // this.mComStyle.width = w - 90 + 'px';
      // this.mComStyle.height = h - 87 + 'px';
      this.$store.dispatch("getTemplateList",0); //获取模板列表
      this.$store.dispatch("getMenuProjectList",0); //获取工程根节点
      this.$store.dispatch("getBBSList");//获取社区列表
      this.$store.dispatch("getDocumentsList");//获取入门列表
      var routerPath = this.$router.currentRoute.path;
      var curMenu = this.$store.state.global.navMenu.filter(node => node.linkTo == routerPath);
      this.$store.state.global.curMenuItem = curMenu[0];
    },
    computed: {
      main: function () {
        return this.$store.state.main;
      },
      templateList: function () {//模板列表
        var list = this.main.templateList;
        var result = list.filter(node => node.project.name.indexOf(this.filterStr) > -1);
        return result;
      },
      flowData: function() {
        return this.$store.state.project.flowData;
      },
      rightContentTargetItem: function() {
        return this.flowData.rightContentTargetItem;
      }
    },
    components: {
      MainTemplateItem,
      FastCreateProjectDialog,
      CopyTemplateAsProject
    },
    methods: {
      fastAddProject() {//快速新建工程
        if(this.flowData.isRunning) {
          this.$notify({
            type: "warning",
            message: "工程运行中，请稍后再试！"
          });
          return;
        }
        //设置添加位置
        this.flowData.rightContentTargetItem = this.$store.state.project.projectMenu[0];
        //显示弹框
        this.main.showDetail.showFastCreateProjectDialog = true;
      },
      loadMore() {//加载更多
        this.$store.dispatch("getTemplateList",1);
      }
    }
  }
</script>

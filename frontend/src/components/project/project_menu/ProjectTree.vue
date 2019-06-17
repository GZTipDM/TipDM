<template>
  <li>
    <a href="javascript:void(0)" @contextmenu="rightClick($event)" :title="item.name">
      <span v-if="!item.leaf" @click="toggle">
        <i :class="[item.open ? (item.loading?'el-icon-loading':'el-icon-caret-bottom'): 'el-icon-caret-right']"></i>
        <i><span>{{item.name}}</span></i>
      </span>
      <span v-else-if="item.leaf" @click="checkRunning">
        <i class="fa fa-flask"></i>
        <i :style="itemStyle">{{ item.name}}</i>
      </span>
    </a>
    <ul v-show="item.open" v-if="!item.leaf">
      <ProjectTree v-for="childItem in item.children" :item="childItem" :parentItem="item"
                   :key="childItem.id"></ProjectTree>
    </ul>
  </li>
</template>
<script>
  export default {
    name: 'ProjectTree',
    props: ['item', 'parentItem'],
    data: function () {
      return {
        itemStyle: {
          color: ""
        }
      }
    },
    computed: {
      flowData: function () {
        return this.$store.state.project.flowData;
      },
      currentProjectItem: function () {
        return this.flowData.currentProjectItem;
      },
      username() {
        return this.$store.state.global.username;
      }
    },
    watch: {
      currentProjectItem: function (val) {
        this.changeStyle(val);
      }
    },
    created: function () {
      this.changeStyle(this.currentProjectItem);
    },
    methods: {
      toggle: function () {//文件夹的展开关闭
        var that = this;
        if (!this.item.leaf) {
          this.item.open = !this.item.open;
          if (this.item.children.length == 0) {//如果该节点已经存在children节点，则不再重复获取
            this.item.loading = true;
            this.flowData.currentParentItem = this.item;
            //获取添加子树
            this.$store.dispatch("getMenuProjectList", 3);
          }
        }
      },
      changeStyle: function (val) {
        if (this.item.id == val.id) {
          this.itemStyle.color = "#e29012";
        } else {
          this.itemStyle.color = "";
        }
      },
      checkRunning: function () {
        if (!this.flowData.isRunning) {
          this.editComponent();
        } else {
          this.$notify({
            type: "warning",
            message: "工程运行中，请稍后再试！"
          });
        }
      },
      editComponent: function () {//工程项点击事件
        if (this.flowData.projectChange) {
          this.$message({
            type: "warning",
            message: "操作频繁，数据准备中，请稍后再试！"
          });
        } else {
          //设置组件切换状态为true
          this.flowData.projectChange = true;
          //切换前对工程进行提交保存
          this.localStorageSave(true);

          this.flowData.propsContentHeight = this.$(".drawingWork .eProperty").height();
          //设置当前项的目标父类集合
          this.flowData.currentParentItem = this.parentItem;
          //设置当前选中文档树
          this.flowData.currentProjectItem = this.item;
          //设置当前操作工程信息
          this.flowData.currentProjectDetail = this.item.project;

          var localJSON = localStorage.getItem(this.username + "_" + this.item.project.id)
          if (localJSON) {
            this.flowData.projectChange = false;
            this.$confirm('检测到最后一次修改未提交, 是否从本地加载?', '提示', {
              confirmButtonText: '是',
              cancelButtonText: '否',
              type: 'warning'
            }).then(() => {
              this.$srore.dispatch("fillCurrentJson", {
                json: localJSON
              });
            }).catch(() => {
              //从服务端获取工程流程数据并填充
              this.$store.dispatch("getCurrentFlowJson");
            });

          } else {
            //从服务端获取工程流程数据并填充
            this.$store.dispatch("getCurrentFlowJson");
          }
          this.$store.commit("updateMenuProjectSelectComponent", "ProjectContentMsg");
        }
      },
      rightClick: function (event) {
        //右键操作前加载子目录
        this.item.loading = true;
        this.flowData.currentParentItem = this.item;
        //获取添加子树
        this.$store.dispatch("getMenuProjectList", 3);
        //阻止默认菜单事件
        if (document.all) {
          window.event.returnValue = false;
        } else {
          event.preventDefault()
        }
        if (this.flowData.isRunning) {
          this.$notify({
            type: "warning",
            message: "工程运行中，请稍后再试！"
          });
          return;
        }
        this.localStorageSave(true);//右键时也要对工程提交保存
        this.flowData.rightContentTargetItem = this.item;
        this.flowData.rightContentTargetProject = this.item.project;
        //设置父类项
        this.flowData.currentParentItem = this.parentItem;
        //更新content_menu 坐标位置
        var dom = document.getElementsByClassName("mCom")[0];//获取容器dom
        var centerDomOffset = dom.getBoundingClientRect();
        var contentHeight = 287;//file 196  root 118
        if (this.item.id == 0) {
          contentHeight = 118;
        } else if (!this.item.leaf) {
          contentHeight = 209;
        } else if (this.item.leaf) {
          contentHeight = 287;
        }
        this.flowData.projectContextMenuMsg = {
          left: event.clientX - centerDomOffset.left,
          top: ((event.clientY - centerDomOffset.top) + contentHeight + 52) > centerDomOffset.bottom ? ((centerDomOffset.bottom - 52 - contentHeight) > 0 ? (centerDomOffset.bottom - 52 - contentHeight) : 0) : (event.clientY - centerDomOffset.top),
          display: 'block'
        }
      },
      deepCopy: function (source) {
        var result;
        (source instanceof Array) ? (result = []) : (typeof(source) === "object") ? (source === null ? (result = "") : (result = {})) : (result = source);
        for (var key in source) {
          result[key] = (typeof source[key] === 'object') ? this.deepCopy(source[key]) : source[key];
        }
        return result;
      },
      localStorageSave(bool) {
        var curProjectItem = this.flowData.currentProjectDetail;
        if (curProjectItem.id) {
          var json = {
            accessToken: localStorage.accessToken,
            projectId: curProjectItem.id,
            content: this.dealFlowJson()
          }
          localStorage.setItem(this.username + "_" + curProjectItem.id, JSON.stringify(json));
          console.log("localStorage save ok..." + curProjectItem.id);
        }
        if (bool) {
          //提交流程到服务端
          this.$store.dispatch("sendLocalStorageToServer", {
            projectId: curProjectItem.id
          });
        }
      },
      dealFlowJson: function () {
        var data = {};
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

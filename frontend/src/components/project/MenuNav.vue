<!--工程菜单块，我的工程模块-->
<template>
  <div class="h100p">
    <dl id="treeSor" class="treeSor" :style="{height:menuDeals.topContentHeight+'px'}">
      <div id="folder">
        <el-popover ref="popover5" placement="top" width="160" v-model="visible2">
          <el-input v-model="projectName" placeholder="添加根目录名称"></el-input>
          <p>&nbsp;</p>
          <div style="text-align: right; margin: 0">
            <el-button size="mini" type="text" @click="visible2 = false">取消</el-button>
            <el-button type="primary" size="mini" @click="addRootDirectory">确定</el-button>
          </div>
        </el-popover>
        <el-button type="text" class="addP" title="新增工程" @click="createProject"><i class="fa fa-plus-circle"></i>
        </el-button>
      </div>
      <dt class="on"><a>工程</a></dt>
      <dd id="gcSub">
        <div style="width:160px;text-align:center" v-if="projectList.length == 0">
          <i class="el-icon-loading"></i>
        </div>
        <div class="tree-menu" id="projctMenu" v-else>
          <ul v-for="menuItem in projectList">
            <ProjectTree :item="menuItem" :parentItem="projectList[0]"></ProjectTree>
          </ul>
        </div>
      </dd>
    </dl>
    <div id="udDrag" @mousedown="dragMouseDown($event)" class="udDrag"><i id="upIcon" class="fa fa-angle-up"></i><i
      id="dwIcon" class="fa fa-angle-down"></i></div>
    <dl id="treeSor2" class="treeSor" :style="{height:menuDeals.footContentHeight+'px'}">
      <dt @click="titleClick(0)" :class="displayStyle.showIndex?'on':''"><a>组件</a></dt>
      <dd :style="{display:displayStyle.showIndex?'block':'none' }">
        <div class="subTree" id="zj">
          <div class="tree-menu">
            <el-input class="zjFiltrate" size="mini" v-model="filterKey" placeholder="输入内容过滤"
                      prefix-icon="el-icon-search"></el-input>
            <el-tree
              ref="tree2"
              :data="systemComponents"
              node-key="id"
              default-expand-all
              @node-drag-start="handleDragStart"
              @node-drag-end="handleDragEnd"
              :filter-node-method="filterNode"
              draggable
              accordion
              :indent="12"
              :allow-drop="allowDrop"
              :allow-drag="allowDrag">
              <span class="define_content_item" slot-scope="{ node, data }">
                <span :style="data.enabled?'':unEnabledClass">
                  <i v-if="data.component" class="fa" :class="data.iconPath?data.iconPath:'fa-cubes'"></i>
                  {{ data.label }}
                </span>
              </span>
            </el-tree>
          </div>
        </div>
      </dd>
    </dl>
  </div>
</template>
<script>
  import ProjectTree from './project_menu/ProjectTree.vue';

  var $d3 = require("d3");
  var win;
  export default {
    name: 'MenuNav',
    data: function () {
      return {
        projectName: "",
        visible2: false,
        dragStyle: {//拖拽属性
          src_posi_Y: 0,//起始点y
          dest_posi_Y: 0,//结束点y
          move_Y: 0,//移动距离-像素
          leftContentHeight: 0,//模块高度
          leftContentTop: 0,//模块上部距离
        },
        unEnabledClass: {
          color: "#c0c4cc"
        },
        displayStyle: {
          showIndex: true
        },
        filterKey: "",
        defaultProps: {
          children: 'children',
          label: 'label'
        }
      }
    },
    computed: {
      project() {
        return this.$store.state.project;
      },
      model() {
        return this.$store.state.model;
      },
      projectList: function () { //工程列表数据
        return this.project.projectMenu;
      },
      menuDeals: function () {//菜单交互集
        return this.project.menuDeals;
      },
      flowData: function () {
        return this.project.flowData;
      },
      systemComponents() {
        return this.project.systemComponents;
      },
      curProject() {
        return this.flowData.currentProjectItem;
      }
    },
    watch: {
      filterKey(val) {
        this.$refs.tree2.filter(val);
      },
      curProject(val) {
        this.$refs.tree2.filter(this.filterKey);
      }
    },
    created: function () {
      console.log("created........");
      //获取工程树根目录
      this.$store.dispatch("getMenuProjectList", 0);
      //获取系统组件列表
      this.$store.dispatch("getSysComponentData").then((res) => {
        setTimeout(() => {
          this.$refs.tree2.filter(this.filterKey);
        },500);
      });
    },
    mounted() {
      this.setDefaultHeight();
      window.onresize = () => {
        this.setDefaultHeight();
      }
    },
    methods: {
      handleDragStart: function (node, event) {
        console.log("dragStart...", node);
        this.$store.state.project.flowData.ifAddDom = true;//设置状态为添加
        this.$store.state.project.flowData.addMenuItem = this.deepCopy(node.data);//设置当前拖动的菜单项
        this.$store.state.project.flowData.pointLayer = {//初始化偏移
          x: 90,
          y: 19
        };
      },
      handleDragEnd: function (event) {
        console.log("dragEnd...");
        this.$store.state.project.flowData.ifAddDom = false;
      },
      setDefaultHeight() {
        let maxHeight = document.getElementById("sideTree").getBoundingClientRect().height;
        this.menuDeals.topContentHeight = this.menuDeals.footContentHeight = (maxHeight - 24) / 2;
      },
      dragMouseDown(event) {
        let leftContent = document.getElementById("sideTree").getBoundingClientRect();
        this.dragStyle.leftContentHeight = leftContent.height;
        this.dragStyle.leftContentTop = leftContent.top;
        win = $d3.select(window).on("mousemove", this.dragMouseMove).on("mouseup", this.dragMouseUp);
        event.preventDefault();
      },
      dragMouseMove() {
        let curY = $d3.event.pageY;
        let topH = curY - this.dragStyle.leftContentTop;
        let bottomH = this.dragStyle.leftContentHeight - topH;
        let h1 = topH - 12;
        let h2 = bottomH - 12;
        let maxHeight = this.dragStyle.leftContentHeight - 172;
        this.menuDeals.topContentHeight = h1 > 121 ? (h1 > maxHeight ? maxHeight : h1) : 121;
        this.menuDeals.footContentHeight = h2 > 148 ? (h2 > maxHeight ? maxHeight : h2) : 148;
      },
      dragMouseUp() {
        win.on("mousemove", null).on("mouseup", null);
      },
      titleClick: function (index) {//组件模型点击事件
        if (index == 0) {
          this.displayStyle.showIndex = true;
          this.displayStyle.display = "block";
        } else if (index == 1) {
          this.displayStyle.showIndex = false;
        }
      },
      addRootDirectory: function () {//添加根目录
        var that = this;
        if (this.projectName == "" || this.projectName.trim().length == 0) {
          this.$notify({
            title: "提示",
            message: "添加目录名称不能为空",
            type: "warning"
          });
          return;
        }
        var data = {};
        data["name"] = this.projectName;
        data["parentId"] = 0; //添加根目录
        this.$.ajax({
          url: that.$store.state.global.httpServer + "/api/document/",
          method: "post",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          data: JSON.stringify(data),
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          success: function (data) {
            if (data.status == "SUCCESS") {
              that.$notify({
                title: '成功',
                message: data.message,
                type: 'success'
              });
              that.projectName = "";//清空输入框
              that.visible2 = false;//取消弹出框
              that.$store.dispatch("getMenuProjectList", 0);
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
      },
      createProject: function () {//添加工程叶子节点
        if (!this.flowData.isRunning) {
          //设置重载父类
          this.flowData.rightContentTargetItem = this.projectList[0];
          //设置父类项
          this.flowData.currentParentItem = this.projectList[0];
          this.$store.state.project.projectMenuModel.showDetail.showProjectDialog = true;
        } else {
          this.$notify({
            type: "warning",
            message: "工程运行中，请稍后再试！"
          });
        }
      },
      deepCopy: function (source) {//深度拷贝，返回新对象
        var result;
        (source instanceof Array) ? (result = []) : (result = {});
        for (var key in source) {
          result[key] = typeof source[key] === 'Array' ? this.deepCopy(source[key]) : source[key];
        }
        return result;
      },
      allowDrag(draggingNode) {
        if(draggingNode.data.enabled) {//是否可用
          return draggingNode.data.component;
        } else {
          return false;
        }
      },
      allowDrop(draggingNode, dropNode, type) {
        return false;
      },
      filterNode(value, data) {
        if (this.curProject.project) {
          var distributed = false;//是否分布式-默认否
          var projectDisTribute = this.curProject.project.paralleled;
          if (data.component) {
            switch (data.extra.engine) {
              case "R":
                distributed = false;
                break;
              case "PYTHON":
                distributed = false;
                break;
              case "HADOOP":
                distributed = true;
                break;
              case "SPARK":
                distributed = true;
                break;
              default:
                distributed = false;
            }
          }
          if (projectDisTribute == distributed) {
            if(!value) return true;
            return data.label.indexOf(value) !== -1;
          } else {
            return false;
          }
        }
        if (!value)  return true
        return data.label.indexOf(value) !== -1;
      }
    },
    components: {
      ProjectTree
    }
  }
</script>

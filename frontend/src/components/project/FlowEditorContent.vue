<template>
    <span>
        <div class="drawingWork clearfix" @click="clearPop" :style="{marginLeft: marginLeft + 'px' }">
            <div class="eProperty" id="rightProperties" :style="{width:propsContentStyle.width+'px'}">
                <!--右侧属性栏-->
                <component v-bind:is="currentView"></component>
            </div>
            <div class="dmMove" @mousedown="lrDragMouseDown($event)"></div>
            <div class="dWork">
                <!--中间可视化编辑区-->
                <ViewEditArea/>
            </div>
        </div>
        <CreateProjectDialog/>
        <ProjectContentMenu/>
        <PreviewDataDialog/>
        <PreviewResultDialog/>
        <PreviewLogsDialog/>
        <PreviewRealLogDialog/>
        <PreviewImageDialog/>
        <PreviewCodeDialog/>
        <SaveAsTemplateDialog/>
        <SaveAsNewProject/>
        <UpdateProjectDetailDialog/>
    </span>
</template>
<script>
  import ProjectPropsDestroy from "./ProjectPropsDestroy.vue"
  import ProjectContentMsg from "./view_edit_area/ProjectContentMsg.vue"
  import ViewEditArea from "./ViewEditArea.vue"
  import UpdateProjectDetailDialog from "./project_menu/project_tree/UpdateProjectDetailDialog.vue"
  import CreateProjectDialog from "./project_menu/project_tree/CreateProjectDialog.vue"
  import ProjectContentMenu from "./project_menu/project_tree/ContentMenu.vue"
  import PreviewDataDialog from "./view_edit_area/drag_component/right_menu_content/PreviewDataDialog.vue"
  import PreviewResultDialog from "./view_edit_area/drag_component/right_menu_content/PreviewResultDialog.vue"
  import PreviewLogsDialog from "./view_edit_area/drag_component/right_menu_content/PreviewLogsDialog.vue"
  import PreviewRealLogDialog from "./view_edit_area/drag_component/right_menu_content/PreviewRealLogDialog.vue"
  import PreviewImageDialog from "./view_edit_area/drag_component/right_menu_content/PreviewImageDialog.vue"
  import PreviewCodeDialog from "./view_edit_area/drag_component/right_menu_content/PreviewCodeDialog.vue"
  import SaveAsTemplateDialog from "./project_menu/project_tree/SaveAsTemplateDialog.vue"
  import SaveAsNewProject from "./project_menu/project_tree/SaveAsNewProject.vue"

  var $d3 = require('d3');
  var win;
  export default {
    name: 'FlowEditorContent',
    data: function () {
      return {
        dragStyle: {//拖拽属性
          conR: 0
        },
        marginLeft: 180
      }
    },
    computed: {
      currentView: function () {
        return this.flowData.selectComponent;
      },
      flowData: function () {
        return this.$store.state.project.flowData;
      },
      propsContentStyle: function () {
        return this.$store.state.project.flowData.propsContentStyle;
      }
    },
    mounted() {
      this.$('body').addClass('loaded');
      this.$('#loader-wrapper .load_title').remove();
    },
    created: function () {
      this.clearPop();//清除右键菜单，如果存在
      this.$store.state.global.otherState.previewProjectFlow = false;
      this.$store.state.global.otherState.uuid = "";
      if (this.$router.currentRoute.path == '/show') {
        this.$store.state.global.otherState.previewProjectFlow = true;
        this.showFillFlow();
      }
    },
    methods: {
      lrDragMouseDown: function (event) {//按下
        var rightContent = document.getElementById("rightProperties").getBoundingClientRect();
        this.dragStyle.conR = rightContent.right;
        win = $d3.select(window).on("mousemove", this.mouseMove).on("mouseup", this.mouseUp);
        event.preventDefault();
      },
      mouseMove: function () {//按下-拖动
        var curX = $d3.event.pageX;
        var width = this.dragStyle.conR - curX;
        this.propsContentStyle.width = width < 210 ? 210 : (width > 400 ? 400 : width);
      },
      mouseUp: function () {//按下-放开
        win.on("mousemove", null).on("mouseup", null);
      },
      clearPop: function () {//清理右键的弹框
        this.flowData.projectContextMenuMsg.display = "none";
        this.flowData.nodeRightContentMsg.display = "none";
        this.$store.state.project.modelMenu.contentMenuMessage.display = "none";
      },
      dealFlowJson: function () {
        var data = {};
        var nodes = this.flowData.nodes;
        var links = this.flowData.links;
        data["style"] = this.flowData.contentStyle;
        data["nodes"] = nodes;
        data["links"] = links;
        return JSON.stringify(data);
      },
      showFillFlow: function () {
        this.marginLeft = 0;
        var path = this.$router.currentRoute.path;
        var fullPath = this.$router.currentRoute.fullPath;
        var search = fullPath.substring(path.length);
        var url = this.getSearchString(search, "f");
        var uuid = url.substring(url.lastIndexOf("/") + 1);
        this.$store.state.global.otherState.uuid = uuid;
        this.$store.dispatch("fillProjectJsonData", {
          url: url
        });
      },
      getSearchString(url, key) {//根据键获取url参数中的value值
        // URL:?f=http://
        var str = url;
        str = str.substring(1, str.length);
        // 以&分隔字符串，获得类似name=xiaoli这样的元素数组
        var arr = str.split("&");
        var obj = new Object();
        // 将每一个数组元素以=分隔并赋给obj对象
        for (var i = 0; i < arr.length; i++) {
          var tmp_arr = arr[i].split("=");
          obj[decodeURIComponent(tmp_arr[0])] = decodeURIComponent(tmp_arr[1]);
        }
        return obj[key];
      }
    },
    components: {
      ProjectPropsDestroy,
      ViewEditArea,
      ProjectContentMenu,
      CreateProjectDialog,
      ProjectContentMsg,
      PreviewDataDialog,
      PreviewResultDialog,
      PreviewLogsDialog,
      PreviewRealLogDialog,
      PreviewImageDialog,
      PreviewCodeDialog,
      SaveAsTemplateDialog,
      SaveAsNewProject,
      UpdateProjectDetailDialog,
    }
  }
</script>

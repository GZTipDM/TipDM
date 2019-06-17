<template>
  <div>
    <div class="wrHd">
      <a @click="zoomOut()" title="缩小画布">
        <i class="fa fa-search-minus"></i>
      </a>
      <span id="zoomSize" class="zoomSize">{{contentStyle.height}}%</span>
      <a @click="zoomIn()">
        <i class="fa fa-search-plus" title="放大画布"></i>
      </a>
      <span class="gLine"></span>
      <a @click="saveFlow">
        <i class="fa fa-save" title="保存流程"></i>
      </a>
      <span class="gLine"></span>
      <a id="run" @click="play" v-if="!isRunning">
        <i class="fa fa-play-circle" title="全部运行"></i>
      </a>
      <a id="sptop" @click="stop" v-else-if="isRunning">
        <i class="fa" :class="isHover ? 'fa-stop-circle' : 'fa-spinner fa-spin'" @mouseover="mouseover"
           @mouseout="mouseout" title="停止运行"></i>
      </a>
    </div>
  </div>
</template>
<style>
</style>
<script>
  import {mapGetters} from "vuex"

  export default {
    name: 'WorkHeader',
    data() {
      return {
        isHover: false
      }
    },
    computed: {
      ...mapGetters([
        "httpServer",
        "permission"
      ]),
      flowData: function () {
        return this.$store.state.project.flowData;
      },
      viewEditArea: function () {
        return this.$store.state.project.viewEditArea;
      },
      contentStyle: function () {
        return this.flowData.contentStyle;
      },
      isRunning: function () {
        return this.flowData.isRunning;
      }
    },
    methods: {
      zoomIn: function () {//拓展画布
        if (this.contentStyle.height <= 290) {
          this.contentStyle.height += 10;
        }
      },
      zoomOut: function () {//缩小画布
        if (this.contentStyle.height > 100) {
          this.contentStyle.height -= 10;
        }
        var content = document.getElementById("dwaContent");
        var clientHeight = content.clientHeight;
        var scrollHeight = content.scrollHeight;
        console.log(clientHeight + "-" + scrollHeight);
        if (clientHeight < scrollHeight) {
          this.contentStyle.height += 10;
        }
      },
      saveFlow: function () {//保存流程
        //当前选中工程项
        var curProjectItem = this.flowData.currentProjectDetail;
        if (curProjectItem.id) {
          this.saveCurrentFlowJson();
        } else {
          this.$notify({
            title: "提示",
            message: "请先选择工程！",
            type: "warning"
          });
        }
      },
      //同步流程json数据到服务端
      saveCurrentFlowJson: function () {
        var that = this;
        var curProjectItem = that.flowData.currentProjectDetail;
        var data = {};
        var nodes = that.flowData.nodes;
        var links = that.flowData.links;
        data["style"] = that.flowData.contentStyle;
        data["nodes"] = nodes;
        data["links"] = links;
        let formData = new FormData();
        formData.append("content", JSON.stringify(data));
        this.$.ajax({
          url: that.httpServer + "/api/project/" + curProjectItem.id ,
          method: "post",
          data: formData,
          processData: false,
          contentType: false,
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          success: function (response) {
            that.$notify({
              title: "提示",
              message: "保存成功",
              type: "success"
            });
          },
          error: function (response) {
            that.$store.commit("dealRequestError", response);
          }
        });
      },
      play: function () {//全部运行
        //当前选中工程项
        var curProjectItem = this.flowData.currentProjectDetail;
        if (curProjectItem.id) {
          //更新运行状态为正在运行
          this.runMethod({
            run: true,
            runIndex: 1,
            projectId: curProjectItem.id
          });
        } else {
          this.$notify({
            title: "提示",
            message: "请先选择工程！",
            type: "warning"
          });
        }
      },
      stop: function () {//停止运行
        this.$store.dispatch("stopFlow");
      },
      mouseover: function () {
        if (this.isRunning) {
          this.isHover = true;
        }
      },
      mouseout: function () {
        if (this.isRunning) {
          this.isHover = false;
        }
      },
      runMethod: function (args) {//运行方法
        var that = this;
        try {
          this.$store.commit("updateMenuProjectSelectComponent", "");//更新右侧组件视图
          //当前选中工程项
          this.$store.dispatch("checkNodeElements").then((checkResult) => {
            console.log(checkResult);
            if (checkResult.testResult) {
              //保存完成后 运行流程
              this.$store.dispatch("saveCurrentFlowJson", args);
            } else {
              this.flowData.isRunning = false;
              this.$notify({
                title: "提示",
                message: checkResult.msg,
                type: "warning"
              });
            }
          })
        } catch (e) {
          that.flowData.isRunning = false;
        }
      }
    }
  }

</script>

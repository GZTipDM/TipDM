<template>
  <div class="context_menu" :style="positionStyle">
    <div class="content">
      <ul>
        <!--预览工程状态下不显示-->
        <template v-if="!otherState.previewProjectFlow">
          <li @click="renameNode" :class="{disableClass:flowData.isRunning}"><i class="fa fa-edit"></i>重命名</li>
          <li @click="deleteNode" :class="{disableClass:flowData.isRunning}"><i class="fa fa-trash-o"></i>删除</li>
          <hr/>
          <li @click="runAll" :class="{disableClass:flowData.isRunning}"><i class="fa fa-play-circle"></i>全部运行</li>
          <li @click="runToThisNode" :class="{disableClass:flowData.isRunning}"><i class="fa fa-step-forward"></i>运行到此处
          </li>
          <li @click="runOnlyThisNode" :class="{disableClass:flowData.isRunning}"><i class="fa fa-play-circle-o"></i>运行该节点
          </li>
          <li @click="runFromThisNode" :class="{disableClass:flowData.isRunning}"><i class="fa fa-forward"></i>从此节点运行
          </li>
          <hr/>
        </template>
        <!--<li @click="previewRealLog"><i class="fa fa-eye"></i>查看实时日志</li>-->
        <template v-if="childItems.length==1 && childItems[0].canPreview">
          <li @click="fillStructure(childItems[0].value)" :class="{disableClass:flowData.isRunning}"><i
            class="fa fa-table"></i>查看数据
          </li>
        </template>
        <template v-else-if="childItems.length>1 && showChild">
          <li @mouseover="childMenu.display='block'" @mouseout="childMenu.display='none'"
              :class="{disableClass:flowData.isRunning}">
            <a href="javascript:void(0)"><i class="fa fa-table"></i>查看数据 <i
              style="float:right;height:26px; line-height:26px;" class="fa fa-angle-right"></i></a>
            <div class="child_menu" :style="{display:childMenu.display}">
              <ul>
                <template v-for="item in childItems">
                  <template v-if="item.canPreview">
                    <li @click="fillStructure(item.value)" :title="item.name"
                        :class="{disableClass:flowData.isRunning}"><i
                      class="fa fa-table"></i>{{item.name}}
                    </li>
                  </template>
                </template>
              </ul>
            </div>
          </li>
        </template>
        <template v-if="isChart">
          <li @click="drawingViews" :class="{disableClass:flowData.isRunning}"><i class="fa fa-bar-chart-o"></i>绘制可视化
          </li>
        </template>
        <template v-if="viewResult">
          <li @click="previewResult" :class="{disableClass:flowData.isRunning}"><i class="fa fa-indent"></i>查看报告</li>
        </template>
        <li @click="previewLogs" :class="{disableClass:flowData.isRunning}"><i class="fa fa-list-alt"></i>查看日志</li>
        <template v-if="viewSource">
          <li @click="previewCode" :class="{disableClass:flowData.isRunning}"><i class="fa fa-file-code-o"></i>查看源码</li>
        </template>
      </ul>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'RightMenuContent',
    data: function () {
      return {
        childMenu: {
          display: "none"
        },
        previewImage: {
          display: "none"
        }
      }
    },
    computed: {
      isChart: function () {
        var range = ["pie", "line", "bar", "area", "scatter", "wordCloud"];
        var curNode = this.flowData.currentNodeItem;
        var result = false;
        if (curNode.tabs) {
          curNode.tabs.forEach((tab) => {
            tab.elements.forEach((ele)=>{
              if (ele.name == "drawingType" && this.$.inArray(ele.value, range) > -1) {
                result = true;
              }
            });
          });
        }
        return result;
      },
      positionStyle: function () {
        var msg = this.flowData.nodeRightContentMsg;
        var extraCounts = this.countExtraMenu();
        var top = msg.top == 0 ? 0 : ((msg.top - extraCounts * 26) > 0 ? (msg.top - extraCounts * 26) : 0);
        return {
          left: msg.left + 'px',
          top: top + 'px',
          display: msg.display
        }
      },
      flowData: function () {
        return this.$store.state.project.flowData;
      },
      viewSource: function () {//查看源码
        var result = false;
        try {
          result = this.flowData.currentNodeItem.allowViewSource;
        } catch (e) {
          console.error(e.message)
        }
        return result;
      },
      viewResult: function () {//查看结果
        var result = false;
        try {
          result = this.flowData.currentNodeItem.hasReport;
        } catch (e) {
          console.error(e.message)
        }
        return result;
      },
      childItems: function () {
        var curNode = this.flowData.currentNodeItem;
        var result = [];
        if (curNode.outputs) {
          curNode.outputs.forEach(function (output, index) {
            var tempItem = {};
            tempItem["name"] = output.description;
            tempItem["value"] = output.value;
            tempItem["id"] = output.id;
            tempItem["model"] = output.model;
            tempItem["canPreview"] = output.canPreview;
            result.push(tempItem);
          });
        }
        return result;
      },
      showChild: function () {//判断是否展示
        var result = false;
        this.childItems.forEach(function (item) {
          if (item.canPreview) {
            result = true;
          }
        });
        return result;
      },
      viewEditArea: function () {
        return this.$store.state.project.viewEditArea;
      },
      otherState: function () {
        return this.$store.state.global.otherState;
      }
    },
    methods: {
      //节点重命名
      renameNode: function () {
        if (this.flowData.isRunning) {
          this.$notify({
            message: "流程运行中，不允许该操作！",
            type: "warning"
          });
          return;
        }
        this.$prompt('请输修改后的名称', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPlaceholder: "请输入名称",
          inputValue: this.flowData.currentNodeItem.name,
          inputValidator(val) {
            var reg = /^(?! +$).+/;
            if (val.trim().length > 20) {
              return "重命名长度范围为1-20个字符";
            } else if (!reg.test(val)) {
              return "名称不能为空或空字符串";
            } else {
              return true;
            }
          }
        }).then(({value}) => {
          this.flowData.currentNodeItem.name = value.trim();
        }).catch(() => {
        });
      },
      runAll: function () {
        var curProjectItem = this.flowData.currentProjectDetail;
        if (curProjectItem.id) {
          this.runMethod({
            run: true,
            runIndex: 1,
            projectId: this.flowData.currentProjectDetail.id
          });
        } else {
          this.$notify({
            title: "提示",
            message: "请先选择工程！",
            type: "warning"
          });
        }
      },
      runToThisNode: function () {
        var curProjectItem = this.flowData.currentProjectDetail;
        if (curProjectItem.id) {
          this.runMethod({
            run: true,
            runIndex: 2,
            projectId: this.flowData.currentProjectDetail.id,
            componentId: this.flowData.currentNodeId.curNodeId
          });
        } else {
          this.$notify({
            title: "提示",
            message: "请先选择工程！",
            type: "warning"
          });
        }
      },
      runOnlyThisNode: function () {
        var curProjectItem = this.flowData.currentProjectDetail;
        if (curProjectItem.id) {
          this.runMethod({
            run: true,
            runIndex: 3,
            projectId: this.flowData.currentProjectDetail.id,
            componentId: this.flowData.currentNodeId.curNodeId
          });
        } else {
          this.$notify({
            title: "提示",
            message: "请先选择工程！",
            type: "warning"
          });
        }
      },
      runFromThisNode: function () {
        var curProjectItem = this.flowData.currentProjectDetail;
        if (curProjectItem.id) {
          this.runMethod({
            run: true,
            runIndex: 4,
            projectId: this.flowData.currentProjectDetail.id,
            componentId: this.flowData.currentNodeId.curNodeId
          });
        } else {
          this.$notify({
            title: "提示",
            message: "请先选择工程！",
            type: "warning"
          });
        }
      },
      deleteNode: function () {
        if (this.flowData.isRunning) {
          this.$notify({
            message: "流程运行中，不允许该操作！",
            type: "warning"
          });
          return;
        }
        this.$store.dispatch("deleteFlowNode");
      },
      fillStructure: function (tableName) {
        if (this.flowData.isRunning) {
          this.$notify({
            message: "流程运行中，不允许该操作！",
            type: "warning"
          });
          return;
        }
        this.viewEditArea.previewDialog = true;
        this.viewEditArea.previewTableName = tableName;
      },
      fillPreviewUrl: function (outId) {//弹出可视化窗口，填充iframe的url
        this.viewEditArea.previewImageDialog = true;
        var projectId = this.flowData.currentProjectDetail.id;
        try {
          var httpServer = this.$store.state.global.httpServer;
          var url = httpServer + "/api/project/" + projectId + "/node/" + outId + "/visual";
          var that = this;
          if (this.otherState.previewProjectFlow) {
            url = this.$store.state.global.httpServer + "/s/" + this.otherState.uuid + "/" + outId + "/visual";
          }
          this.$.ajax({
            url: url,
            method: "get",
            dataType: "json",
            beforeSend: function(xhr) {
              xhr.setRequestHeader("accessToken",localStorage.accessToken);
            },
            success: function (response) {
              that.viewEditArea.previewImageUrl = response.data;
            },
            error: function (response) {
              that.viewEditArea.previewImageUrl = "";
              that.$store.commit("dealRequestError", response);
            }
          });
        } catch (e) {
          console.error(e.message)
        }
      },
      previewResult: function () { //弹出查看结果窗口，填充iframe的url
        if (this.flowData.isRunning) {
          this.$notify({
            message: "流程运行中，不允许该操作！",
            type: "warning"
          });
          return;
        }
        try {
          var that = this;
          var nodeId = this.flowData.currentNodeItem.id;
          var url = this.$store.state.global.httpServer + "/api/project/node/" + nodeId + "/result";
          if (this.otherState.previewProjectFlow) {
            url = this.$store.state.global.httpServer + "/s/" + this.otherState.uuid + "/" + nodeId + "/result";
            debugger;
          }
          this.$.ajax({
            url: url,
            method: "get",
            dataType: "json",
            beforeSend: function(xhr) {
              xhr.setRequestHeader("accessToken",localStorage.accessToken);
            },
            success: function (response) {
              that.viewEditArea.previewResultData = response.data;
              that.viewEditArea.previewResultDialog = true;
            },
            error: function (response) {
              that.viewEditArea.previewResultData = "";
              that.$store.commit("dealRequestError", response);
            }
          });
        } catch (e) {
          console.error(e.message)
        }
      },
      //查看节点实时日志
      previewRealLog() {
        this.viewEditArea.previewRealLogDialog = true;
      },
      previewLogs: function () {//预览日志
        if (this.flowData.isRunning) {
          this.$notify({
            message: "流程运行中，不允许该操作！",
            type: "warning"
          });
          return;
        }
        this.viewEditArea.previewLogsDialog = true;
        try {
          var that = this;
          var nodeId = this.flowData.currentNodeItem.id;
          var url = this.$store.state.global.httpServer + "/api/project/node/" + nodeId + "/log";
          if (this.otherState.previewProjectFlow) {
            url = this.$store.state.global.httpServer + "/s/" + this.otherState.uuid + "/" + nodeId + "/log";
            debugger;
          }
          this.$.ajax({
            url: url,
            method: "get",
            dataType: "json",
            beforeSend: function(xhr) {
              xhr.setRequestHeader("accessToken",localStorage.accessToken);
            },
            success: function (response) {
              that.viewEditArea.previewLogsData = response.data;
            },
            error: function (response) {
              that.viewEditArea.previewLogsData = "";
              that.$store.commit("dealRequestError", response);
            }
          });
        } catch (e) {
          console.error(e.message)
        }
      },
      previewCode: function () {
        if (this.flowData.isRunning) {
          this.$notify({
            message: "流程运行中，不允许该操作！",
            type: "warning"
          });
          return;
        }
        this.viewEditArea.previewCodeDialog = true;
        try {
          var that = this;
          var nodeId = this.flowData.currentNodeItem.id;
          var projectId = this.flowData.currentProjectDetail.id;
          var url = that.$store.state.global.httpServer + "/api/project/" + projectId + "/" + nodeId + "/viewsource";
          if (this.otherState.previewProjectFlow) {
            url = this.$store.state.global.httpServer + "/s/" + this.otherState.uuid + "/" + nodeId + "/viewsource";
          }
          this.$.ajax({
            url: url,
            method: "get",
            dataType: "json",
            beforeSend: function(xhr) {
              xhr.setRequestHeader("accessToken",localStorage.accessToken);
            },
            success: function (response) {
              if (response.status == "SUCCESS") {
                that.viewEditArea.previewCodeData = response.data;
              } else if (response.status == "FAIL") {
                that.$notify({
                  type: "warning",
                  message: response.message
                });
                that.viewEditArea.previewCodeData = "";
              }
            },
            error: function (response) {
              that.viewEditArea.previewCodeData = "";
              that.$store.commit("dealRequestError", response);
            }
          });
        } catch (e) {
          that.viewEditArea.previewCodeData = "";
          that.viewEditArea.previewCodeDatas = "";
        }
      },
      runMethod: function (args) {//运行方法
        if (this.flowData.isRunning) {
          this.$notify({
            message: "流程运行中，不允许该操作！",
            type: "warning"
          });
          return;
        }
        //更新运行状态为正在运行
        if (this.flowData.isRunning) {
          this.$store.commit("updateMenuProjectSelectComponent", "");//更新右侧组件视图
          return;
        }
        //当前选中工程项
        this.$store.dispatch("checkNodeElements").then((checkResult) => {
          if (checkResult.testResult) {
            //保存完成后 运行流程
            this.$store.dispatch("saveCurrentFlowJson", args);
          } else {
            this.$notify({
              message: checkResult.msg,
              type: "warning"
            });
          }
        });
      },
      drawingViews: function () {//绘制可视化
        if (this.flowData.isRunning) {
          this.$notify({
            message: "流程运行中，不允许该操作！",
            type: "warning"
          });
          return;
        }
        this.viewEditArea.showEChartsDialog = true;
      },
      countExtraMenu: function () {//计算额外菜单数量
        var count = 0;
        if (this.viewResult) {//查看结果
          count += 1;
        }
        if (this.viewSource) {//查看源码
          count += 1;
        }
        if (this.showChild) {//查看数据 & 可视化
          count += 2;
        }
        return count;
      }
    }
  }
</script>

<style>
  .disableClass {
    background: #dcd5dd;
    color: #a08888;
  }

  .disableClass:hover {
    background: #dcd5dd;
    color: #a08888;
  }

  .context_menu .child_menu {
    background: #fff;
    padding: 6px 0;
    border: 1px solid #ccc;
    box-shadow: 0 0 6px rgba(102, 102, 102, 0.3);
    min-width: 150px;
    z-index: 99;
    position: absolute;
    border-radius: 2px;
  }

  .context_menu .content ul li {
    line-height: 26px;
    height: 26px;
    display: block;
    padding: 0 16px;
    cursor: pointer;
    white-space: nowrap;
    width: 118px;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .context_menu .content ul li i {
    color: #999;
    margin-right: 8px;
    width: 16px;
    text-align: right;
  }

  .context_menu .content ul li .child_menu {
    margin-left: 133px;
    margin-right: -133px;
    margin-top: -33px;
  }

  .context_menu .content ul li:hover {
    background: #fbe8ca;
    color: #000;
  }

  .context_menu .content ul li:hover i {
    color: #666;
  }

  .context_menu .content ul li.iLine {
    height: 10px;
    position: relative;
    cursor: default;
  }

  .context_menu .content ul li.iLine:hover {
    background: none;
  }

  .context_menu .content ul li.iLine:before {
    content: "";
    height: 1px;
    border-top: 1px dotted #dfdfdf;
    width: calc(100% - 20px);
    position: absolute;
    top: 4px;
    left: 10px;
  }

  hr {
    display: block;
    -webkit-margin-before: 0.2em;
    -webkit-margin-after: 0.1em;
    -webkit-margin-start: auto;
    -webkit-margin-end: auto;
    border-style: inset;
    margin: 0px 0px;
    border: 1px solid #ccc;
  }
</style>

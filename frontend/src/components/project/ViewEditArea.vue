<!--可视化编辑块-->
<template>
    <span>
        <!--头部工具栏-->
        <div class="wTool">
            <WorkHeader v-if="!otherState.previewProjectFlow"></WorkHeader>
        </div>
      <!--编辑区域-->
        <div id="dwArea" class="dwArea" @click="setCurrentMsg($event)" v-loading="flowData.projectChange"
             element-loading-text="数据加载中...">
            <div id="dwaContent" uWidth="100" uHeight="100"
                 :style="{width: contentStyle.width+'%', height: contentStyle.height+'%'}" @dragover="dragOver($event)"
                 @dragenter="dragEnter($event)" @drop="drop($event)">
                <DragComponent v-for="(item,index) in flowNodes" :item="item" :index="index"
                               :key="item.id"></DragComponent>
                <svg class="svgClass" style="width:100%;height:100%">
                    <PathLine v-for="(item,index) in flowLines" v-bind:item="item" v-bind:index="index"
                              :key="item.id"></PathLine>
                </svg>
            </div>
            <ProjectNodeContentMenu/>
        </div>
    </span>
</template>
<style>
  .svgClass {
    position: absolute;
    top: 0px;
    left: 0px;
    z-index: 1;
  }

  .test {
    fill: none;
    stroke: #ccc;
    stroke-width: 1.5px;
  }

  .test:hover {
    fill: none;
    stroke: #9c9c9c;
    stroke-width: 2px;
  }

  .test:focus {
    outline: none;
  }
</style>
<script>
  import WorkHeader from "./view_edit_area/WorkHeader.vue"
  import DragComponent from "./view_edit_area/DragComponent.vue"
  import PathLine from "./view_edit_area/PathLine.vue"
  import ProjectNodeContentMenu from "./view_edit_area/drag_component/RightMenuContent.vue"

  export default {
    name: 'ViewEditArea',
    computed: {
      flowData: function () {
        return this.$store.state.project.flowData;
      },
      flowNodes: function () {
        return this.flowData.nodes;
      },
      flowLines: function () {
        return this.flowData.links;
      },
      contentStyle: function () {
        return this.flowData.contentStyle;
      },
      otherState: function () {
        return this.$store.state.global.otherState;
      }
    },
    created() {
      this.$store.commit("updateMenuProjectSelectComponent", "ProjectContentMsg");
    },
    mounted: function () {
      var a = document.getElementById("dwArea").getBoundingClientRect();//外框
      var b = document.getElementById("dwaContent").getBoundingClientRect();//内容
      this.contentStyle.curHeight = a.height;
      this.contentStyle.curWidth = a.width;
    },
    components: {
      WorkHeader,
      DragComponent,
      PathLine,
      ProjectNodeContentMenu
    },
    methods: {
      dragOver: function (event) {
        event.preventDefault();
      },
      dragEnter: function (event) {
        event.preventDefault();
        console.log("dragEnter...");
      },
      drop: function (event) {
        event.stopPropagation();
        event.preventDefault();
        console.log("drop....");
        this.flowData.positionChange = true;
        var curProjectItem = this.flowData.currentProjectDetail;
        if (!curProjectItem.id) {
          this.$notify({
            title: "提示",
            message: "请先选择工程后进行操作！",
            type: "warning"
          });
          return;
        }
        var positions = this.getXYPosition(event);//获取被拖拽节点的左上角原点坐标
        this.flowData.currentXY = positions //更新拖拽移动后的节点的原点坐标
        //添加节点
        if (this.flowData.ifAddDom) {
          //节点id需要根据一定的规则生成不重复的数据临时
          this.getComponentId();//获取id并添加节点
        }
      },
      getComponentId: function () {//获取id
        var that = this;
        this.$.ajax({
          url: that.$store.state.global.httpServer + "/api/seq/?d="+new Date().getTime(),
          method: "get",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          success: function (data) {
            if (data.status == "SUCCESS") {
              that.addFlowItem(data.data);
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
      //构建添加可视化流程节点数据
      addFlowItem: function (compId) {
        try {
          var componentItem = this.flowData.addMenuItem;//添加组件项
          var inputs = [];//输入点集合
          var inputData = [];//输入集
          var outputData = []; //输出集
          if (componentItem.inputs) {
            var inNum = componentItem.inputs.length;//输入点数量
            var inEveryLeft = 180 / (inNum + 1);
            for (var i = 1; i <= inNum; i++) {
              inputs.push({
                id: compId + "_1_" + i,
                isConnected: false,
                left: inEveryLeft * (i),
                description: componentItem.inputs[i - 1]["description"],
                type: componentItem.inputs[i - 1]["type"],
                key: componentItem.inputs[i - 1]["key"],
                cat: componentItem.inputs[i - 1]["cat"],
                model: componentItem.inputs[i - 1]["model"],
                canPreview: componentItem.inputs[i - 1]["canPreview"],
                value: compId + "_1_" + i
              });
              inputData.push({
                key: componentItem.inputs[i - 1]["key"],
                dataFields: []
              });
              outputData.push({
                key: componentItem.inputs[i - 1]["key"],
                dataFields: []
              });
            }
          }
          var outputs = [];//输出点集合
          if (componentItem.outputs) {
            var outNum = componentItem.outputs.length;
            var outEveryLeft = 180 / (outNum + 1);
            for (var i = 1; i <= outNum; i++) {
              var value = "";
              if (componentItem.outputs[i - 1]["value"]) {//当添加为模型时，value值为模型路径
                value = componentItem.outputs[i - 1]["value"];
              } else {
                value = compId + "_0_" + i;
              }
              outputs.push({
                id: compId + "_0_" + i,
                left: outEveryLeft * (i),
                description: componentItem.outputs[i - 1]["description"],
                type: componentItem.outputs[i - 1]["type"],
                key: componentItem.outputs[i - 1]["key"],
                cat: componentItem.outputs[i - 1]["cat"],
                access: componentItem.outputs[i - 1]["access"],
                columns: componentItem.outputs[i - 1]["columns"],
                model: componentItem.outputs[i - 1]["model"],
                canPreview: componentItem.outputs[i - 1]["canPreview"],
                value: value
              });
            }
          }
          var tabs = [];//tab属性
          componentItem.tabs.forEach(function (tabItem) {
            tabItem.elements.forEach(function (ele) {
              //如果控件为数据源dataGrid，则添加字段属性 规则 element.name+"Items"字符串
              if (ele.elementType == 5) {
                ele.extra[ele.name + "Items"] = [];
              } else if (ele.elementType == 0) {
                ele.extra["tableName"] = "";
              }
            });
            tabs.push(tabItem);
          });
          var nodeId = compId + "";//节点id
          var nodeItem = {//节点信息
            id: nodeId, //节点id
            serverId: componentItem.id,//组件服务端ID
            engine: componentItem.extra.engine,
            name: componentItem.name,//节点名称
            allowViewSource: componentItem.allowViewSource,//是否支持R源码预览
            supportPMML: componentItem.supportPMML,//是否支持PMML
            hasReport: componentItem.hasReport,
            //下边三维运算符主要解决自定义算法无iconPath的问题
            iconPath: componentItem.iconPath ? (componentItem.iconPath.length > 0 ? componentItem.iconPath : "fa-cubes") : "fa-cubes",
            runStyle: "status-disW",//运行背景色 默认status-disW  完成 status-sus 失败 status-erro
            targetAlgorithm: componentItem.targetAlgorithm,//算法服务
            minimumInput: componentItem.minimumInput,//输入最小个数
            description: componentItem.description, //组件描述
            left: this.flowData.currentXY.left,
            top: this.flowData.currentXY.top,
            inputs: inputs,
            outputs: outputs,
            tabs: tabs,
            inputData: inputData,//节点的输入数据集，可能存在多个
            outputData: outputData,//节点的输出数据集，结构与输入保持一致
            outputExtraData: [],//当前节点的额外输出字段
            lastUpdateTime: new Date().getTime()//添加日期时间戳
          };
          var ifExist = this.flowNodes.filter(node => node.id==nodeItem.id);
          if(ifExist.length > 0) {
            this.$notify({
              type: "warning",
              message: "组件ID重复，添加失败！"
            });
          } else {
            this.flowNodes.push(this.deepCopy(nodeItem));//添加节点
          }
          this.$store.state.project.flowData.ifAddDom = false;
        } catch (e) {
          console.error(e.message);
        }
      },
      getXYPosition: function (event) {//计算节点的左上角原点位置
        var dom = document.getElementById("dwArea");//获取容器dom
        var centerDomOffset = dom.getBoundingClientRect();//获取容器的相对偏移
        var ew = 180;//被拖拽的节点的宽度
        var eh = 38;//被拖拽的节点的高度
        var cdl = centerDomOffset.left;//画布左偏移
        var cdt = centerDomOffset.top;//画布上偏移
        var cx = event.clientX;//鼠标X轴偏移
        var cy = event.clientY;//鼠标Y轴偏移
        var sl = dom.scrollLeft;//容器滚动左边偏移距离
        var st = dom.scrollTop;//容器滚动上部偏移距离
        var sw = dom.scrollWidth;//容器滚动宽度
        var sh = dom.scrollHeight;//容器滚动高度
        var plx = this.flowData.pointLayer.x;//鼠标点与节点元素的左边距离
        var ply = this.flowData.pointLayer.y; //鼠标点与节点元素的上边距离
        var _left = cx - cdl + sl - plx;
        var _top = cy - cdt + st - ply;
        return {
          left: _left < 0 ? 0 : (_left > sw - ew ? sw - ew : _left),//节点拖动左右边界值检测
          top: _top < 0 ? 0 : (_top > sh - eh ? sh - eh : _top)//节点拖动上下边界值检测
        }
      },
      setCurrentMsg: function (event) {
        event.preventDefault();
        this.$store.commit("updateMenuProjectSelectComponent", "ProjectContentMsg");
      },
      //深度拷贝，返回新对象或数组
      deepCopy: function (source) {
        var result;
        (source instanceof Array) ? (result = []) : (typeof(source) === "object") ? (source === null ? (result = "") : (result = {})) : (result = source);
        for (var key in source) {
          result[key] = (typeof source[key] === 'object') ? this.deepCopy(source[key]) : source[key];
        }
        return result;
      }
    }
  }
</script>

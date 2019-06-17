<!--画布中的节点-->
<template>
  <div tabindex="0" draggable="true" class="pane-node" @contextmenu="rightClick($event)"
       @click="setCurNodeId($event)" @keyup.delete="keyUpDelete"
       :style="{transform: 'translateX('+item.left+'px) translateY('+item.top+'px) translateZ(0px)',zIndex: 2}"
       @dragstart="domDragStart($event)" @dragend="domDragend($event)" :id="'node'+item.id">
    <div :title="item.name" class="pane-node-content element-process" :class="item.runStyle">
      <span class="icon icon-click"><i class="fa" :class="item.iconPath"></i></span>
      <span class="name" style="white-space: nowrap; text-overflow: ellipsis;">{{item.name}}</span>
      <span class="status icon-warning-o"></span>
      <div class="pane-ports in">
        <EleInPortItem v-for="(inItem,index) in item.inputs" v-bind:item="inItem" :index="index"
                       :nodeId="item.id" :key="index"></EleInPortItem>
      </div>
      <div class="pane-ports out">
        <EleOutPortItem v-for="(outItem,index) in item.outputs" v-bind:item="outItem" :index="index"
                        :nodeId="item.id" :key="index"></EleOutPortItem>
      </div>
    </div>
  </div>
</template>

<script>
  import EleInPortItem from "./drag_component/EleInPortItem.vue";
  import EleOutPortItem from "./drag_component/EleOutPortItem.vue";

  export default {
    name: 'DragComponent',
    props: ['item', 'index'],
    computed: {
      flowData: function () {//流程数据交互集
        return this.$store.state.project.flowData;
      },
      otherState: function () {
        return this.$store.state.global.otherState;
      }
    },
    components: {
      EleInPortItem,
      EleOutPortItem
    },
    methods: {
      domDragStart: function (event) {//节点开始拖拽，缓存当前节点的相对偏移
        event.dataTransfer.setData('Text', "aa");
        console.log("domDragStart...");
        var curOffset = event.currentTarget.getBoundingClientRect();//节点偏移属性
        var centerDomOffset = document.getElementById("dwArea").getBoundingClientRect();//画布容器偏移属性
        //更新当前节点的相对偏移
        this.$store.state.project.flowData.pointLayer = {
          x: event.clientX - curOffset.left,
          y: event.clientY - curOffset.top
        }
      },
      domDragend: function (event) { //在这里更新节点坐标 通过event相关获取
        event.preventDefault();
        console.log("domDragEnd...");
        //判断位移坐标是否修改
        if (!this.flowData.positionChange) {
          return;
        }
        this.flowData.positionChange = false;//判断复位
        this.$store.commit("updateNodeXYState", this.item.id);//修改节点坐标
        //获取该节点对应的源
        var curNode = this.flowData.nodes.filter(node => node.id == this.item.id)[0];
        //当前节点移动后左上角的坐标
        var curNodePoint = {x: curNode.left, y: curNode.top};
        //获取要求改的连线的源
        //==>获取所有从该节点连出去的线
        var outLinks = this.flowData.links.filter(node => node.source == this.item.id);
        for (var i = 0; i < outLinks.length; i++) {
          var ele = outLinks[i];
          var outPointLeft = curNode.outputs.filter(node => node.id == ele.outputPortId)[0].left;//获取线条源输出点相对偏移（当前节点）
          var source = {x: curNodePoint.x + outPointLeft + 2, y: curNodePoint.y + 38};//重绘源节点坐标
          var targetNode = this.flowData.nodes.filter(node => node.id == ele.target)[0];//获取指向节点
          var inPointLeft = targetNode.inputs.filter(node => node.id == ele.inputPortId)[0].left;//获取指向节点的输入点相对偏移
          var target = {x: targetNode.left + inPointLeft + 1, y: targetNode.top};//重绘指向节点坐标
          var position = [{source: source, target: target}];//重构坐标点信息
          ele.d = this.elbow(position[0]);//修改线条属性d
        }
        //==>获取所有连向该节点的线
        var inLinks = this.flowData.links.filter(node => node.target == this.item.id);
        for (var i = 0; i < inLinks.length; i++) {
          var ele = inLinks[i];//单线
          var sourceNode = this.flowData.nodes.filter(node => node.id == ele.source)[0];//该线的源节点
          var outPointLeft = sourceNode.outputs.filter(node => node.id == ele.outputPortId)[0].left;//源节点的输出点相对偏移
          var source = {x: sourceNode.left + outPointLeft + 2, y: sourceNode.top + 38};//重构源节点坐标
          var inPortLeft = curNode.inputs.filter(node => node.id == ele.inputPortId)[0].left;//指向点的输入点的相对偏移（当前节点）
          var target = {x: curNodePoint.x + inPortLeft + 1, y: curNodePoint.y};//重构线条指向坐标
          var position = [{source: source, target: target}];//坐标
          ele.d = this.elbow(position[0]);//修改线条属性d
        }
      },
      setCurNodeId: function (event) {//缓存当前节点信息
        event.preventDefault();
        if (this.flowData.isRunning) {
          return;
        }
        //阻止点击事件冒泡---begin
        if (event && event.stopPropagation) {//非IE
          event.stopPropagation();
        } else {//IE
          window.event.cancelBubble = true;
        }
        //阻止点击事件冒泡--end
        this.$store.commit("updateCurrentNode", {
          curNodeId: this.item.id,
          curNodeIndex: this.index
        });
        //设置当前项
        this.flowData.currentNodeItem = this.item;
        this.flowData.propsContentHeight = this.$(".drawingWork .eProperty").height();
        this.flowData.nodeRightContentMsg.display = "none";//清除节点上的右键菜单
        this.flowData.projectContextMenuMsg.display = "none";//清除工程菜单树上的右键菜单

        this.$store.commit("updateMenuProjectSelectComponent", "");
        setTimeout(() => {
          this.$store.commit("updateMenuProjectSelectComponent", "ProjectPropsDestroy");//更新右侧组件视图
        }, 100);
        this.$store.commit("updateMenuProjectNodeChange");//更新组件切换状态
      },
      elbow: function (d) {//计算path d值
        var e = d.source, t = d.target;
        var n = (e.x + t.x) / 2
          , i = (e.y + t.y) / 2
          , r = 50
          , o = t.y - e.y;
        return o > -100 && 100 > o && (r = Math.max(Math.abs(t.y - e.y) / 2, 30)),
          ["M", e.x, e.y, "Q", e.x, e.y + r, n, i, "T", t.x, t.y].join(" ");
      },
      keyUpDelete: function () {
        if (!this.flowData.isRunning) {
          this.$store.dispatch("deleteFlowNode");
        } else {
          this.$notify({
            type: "warning",
            message: "工程运行中，不能删除！"
          });
        }
      },
      rightClick: function (event) {
        if (document.all) {
          window.event.returnValue = false;
        } else {
          event.preventDefault()
        }
//                if (!this.flowData.isRunning) {
        //更细当前节点信息
        this.$store.commit("updateCurrentNode", {
          curNodeId: this.item.id,
          curNodeIndex: this.index
        });
        this.flowData.currentNodeItem = this.item;
        //获取位置
        var position = this.getNextContextMenuPosition(event);
        this.flowData.nodeRightContentMsg = {//设置节点右键菜单属性
          left: position.left,
          top: position.top,
          display: 'block'
        }
//                }
      },
      getNextContextMenuPosition: function (ev) { ///绘制右键菜单的坐标
        var dom = document.getElementById("dwArea");//获取容器dom
        var contentDomOffset = dom.getBoundingClientRect();//获取容器的相对偏移
        //组件所在容器的offset
        var contextMenuOffset = document.getElementsByClassName("context_menu")[0].getBoundingClientRect();

        var x = ev.clientX,//鼠标点击坐标X
          y = ev.clientY,//鼠标点击坐标Y
          cl = contentDomOffset.left,//容器的左偏移
          ct = contentDomOffset.top, //容器的上偏移
          //ch =  contentDomOffset.height,//容器的高度
          ch = 0,//容器的高度
          cw = contentDomOffset.width, //容器的宽度
          cst = dom.scrollTop,//容器的上滚动距离
          csl = dom.scrollLeft,//容器的左滚动距离
          csw = dom.scrollWidth,//容器的滚动宽度
          csh = dom.scrollHeight;//容器的滚动高度
        var mw = contextMenuOffset.width > 0 ? contextMenuOffset.width : 150;//菜单组件的宽度
        //mh = contextMenuOffset.height>0?contextMenuOffset.height:210;//菜单组件的高度
        var mh = 210;//菜单组件的高度
        if (this.otherState.previewProjectFlow) {
          mh = 53;
        }
        var _left = x - cl + csl;
        var _top = y - ct + cst;
        var left = _left + mw > cw + csl ? (_left - mw > 0 ? _left - mw : 0) : _left;
        var top = _top + mh > ch + cst ? (_top - mh > 0 ? _top - mh : 0) : _top;
        return {
          left: left,
          top: top
        }
      }
    }
  }
</script>
<style>
  .pane-node:focus {
    background-color: blue
  }
</style>

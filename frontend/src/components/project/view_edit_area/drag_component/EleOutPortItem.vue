<!--流程中节点的输出圆点组件-->
<template>
  <div :title="item.description+' 类型：'+item.cat" class="pane-port"
       :class="{'data':item.cat=='DATA', 'model':item.cat=='MODEL', 'unstruct': item.cat=='UNSTRUCT'}" :id="item.id"
       :style="leftStyle" @mousedown="outMouseDown($event)">
    <span class="port-magnet"></span>
  </div>
</template>
<script>
  var d3 = require('d3');
  var line, w;
  export default {
    name: 'EleOutPortItem',
    props: ['item', 'index', 'nodeId'],
    data: function () {
      return {
        source: ""
      }
    },
    computed: {
      leftStyle: function () {
        return {
          left: this.item.left + 'px'
        }
      },
      flowData: function () {
        return this.$store.state.project.flowData;
      }
    },
    methods: {
      outMouseDown: function (event) {//输出点鼠标按下事件
        this.flowData.source = {
          sourceNodeId: this.nodeId,
          sourcePortId: this.item.id,
          key: this.item.key,
          cat: this.item.cat
        };
        var offset = event.currentTarget.getBoundingClientRect();
        var contentDomOffset = document.getElementById("dwaContent").getBoundingClientRect();
        var source = {x: offset.left - contentDomOffset.left + 6, y: offset.top - contentDomOffset.top};
        this.source = source;
        var judgeClazz = this.item.cat.toLocaleLowerCase();
        var tempSelect = `.in .${judgeClazz}`;
        d3.selectAll(tempSelect).classed("is-connectable", true);
        //清除已被连接的点的可被链接状态
        d3.selectAll(".in .is-connected").classed("is-connectable", false);
        //清除自身节点的输入可被链接状态
        d3.select("#node" + this.nodeId).selectAll(".in .pane-port").classed("is-connectable", false);
        var position = [{source: source, target: source}];
        line = d3.selectAll("svg").append("g").selectAll("path").data(position).enter().append("path").attr("class", "test").attr("d", this.elbow);
        w = d3.select(window).on("mousemove", this.mouseMove).on("mouseup", this.mouseUp);
        event.preventDefault();

      },
      mouseMove: function () {//鼠标划线开始后的移动事件
        var contentDomOffset = document.getElementById("dwaContent").getBoundingClientRect();
        var target = {x: d3.event.pageX - contentDomOffset.left, y: d3.event.pageY - contentDomOffset.top};
        var position = [{source: this.source, target: target}];
        line.data(position).transition().duration(10).attr("d", this.elbow);
      },
      mouseUp: function () {//鼠标划线结束后放开事件
        this.flowData.source = {
          sourceNodeId: "",
          sourcePortId: "",
          key: "",
          cat: ""
        };
        if (!this.flowData.isConnecting) {
          line.remove();
        } else {
          var dom = document.elementFromPoint(d3.event.pageX, d3.event.pageY);
          var offset = dom.getBoundingClientRect();
          var contentDomOffset = document.getElementById("dwaContent").getBoundingClientRect();
          var target = {x: offset.left - contentDomOffset.left + 20, y: offset.top - contentDomOffset.top + 20};
          var position = [{source: this.source, target: target}];
          line.data(position).transition().duration(10).attr("d", this.elbow);
          line.remove();
          if (this.nodeId == this.$store.state.project.flowData.target.targetNodeId) {//连线时输入与输出是同一节点时不能生成连线
          } else {
            this.$store.commit("addFlowLinkNode", {
              sourceId: this.nodeId,
              d: this.elbow(position[0]),
              outputPortId: this.item.id
            });
          }
//                d3.select(dom.parentNode).classed("is-connected", true); 该行废弃，改为通过数据更新dom
        }
        d3.selectAll(".in .pane-port").classed("is-connectable", false);
        w.on("mousemove", null).on("mouseup", null);
      },
      elbow: function (d) {//计算path d值
        var e = d.source, t = d.target;
        var n = (e.x + t.x) / 2
          , i = (e.y + t.y) / 2
          , r = 50
          , o = t.y - e.y;
        return o > -100 && 100 > o && (r = Math.max(Math.abs(t.y - e.y) / 2, 30)),
          ["M", e.x, e.y, "Q", e.x, e.y + r, n, i, "T", t.x, t.y].join(" ");
      }
    }
  }
</script>

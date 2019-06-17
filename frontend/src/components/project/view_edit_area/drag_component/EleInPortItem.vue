<!--流程中节点的输入圆点组件-->
<template>
  <div :title="item.description" class="pane-port" :style="leftStyle"
       :class="{'is-connected': item.isConnected, 'data':item.cat=='DATA', 'model':item.cat=='MODEL', 'unstruct': item.cat=='UNSTRUCT'}"
       :id="item.id" @mouseover="inMouseOver" @mouseout="inMouseOut">
    <span class="port-magnet"></span>
  </div>
</template>

<script>
  var $d3 = require('d3');
  export default {
    name: 'EleInPortItem',
    props: ["item", "index", "nodeId"],
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
      inMouseOver: function (event) {//输入点鼠标进入
        //在输入节点已被连接或者与连接至该点的输出点cat不一致情况下不能连接节点
        if (this.item.isConnected || (this.item.cat != this.flowData.source.cat)) {//输入节点已被连接时，不能重复连线
          this.flowData.isConnecting = false;
        } else {
          //节点可以被链接
          this.flowData.isConnecting = true;
          $d3.select(event.currentTarget).select("span").classed("is-adsorbed", true);
          this.flowData.target = {
            targetNodeId: this.nodeId,
            targetPortId: this.item.id,
            model: this.item.model
          };
          //校验输入是否是模型&&输出是否接受模型
          /*
          if(this.flowData.source.key == "model") {
              if(this.item.model) {
                  this.flowData.isConnecting = true;
              } else {
                  this.flowData.isConnecting = false;
                  this.$notify({
                      message: "当前输入点不允许接入模型，请修改后重试！",
                      type: "warning"
                  });
              }
          }
          */
          //闭环校验
          this.checkCircle(this.flowData.source.sourceNodeId);
        }
      },
      inMouseOut: function (event) {//输出点鼠标移出
        //节点不能被链接
        this.flowData.isConnecting = false;
        $d3.select(event.currentTarget).select("span").classed("is-adsorbed", false);
        this.flowData.target = {
          targetNodeId: "",
          targetPortId: ""
        };
      },
      /* 校验流程是否闭环    逻辑
      1.找出source节点的父节点，
      2.校验父节点的输入点inPortId 是否存在target中的inPortId
      3.存在则闭环，设置连接状态为不可连接
      3.不存在，则找出该节点的父节点，重复上述校验
      */
      checkCircle: function (nodeId) {
        var that = this;
        var targetNodeId = this.nodeId;//指向节点id
        var targetPortId = this.item.id;//指向输出点id
        var links = this.flowData.links;//连线集
        var nodes = this.flowData.nodes;//节点集
        //找出所有指向该节点的连线
        var sourceLinks = links.filter(link => link.target == nodeId);
        //遍历连线，找出当前节点的所有父节点
        sourceLinks.forEach(function (link) {
          //找出当前连线的源节点
          var sourceNodes = nodes.filter(node => node.id == link.source);
          //遍历源节点
          sourceNodes.forEach(function (node) {
            //找出源节点的所有inputs，遍历、检查是否存在目的点
            node.inputs.forEach(function (input) {
              if (input.id == targetPortId) {
                //console.log("闭环");
                that.flowData.isConnecting = false;
                that.$notify({
                  title: "提示",
                  message: "流程不允许出现闭环！",
                  type: "warning"
                });
              } else {
                //重复校验
                that.checkCircle(node.id);
              }
            });
          });
        });
      }
    }
  }
</script>

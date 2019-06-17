<template>
  <g :class="[paneLink,item.runStyle]" @contextmenu="rightClick($event)" @keyup.delete="keyUpDelete" tabindex="0"
     :id="item.id" @click="selectPath">
    <path class="connector-wrap" :d="item.d"></path>
    <path class="connector" :d="item.d"></path>
    <path class="source-marker"></path>
    <path class="target-marker"></path>
  </g>
</template>

<script>
  export default {
    name: 'PathLine',
    props: ['item', 'index'],
    data: function () {
      return {
        paneLink: 'pane-link'
      }
    },
    computed: {
      flowData: function () {//流程数据交互集
        return this.$store.state.project.flowData;
      }
    },
    methods: {
      selectPath: function () {
        //阻止点击事件冒泡---begin 避免点击连线时触发父类组件的点击事件
        if (event && event.stopPropagation) {//非IE
          event.stopPropagation();
        } else {//IE
          window.event.cancelBubble = true;
        }
        //this.$store.state.other.contextMenuMsg.display="none";//清除界面上存在的右键菜单
        //阻止点击事件冒泡--end
        this.$store.commit('updateFlowCurrentPath', {
          curPathId: this.item.id,
          curPathIndex: this.index
        });
      },
      keyUpDelete: function () {
        if (!this.flowData.isRunning) {
          this.$store.dispatch("removeFlowPath");
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

        this.$store.commit('updateFlowCurrentPath', {//设置当前节点
          curPathId: this.item.id,
          curPathIndex: this.index
        });
        var position = this.getNextContextMenuPosition(event);
        /*
        this.$store.state.other.contextMenuMsg = {//更新右键菜单的属性
            left:position.left,
            top:position.top,
            display:'block'
        }
        */
      },
      getNextContextMenuPosition: function (ev) {///绘制右键菜单的坐标
        var dom = document.getElementsByClassName("pane-stage");//获取容器dom
        var contentDomOffset = dom[0].getBoundingClientRect();//获取容器的相对偏移
        //组件所在容器的offset
        var contextMenuOffset = document.getElementsByClassName("context_menu")[0].getBoundingClientRect();
        var x = ev.clientX,//鼠标点击坐标X
          y = ev.clientY,//鼠标点击坐标Y
          cl = contentDomOffset.left,//容器的左偏移
          ct = contentDomOffset.top,//容器的上偏移
          cst = dom[0].scrollTop,//容器的上滚动距离
          csl = dom[0].scrollLeft,//容器的左滚动距离
          csw = dom[0].scrollWidth,//容器的滚动宽度
          csh = dom[0].scrollHeight;//容器的滚动高度
        mw = contextMenuOffset.width > 0 ? contextMenuOffset.width : 200;//菜单组件的宽度
        mh = contextMenuOffset.height > 0 ? contextMenuOffset.height : 200;//菜单组件的高度
        var _left = x - cl + csl;
        var _top = y - ct + cst;
        return {
          left: _left + mw > csw ? _left - mw : _left,//计算菜单组件的左偏移包含边界
          top: _top + mh > csh ? _top - mh : _top//计算菜单组件的上偏移包含边界
        }
      }
    }
  }
</script>

<style>
  .pane-link {
    outline: none;
  }
</style>

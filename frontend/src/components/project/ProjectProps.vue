<!--菜单属性块-->
<template>
  <div class="h100p" style="position:relative">
    <el-collapse class="projectPropsParameter" accordion :value="currentNodeItem.tabs.length>0?currentNodeItem.tabs[0].id:''">
      <el-collapse-item v-for="tabItem in currentNodeItem.tabs" :title="tabItem.tabName" :name="tabItem.id" :key="tabItem.id">
        <div class="contentStyle">
            <template v-for="(element,index) in tabItem.elements">
              <PropItem :item="element" :index="index" :nodeItem="currentNodeItem"></PropItem>
            </template>
        </div>
      </el-collapse-item>
    </el-collapse>
    <div class="projectPropsDesc" :style="{width:propsContentStyle.width+'px'}">
      <el-collapse accordion>
        <el-collapse-item>
          <template slot="title">
            组件描述
          </template>
          <div class="projectPropsDescText">{{currentNodeItem.description}}</div>
        </el-collapse-item>
      </el-collapse>
    </div>
  </div>
</template>
<style>

  .el-collapse-item__content {
    padding: 0px
  }
</style>
<script>
  import PropItem from "./project_props/PropItem.vue";
  export default {
    name: 'ProjectProps',
    computed: {
      currentNodeItem: function () {//当前节点操作项
        return this.$store.state.project.flowData.currentNodeItem;
      },
      contentHeight() {
        var parentHeight = this.$store.state.project.flowData.propsContentHeight
        return parentHeight - 44 * (this.currentNodeItem.tabs.length + 1);
      },
      propsContentStyle: function () {
        return this.$store.state.project.flowData.propsContentStyle;
      }
    },
    components: {
      PropItem
    }
  }
</script>

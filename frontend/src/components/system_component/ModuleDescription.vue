<template>
  <el-card class="box-card" shadow="never">
    <div slot="header">
      <span>模块简介</span>
    </div>
    <div :style="{height: contentHeight+'px', overflow: 'auto'}">
      <div>该模块主要用于工程中系统组件的管理。。。</div>
    </div>
  </el-card>
</template>
<style scoped>
  .el-card {
    border: none;
  }
</style>
<script>
  export default {
    name: "ModuleDescription",
    data() {
      return {
        contentHeight: 400
      }
    },
    mounted() {
      this.contentHeight = this.$("#cManage").height()-94;
    }
  }
</script>

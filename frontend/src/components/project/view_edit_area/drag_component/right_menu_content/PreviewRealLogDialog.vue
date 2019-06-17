<template>
  <el-dialog title="查看当前节点实时日志" :visible.sync="previewData.previewRealLogDialog" class="center" width="85%">
    <el-col :span="24" label="dd" class="fl">
      <div style="height:400px; overflow-y:auto; text-align:left;" v-if="previewData.previewRealLogData != ''"
           v-html="previewData.previewRealLogData">
      </div>
      <div style="height:400px;" v-else>
        <el-input type="textarea" :readonly="true" class="curContent" :autosize="{minRows: 18, maxRows: 18}"
                  placeholder="无日志信息">
        </el-input>
      </div>
    </el-col>
  </el-dialog>
</template>
<style>
  .curContent .el-textarea__inner {
    resize: none;
  }
</style>
<script>
  export default {
    name: 'PreviewRealLogDialog',
    computed: {
      previewData: function () {
        return this.$store.state.project.viewEditArea;
      }
    }
  }
</script>

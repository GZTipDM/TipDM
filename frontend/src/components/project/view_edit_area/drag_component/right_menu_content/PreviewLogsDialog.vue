<template>
  <el-dialog title="查看日志" :visible.sync="previewData.previewLogsDialog" class="center" width="80%">
    <el-col :span="24" label="dd" class="fl">
      <div style="height:400px; overflow-y:auto; text-align:left;" v-if="previewData.previewLogsData != ''"
           v-html="previewData.previewLogsData">
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
    name: 'PreviewLogsDialog',
    computed: {
      previewData: function () {
        return this.$store.state.project.viewEditArea;
      }
    }
  }
</script>

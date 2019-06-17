<template>
  <el-dialog width="80%" top="20vh" title="查看源码" :visible.sync="previewData.previewCodeDialog" v-if="previewData.previewCodeDialog">
    <div>
      <editor :value="previewData.previewCodeData" @init="editorInit();" readOnly="true" lang="r" theme="chrome"
              width="100%" height="400"></editor>
    </div>
  </el-dialog>
</template>
<script>
  export default {
    name: 'PreviewCodeDialog',
    data() {
      return {
        activeTab: "first"
      }
    },
    components: {
      editor: require('vue2-ace-editor')
    },
    computed: {
      previewData: function () {
        return this.$store.state.project.viewEditArea;
      }
    },
    methods: {
      editorInit: function () {
        require('brace/mode/pgsql');
        require('brace/mode/r');
        require('brace/mode/python');
        require('brace/theme/chrome');
      },
      handleClick: function () {
      }
    }
  }
</script>

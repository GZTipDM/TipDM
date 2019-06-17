<!--渲染绘图按钮-->
<template>
  <el-form v-show="item.visible" label-position="top" class="demo-form-stacked" :style="paddingStyle">
    <el-form-item :label="item.label" prop="value">
      <template v-if="item.toolTip">
        <el-tooltip effect="light" placement="bottom-end" class="hint_information">
          <div slot="content">
            <template v-for="i in item.toolTip.split(';')">
              {{i}} <br/>
            </template>
          </div>
          <i class="fa fa-question-circle row_information" style="cursor:pointer;"></i>
        </el-tooltip>
      </template>
      <el-button type="primary" style="width: 100%" @click="drawing">{{item.value}}</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
  export default {
    name: 'TipDrawingButton',
    props: ["item", "index"],
    data: function () {
      return {
        paddingStyle: {
          paddingLeft: '5px',
          paddingRight: '5px'
        }
      }
    },
    computed: {
      viewEditArea: function () {
        return this.$store.state.menuProject.viewEditArea;
      }
    },
    methods: {
      drawing: function () {//绘图
        this.viewEditArea.showEChartsDialog = true;
      }
    }
  }
</script>

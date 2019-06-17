<template>
  <el-dialog title="数据可视化" top="10%" v-if="true" :visible.sync="previewData.previewImageDialog" class="center"
             width="85%">
    <el-col :span="24" label="dd" class="fl">
      <div style="height:450px; overflow: auto;">
        <span v-if="previewImageUrl === null">{{msg}}</span>
        <iframe v-else :src="previewImageUrl" @load="load" width="100%" height="100%" scrolling="auto"></iframe>
        <!--<el-row :gutter="10">-->
        <!--<el-col :span="9">-->
        <!--<div class="border">-->
        <!--<el-form label-position="left" label-width="80px">-->
        <!--<el-form-item label="标题">-->
        <!--<el-input v-model="optionData.title"></el-input>-->
        <!--</el-form-item>-->
        <!--</el-form>-->
        <!--<el-form label-position="left" label-width="80px">-->
        <!--<el-form-item label="X轴数据">-->
        <!--<el-select v-model="value" placeholder="请选择">-->
        <!--<el-option-->
        <!--v-for="item in options"-->
        <!--:key="item.value"-->
        <!--:label="item.label"-->
        <!--:value="item.value">-->
        <!--</el-option>-->
        <!--</el-select>-->
        <!--</el-form-item>-->
        <!--</el-form>-->
        <!--<el-form label-position="left" label-width="80px">-->
        <!--<el-form-item label="标题">-->
        <!--<el-input v-model="title"></el-input>-->
        <!--</el-form-item>-->
        <!--</el-form>-->
        <!--</div>-->
        <!--</el-col>-->
        <!--<el-col :span="1">-->
        <!--<div class="border">123</div>-->
        <!--</el-col>-->
        <!--<el-col :span="13">-->
        <!--<tip_bar :title="optionData.title" :xTitle="optionData.xData" :xData="optionData.sData"></tip_bar>-->
        <!--</el-col>-->
        <!--</el-row>-->
      </div>
    </el-col>
  </el-dialog>
</template>
<style>
</style>
<script>
  export default {
    name: 'PreviewImageDialog',
    data: function () {
      return {
        msg: "",
        loading: false,
        rawData: [],
        optionData: {
          title: 'hello vue',//title
          //X轴Data
          xData: ["衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子"],
          //X对应数据
          sData: [5, 20, 36, 10, 10, 20]
        },
        options: [{
          value: '选项1',
          label: '黄金糕'
        }, {
          value: '选项2',
          label: '双皮奶'
        }
        ]
      }
    },
    computed: {
      previewData: function () {
        this.loading = true;
        return this.$store.state.project.viewEditArea;
      },
      previewImageUrl: function () {
        if (this.previewData.previewImageUrl == null || this.previewData.previewImageUrl == "") {
          setTimeout(() => {
            this.msg = "数据可视化渲染失败";
            this.loading = false;
          }, 2000);
        }
        return this.previewData.previewImageUrl;
      },
      previewImageDialog: function () {
        return this.previewData.previewImageDialog;
      },
      previewDataList: function () {
        return this.previewData.previewDataList;
      }
    },
    watch: {
      previewImageDialog: function (val) {
        if (val) {
        }
      }
    },
    methods: {
      load: function () {
        this.loading = false;
      },
      getDataStructure() {//获取当前节点的输出数据

      }
    },
    components: {
      // tip_bar: TipBar
    }
  }
</script>

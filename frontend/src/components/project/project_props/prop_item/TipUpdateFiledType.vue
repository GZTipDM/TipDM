<!--修改输出列名类型组件-->
<template>
  <el-form v-show="item.visible" label-position="top" :model="dynamicValidateForm" ref="dynamicValidateForm"
           class="demo-form-stacked" :style="paddingStyle">
    <el-form-item :label="item.label">
      <template v-if="item.toolTip">
        <el-tooltip effect="light" placement="bottom-end"
                    class="hint_information">
          <div slot="content">
            <template v-for="i in item.toolTip.split(';')">
              {{i}} <br/>
            </template>
          </div>
          <i class="fa fa-question-circle row_information" style="cursor:pointer;"></i>
        </el-tooltip>
      </template>
      <div style="overflow:auto; max-height:250px;">
        <el-col :span="6">
          字段名
        </el-col>
        <el-col :span="6">
          类型
        </el-col>
        <el-col :span="6">
          新类型
        </el-col>
        <el-col :span="6">
          参数
        </el-col>
        <template v-for="(item,index) in dynamicValidateForm.domains">
          <el-row :gutter="2">
            <el-col :span="6">
              <el-input :disabled="true" :value="inputData[item.index]['name']" size="small"></el-input>
            </el-col>
            <el-col :span="6">
              <el-input :disabled="true" :value="inputData[item.index]['dataType']" size="small"></el-input>
            </el-col>
            <el-col :span="6">
              <el-select v-model="item.dataType" placeholder="请选择" size="small" @change="(value) => handleDataTypeChange(item,value)">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-input-number v-if="item.dataType=='numeric'" title="保留小数点位数" size="small" v-model="item.otherParam"
                               :min="0" :max="1000" @change="setItemValue"></el-input-number>
              <el-input-number v-else-if="item.dataType.indexOf('character')>-1" title="字符串长度" size="small"
                               v-model="item.otherParam" :min="0" :max="255" @change="setItemValue"></el-input-number>
            </el-col>
          </el-row>
        </template>
      </div>
    </el-form-item>
  </el-form>
</template>
<style scoped>
  .el-form-item .el-form-item {
    margin-bottom: 20px;
  }
</style>
<script>
  export default {
    name: "TipUpdateFiledType",
    props: ["item", "index", "nodeItem"],
    data: function () {
      return {
        paddingStyle: {
          paddingLeft: '5px',
          paddingRight: '5px'
        },
        options: [
//                    {value: 'character',label: 'character'},
//                    {value: 'bigint',label: 'bigint'},
          {value: 'numeric', label: '数值'},
//                    {value: 'bool',label: 'bool'},
          {value: 'text', label: '字符'},
          {value: 'date', label: '日期'},
          {value: 'timestamp', label: '时间'}
        ]

      }
    },
    computed: {
      inputData: function () {//当前参数对应的输入字段集
        var curFields = this.nodeItem.inputData.filter(node => node.key === this.item.extra.key)[0];
        return curFields.dataFields;
      },
      outputData: function () {//当前参数对应的输出字段集
        var curFields = this.nodeItem.outputData.filter(node => node.key === this.item.extra.key)[0];
        return curFields.dataFields;
      },
      dynamicValidateForm: function () {
        var result = {};
        result.domains = this.outputData;
        return result;
      }
    },
    created: function () {
      this.setItemValue();//组件被创建时更新组件值
    },
    watch: {
      outputData: function () {//当输出值有变化时，更新当前参数值
        this.setItemValue();
      }
    },
    methods: {
      handleDataTypeChange(item,val) {
        if(val=='numeric') {
          item.otherParam = 0;
        }
        this.setItemValue();
      },
      setItemValue: function () {//更新当前参数值
        try {
          var result = [];
          var that = this;
          this.outputData.forEach(function (item, index) {
            var field = {};
            field["origName"] = that.inputData[item.index]['name'];//原字段
            field["origType"] = that.inputData[item.index]['dataType'];//原类型
            field["targetType"] = item.dataType;//修改后字段
            field["otherParam"] = item.otherParam;//修改后参数
            result.push(field);
          });
          this.item.value = JSON.stringify(result);
        } catch (e) {
          console.error(e.message)
        }
      }
    }
  }
</script>

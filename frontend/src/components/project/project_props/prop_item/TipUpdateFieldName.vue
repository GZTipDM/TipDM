<!--修改输出列名组件-->
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
      <div style="width:100%;overflow:auto; max-height:250px;">
        <el-col :span="11">
          原字段名
        </el-col>
        <el-col :span="1"></el-col>
        <el-col :span="11">
          &nbsp;&nbsp;&nbsp;&nbsp;新字段名
        </el-col>
        <template v-for="(item,index) in dynamicValidateForm.domains">
          <el-col :span="11">
            <el-input :disabled="true" :value="inputData[item.index]['name']" size="small"></el-input>
          </el-col>
          <el-col :span="1">&nbsp;</el-col>
          <el-col :span="11">
            <el-form-item :prop="'domains.' + index + '.name'" :rules="rules">
              <el-input v-model="item.name" @change="setItemValue" size="small"></el-input>
            </el-form-item>
          </el-col>
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
    name: "TipUpdateFieldName",
    props: ["item", "index", "nodeItem"],
    data: function () {
      //自定义输入名称输入校验
      var validateTableName = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('不能为空'));
        } else {
          var regstr = this.$store.state.regexp.fieldName;
          var reg = eval(regstr);//数字、26个英文字母或者下划线组成的字符串
          if (!reg.test(value)) {
            callback(new Error('异常字符'));
          }
          callback();
        }
      };
      return {
        paddingStyle: {
          paddingLeft: '5px',
          paddingRight: '5px'
        },
        rules: {
          validator: validateTableName, trigger: 'change'
        }

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
      setItemValue: function () {//更新当前参数值
        try {
          var result = [];
          var that = this;
          this.outputData.forEach(function (item, index) {
            var field = {};
            field["origName"] = that.inputData[item.index]['name'];//原字段
            field["targetName"] = item.name;//修改后字段
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

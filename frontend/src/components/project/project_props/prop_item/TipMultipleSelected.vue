<!--带校验的字段下拉多选选框-->
<template>
  <el-form v-show="item.visible" label-position="top" :model="item" :rules="rules" class="demo-form-stacked"
           :style="paddingStyle">
    <el-form-item :label="item.label" prop="value">
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
      <el-button type="primary" size="mini" title="更新字段下拉列表" @click="refreshDg"><i class="fa fa-refresh"
                                                                                   @click="refreshDg"></i>
      </el-button>
      <br/>
      <el-select multiple v-model="selectValue" @change="updateValue" :placeholder="item.placeholder" size="small"
                 style="width:100%">
        <el-option v-for="option in options" size="small" :key="option.value" :value="option.value"
                   :label="option.label"></el-option>
      </el-select>
    </el-form-item>
  </el-form>
</template>
<script>
  //下拉单选框 my-selected-single item.value 的格式为"aa:AA;bb:BB;cc:CC"
  export default {
    name: 'TipMultipleSelected',
    props: ["item", "index", "nodeItem"],
    data: function () {
      return {
        paddingStyle: {
          paddingLeft: '5px',
          paddingRight: '5px'
        },
        rules: {
          value: [
            {required: this.item.required, message: '请选择', trigger: 'change'}
          ]
        },
        fieldOptions: [],//临时参数
        options: [],//数据字段列表
        selectValue: []//已选择项[value1,value2,value3...]
      }
    },
    computed: {
      flowData: function () {
        return this.$store.state.project.flowData;
      },
      currentNodeItem: function () {
        return this.flowData.currentNodeItem;
      }
    },
    created() {
      try {
        this.options = this.item.extra[this.item.name + 'Items']
      } catch (e) {
        console.error(e.message)
      }
      if (this.item.value) {
        this.selectValue = this.item.value.split(",");
      }
    },
    methods: {
      dealOptions: function (optionStr) {
        var result = [];
        try {
          var array = optionStr.split(";");
          for (var int = 0; int < array.length; int++) {
            result.push({text: array[int].split(":")[0], value: array[int].split(":")[1]});
          }
        } catch (e) {
          console.error(e.message);
        }
        return result;
      },
      //更新选中值
      updateValue: function () { //格式 value1,value2,value3...
        this.item.value = this.selectValue.join(",");
      },
      refreshDg: function () {
        this.fillInputData();
      },
      fillInputData: function () {
        try {
          if (this.flowData.currentNodeItem.inputs.length == 0) {//如果没有输入则该节点为数据源
            console.error("下拉选择组件不使用与无输入的组件！");
          } else {
            //获取该参数对应的输入点
            var inputPort = this.currentNodeItem.inputs.filter(input => input.key == this.item.extra.key);
            //判断key值是否配置正常及输入点key是否相同
            if (inputPort.length != 1) {
              this.$notify({
                type: "warning",
                title: "参数配置异常",
                message: "参数额外配置key值与输入key值不匹配！"
              });
              return;
            }
            var inputPortId = inputPort[0].id;//输入点id
            var inputKey = inputPort[0].key;  //输入点key值
            //该输入点对应的输入字段集
            var curInputData = this.currentNodeItem.inputData.filter(node => node.key == inputKey)[0];
            //清空输入数据集
            curInputData["dataFields"] = [];
            this.options = [];//清空下拉菜单
            this.item.extra[this.item.name + 'Items'] = [];//清空当前项额外参数
            //清空对应输出数据集
//                        var curOutputData = this.currentNodeItem.outputData.filter(node => node.key == inputKey)[0];
//                        curOutputData["dataFields"] = [];
            //找出指向该输入点的连线
            var link = this.flowData.links.filter(link => link.inputPortId == inputPortId)[0];
            if (!link) {
              var oldLabel = this.item.label;
              this.item.label = "";
              this.item.value = "";
              this.item.label = oldLabel;
              var that = this;
              //清空当前参数缓存字段集合
              this.item.extra[this.item.name + 'Items'] = [];
              return;
            }
            ;
            var parentNode = this.flowData.nodes.filter(node => node.id == link.source)[0];
            //获取对应上个节点的输出字段集
            var parentOutputData = parentNode.outputData;

            var hasOutput = false;//当前节点的父节点是否有字段输出
            parentOutputData.forEach(function (item) {
              if (item.dataFields.length > 0) {
                hasOutput = true;
              }
            });
            var outputItem = parentNode.outputs.filter(out => out.id == link.outputPortId);
            var result = this.deepCopy(parentOutputData);
            if (outputItem.length > 0) {
              if (outputItem[0].access == "APPEND") {
                let cols = JSON.parse(outputItem[0].columns);
                if (cols.length > 0) {
                  hasOutput = true;
                }
                cols.forEach(function (col) {
                  result[0].dataFields.push({
                    name: col.name,
                    dataType: col.type,
                    values: ''
                  });
                });
              }
            }
            if (hasOutput) {//有设置输出字段
              var parentOutputExtraData = parentNode.outputExtraData;
              var extra = this.deepCopy(parentOutputExtraData);
              //填充对应的inputData项及当前参数项
              this.fillInputDataAndCurParamData(result, extra, curInputData);
            } else {//无设置输出字段,则尝试获取上个节点输出字段集
              var parentOutPortId = link.outputPortId;//上个节点的输出点id（id与value值一致）
              this.getOutputTableStructure(parentOutPortId, curInputData)
            }
          }
        } catch (e) {
          console.error(e.message)
        }
      },
      getOutputTableStructure: function (tableName, curInputData) {//填充当前参数对应的输入点字段
        try {
          var that = this;
          var url = that.$store.state.global.httpServer + "/api/datasource/table/" + tableName + "/structure";
          this.$.ajax({
            url: url,
            method: "get",
            type: "json",
            beforeSend: function(xhr) {
              xhr.setRequestHeader("accessToken",localStorage.accessToken);
            },
            success: function (response) {
              var fieldData = response.data;
              fieldData.forEach(function (item, index) {
                item["index"] = index;
              });
              //将父节点输出集进行组装重组-end
              if (fieldData.length > 0) {
                curInputData.dataFields = that.deepCopy(fieldData);
              }

              that.fieldOptions = [];

              curInputData["dataFields"].forEach(function (field) {
                that.fieldOptions.push({
                  label: field.name,
                  value: field.name
                });
              });

              that.options = that.fieldOptions;
              that.item.extra[that.item.name + 'Items'] = that.fieldOptions;
            },
            error: function (response) {
              that.$store.commit("dealRequestError", response);
            }
          });
        } catch (e) {
          console.error(e.message)
        }
      },
      fillInputDataAndCurParamData: function (parentData, extraData, curInputData) {
        try {
          var result = [];
          //将父节点输出集进行组装重组-start
          parentData.forEach(function (item) {
            item.dataFields.forEach(function (field) {
              result.push(field);
            });
          });
          extraData.forEach(function (field) {
            result.push(field);
          });
          result.forEach(function (item, index) {
            item["index"] = index;
          });
          //将父节点输出集进行组装重组-end
          curInputData.dataFields = this.deepCopy(result);
          this.fieldOptions = [];

          var that = this;
          curInputData["dataFields"].forEach(function (field) {
            that.fieldOptions.push({
              label: field.name,
              value: field.name
            });
          });

          this.options = this.fieldOptions;
          this.item.extra[this.item.name + 'Items'] = this.fieldOptions;
        } catch (e) {
          console.error(e.message)
        }
      },
      deepCopy: function (source) {
        var result;
        (source instanceof Array) ? (result = []) : (typeof(source) === "object") ? (source === null ? (result = "") : (result = {})) : (result = source);
        for (var key in source) {
          result[key] = (typeof source[key] === 'object') ? this.deepCopy(source[key]) : source[key];
        }
        return result;
      }
    }
  }
</script>
<style scoped>
  .el-form-item {
    margin-bottom: 5px;
  }
</style>

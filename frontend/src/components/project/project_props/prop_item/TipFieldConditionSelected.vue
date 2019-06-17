<!--带校验的字段下拉多选选框-->
<template>
  <el-form v-show="item.visible" label-position="top" class="demo-form-stacked"
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

      <el-button type="primary" icon="plus" size="mini" title="添加项"
                 @click="conList.push({field: '', method: 'sum'})"></el-button>
      <el-button type="primary" size="mini" title="更新字段下拉列表" @click="refreshDg"><i class="fa fa-refresh"
                                                                                   @click="refreshDg"></i></el-button>
      <br/>
      <div style="width:100%;overflow:auto; max-height:150px">
        <template v-for="(item,index) in conList">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-select v-model="item.field" @change="childItemValueChange" placeholder="" size="small">
                <el-option
                  v-for="item in fieldOptions"
                  :label="item.label"
                  :key="item.value"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="9">
              <el-select v-model="item.method" @change="childItemValueChange" placeholder="" size="small">
                <el-option
                  v-for="item in methodOptions"
                  :label="item.label"
                  :key="item.value"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="3">
              <i class="fa fa-trash-o" @click="conList.splice(index,1)"></i>
            </el-col>
          </el-row>
        </template>
      </div>
    </el-form-item>
  </el-form>
</template>
<script>
  //下拉单选框 my-selected-single item.value 的格式为"aa:AA;bb:BB;cc:CC"
  export default {
    name: 'TipFieldConditionSelected',
    props: ["item", "index", "nodeItem"],
    data: function () {
      return {
        paddingStyle: {
          paddingLeft: '5px',
          paddingRight: '5px'
        },
        options: [],
        conList: [//结果集
        ],
        fieldOptions: [],
        methodOptions: [//条件集
          {label: "sum", value: "sum"},
          {label: "count", value: "count"},
          {label: "avg", value: "avg"},
          {label: "min", value: "min"},
          {label: "max", value: "max"},
        ]
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
      this.fillList();
      try {
        if (this.item.extra[this.item.name + 'Items']) {
          this.fieldOptions = this.item.extra[this.item.name + 'Items'];
        }
      } catch (e) {
        console.error(e.message)
      }
    },
    watch: {
      conList: function () {//检测集合子集的变化
        //更新节点值
        this.childItemValueChange();
      }
    },
    methods: {
      fillList: function () {//填充条件列表信息
        var result = [];
        if (this.item.value) {
          try {
            result = JSON.parse(this.item.value)
          } catch (e) {
            console.error(e.message);
          }
        }
        this.conList = result;
      },
      childItemValueChange: function () {//更新节点值
        this.item.value = JSON.stringify(this.conList);
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
            var curOutputData = this.currentNodeItem.outputData.filter(node => node.key == inputKey)[0];
            curOutputData["dataFields"] = [];
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
            if (hasOutput) {//有设置输出字段
              var parentOutputExtraData = parentNode.outputExtraData;
              var result = this.deepCopy(parentOutputData);
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

<!--字段信息表格-->
<template>
  <el-form v-show="item.visible" label-position="top" :model="item" class="demo-form-stacked" :style="paddingStyle">
    <el-form-item :label="item.label">
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
      <el-button size="mini" type="primary" @click="refreshDg" title="更新字段列表"><i class="fa fa-refresh"></i>
      </el-button>
      <el-input placeholder="添加字段过滤字符串" size="small" @change="updateFilters" />
      <el-input type="text" style="display: none" />
      <el-table :row-class-name="'row_style'" :header-row-class-name="'row_header_style'" ref="table" :data="showDataList" height="220" border style="width: 100%;"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="45">
        </el-table-column>
        <el-table-column prop="name" label="字段" width="125">
        </el-table-column>
        <el-table-column prop="dataType" label="类型" width="65" :formatter="typeFormatter">
        </el-table-column>
        <el-table-column prop="values" label="取值范围" width="125" show-overflow-tooltip
                         :formatter="valsFormatter">
        </el-table-column>
      </el-table>
    </el-form-item>
  </el-form>
</template>
<style scoped>
  .demo-table-expand .el-form-item {
    margin-left: 10px;
  }
</style>
<script>
  export default {
    name: 'TipDataGrid',
    props: ["item", "index", "type"],
    data: function () {
      return {
        paddingStyle: {
          paddingLeft: '5px',
          paddingRight: '5px'
        },
        handleChange: false, //是否手动勾选
        filterString: "",//筛选字段
        dataList: [],//列表数据集合
      }
    },
    computed: {
      flowData: function () {
        return this.$store.state.project.flowData;
      },
      currentNodeItem: function () {
        return this.flowData.currentNodeItem;
      },
      showDataList() {
        return this.dataList.filter(node => node.name.indexOf(this.filterString) > -1);
      }
    },
    created() {
      if (this.item.extra[this.item.name + 'Items']) {
        this.dataList = this.item.extra[this.item.name + 'Items'];
      }
    },
    mounted: function () {
      this.fillDg();
      //自动填充完成后设置修改状态为手动
      this.handleChange = true;
    },
    methods: {
      updateFilters(value) {
        this.filterString = value;
        this.handleChange = false;
        var that = this;
        setTimeout(() => {
          that.fillDg();
          that.handleChange = true;
        }, 100);
      },
      handleSelectionChange(val) {
        try {
          //当组件创建时，不再执行该方法，手动时才调用
          if (!this.handleChange) {
            return;
          }
          var arr = new Array(val.length);
          val.sort(function (a, b) {
            return a.index - b.index;
          });
          val.forEach(function (item, index) {
            arr[index] = item.name;
          });
          this.item.value = arr.join(",");
          //设置输出字段
          this.changeOutputData(val);
        } catch (e) {
          console.error(e.message)
        }
      },
      changeOutputData: function (selectFields) {
        try {
          var inputPort = this.currentNodeItem.inputs.filter(input => input.key == this.item.extra.key)[0];
          var curOutputData = this.currentNodeItem.outputData.filter(node => node.key == inputPort.key)[0];
          curOutputData.dataFields = [];
          var outputFields = this.deepCopy(selectFields);
          //添加选中字段集
          outputFields.forEach(function (field) {
            curOutputData.dataFields.push(field);
          });
        } catch (e) {
          console.error(e.message)
        }
      },
      typeFormatter: function (row, column) {//格式化字段type
        var type = row.dataType.split(" ")[0];
        var result = type;
        switch (type) {
          case "numeric":
            result = "数值";
            break;
          case "text":
            result = "字符";
            break;
          case "date":
            result = "日期";
            break;
          case "timestamp":
            result = "时间";
            break;

        }
        return result;
      },
      valsFormatter: function (row, column) {//格式化vals
        //'character', 'bigint', 'numeric', 'bool', 'text', 'timestamp'
        var result;
        try {
          switch (row.dataType) {
            case "bigint":
            case "numeric":
              var arr = row.values.split(",");
              var numArray = new Array(arr.length);
              arr.forEach(function (val, index) {
                numArray[index] = val;
              });
              numArray.sort(this.sortNumber);
              result = numArray[0] + "-" + numArray[numArray.length - 1]
              break;
            default:
              result = row.values;
              break;
          }
        } catch (e) {
          result = "";
        }
        return result;
      },
      sortNumber: function (a, b) {//由小到大的排序器
        return a - b
      },
      fillDg: function () {
        //根据value填充extra中的值的选中状态
        var that = this;
        try {
          if (this.item.value) {
            this.item.value.split(",").forEach((field) => {
              var temptItem = that.dataList.filter(node => node.name == field);
              if (temptItem.length > 0) {
                that.$refs.table.toggleRowSelection(temptItem[0], true)
              }
            });
          }
        } catch (e) {
          console.log(e.message);
        }
      },
      refreshDg: function () {
        this.fillInputData();
      },
      fillInputData: function () {
        try {
          if (this.flowData.currentNodeItem.inputs.length == 0) {//如果没有输入则该节点为数据源
            var tabName = this.flowData.currentNodeItem.outputs[0].value;
            this.getTableStructure(tabName);
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
              this.dataList = [];
              return;
            }
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
        var that = this;
        var url = this.$store.state.global.httpServer + "/api/datasource/table/" + tableName + "/structure";
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
            curInputData.dataFields = that.deepCopy(fieldData);
            var oldLabel = that.item.label;
            that.item.label = "";
            that.item.value = "";
            that.item.extra[that.item.name + 'Items'] = that.deepCopy(fieldData);
            that.dataList = that.deepCopy(fieldData);
            that.item.label = oldLabel;
          },
          error: function (response) {
            that.$store.commit("dealRequestError", response);
          }
        });
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
          var oldLabel = this.item.label;
          this.item.label = "";
          this.item.value = "";
          this.item.extra[this.item.name + 'Items'] = this.deepCopy(result);
          this.dataList = this.deepCopy(result);
          this.item.label = oldLabel;
        } catch (e) {
          console.error(e.message)
        }
      },
      getTableStructure: function (tabName) {
        var that = this;
        var url = "";
        switch (this.type) {
          case "database":
            url = that.$store.state.global.httpServer + "/api/datasource/table/" + tabName + "/structure";
            break;
          case "hive":
            url = that.$store.state.global.httpServer + "/api/hive/default/structure?tableName=" + tabName;
            break;
        }
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
            var oldLabel = that.item.label;
            that.item.value = "";
            that.item.label = "";
            that.item.extra[that.item.name + 'Items'] = fieldData;
            that.dataList = fieldData;
            that.item.label = oldLabel;
            //默认输出所有字段
            that.currentNodeItem.outputData = [];
            that.currentNodeItem.outputData.push({
              key: "database",
              dataFields: fieldData
            });
          },
          error: function (response) {
            that.$store.commit("dealRequestError", response);
          }
        });
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

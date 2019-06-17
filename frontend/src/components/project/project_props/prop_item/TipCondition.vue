<template>
  <el-form v-show="item.visible" label-position="top" :model="item" class="demo-form-stacked" :style="paddingStyle">
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
      <el-button type="primary" size="mini" title="添加项"
                 @click="conList.push({concat: 'and',field: '', condition: 'gt',value: '0'})"><i class="fa fa-plus"></i></el-button>
      <el-button type="primary" size="mini" title="更新字段下拉列表" @click="refreshDg"><i class="fa fa-refresh"
                                                                                   @click="refreshDg"></i></el-button>
      <br/>
      <div style="width:100%;overflow:auto; max-height:150px">
        <template v-for="(item,index) in conList">
          <div style="width:360px;">
            <el-select v-model="item.concat" @change="childItemValueChange" placeholder="" size="small"
                       style="width:80px;">
              <el-option
                v-for="item in concatOptions"
                :label="item.label"
                :key="item.value"
                :value="item.value">
              </el-option>
            </el-select>
            <el-select v-model="item.field" @change="childItemValueChange" placeholder="" size="small"
                       style="width:80px;">
              <el-option
                v-for="item in fieldOptions"
                :label="item.label"
                :key="item.value"
                :value="item.value">
              </el-option>
            </el-select>
            <el-select v-model="item.condition" @change="childItemValueChange" placeholder="" size="small"
                       style="width:80px;">
              <el-option
                v-for="item in conditionOptions"
                :label="item.label"
                :key="item.value"
                :value="item.value">
              </el-option>
            </el-select>
            <el-input v-model="item.value" @change="childItemValueChange" size="small" style="width:80px"></el-input>
            <i class="fa fa-trash-o" @click="conList.splice(index,1)"></i>
          </div>
        </template>
      </div>
    </el-form-item>
  </el-form>
</template>

<script>
  export default {
    name: 'TipCondition',
    props: ["item", "index", "nodeItem"],
    data: function () {
      return {
        paddingStyle: {
          paddingLeft: '5px',
          paddingRight: '5px'
        },
        conList: [//结果集
        ],
        concatOptions: [//关联集
          {label: "and", value: "and"},
          {label: "or", value: "or"}
        ],
        fieldOptions: [],
        conditionOptions: [//条件集
          {label: ">", value: "gt"},
          {label: ">=", value: "geq"},
          {label: "=", value: "equal"},
          {label: "<", value: "lt"},
          {label: "<=", value: "leq"},
          //{label: "==", value: "equalTo"},
          {label: "!=", value: "notEqual"},
          {label: "contains", value: "contains"},
          {label: "notContains", value: "notContains"},
          {label: "like", value: "like"},
          {label: "notLike", value: "notLike"},
          {label: "rlike", value: "rlike"},
          {label: "notRlike", value: "notRlike"}
        ]
      }
    },
    created: function () {
      this.fillList();
      try {
        if (this.item.extra[this.item.name + 'Items']) {
          this.fieldOptions = JSON.parse(this.item.extra[this.item.name + 'Items']);
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
      updateLocal: function () {
        //this.$store.commit("localStorageSave");
      },
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
      fillFieldOptions: function (dataList) {

      },
      childItemValueChange: function () {//更新节点值
        this.item.value = JSON.stringify(this.conList);
      },
      refreshDg: function () {//获取字段信息
        this.fillInputData();
      },
      fillInputData: function () {
        try {
          if (this.nodeItem.inputs.length == 0) {//如果没有输入则该节点为数据源
            var tabName = this.nodeItem.outputs[0].value;
            this.getTableStructure(tabName);
          } else {
            //获取该参数对应的输入点
            var inputPort = this.nodeItem.inputs.filter(input => input.key == this.item.extra.key);
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
            var curInputData = this.nodeItem.inputData.filter(node => node.key == inputKey)[0];

            this.fieldOptions = [];

            var that = this;
            curInputData["dataFields"].forEach(function (field) {
              that.fieldOptions.push({
                label: field.name,
                value: field.name
              });
            });
            this.item.extra[that.item.name + 'Items'] = JSON.stringify(this.fieldOptions);
          }
        } catch (e) {
          console.error(e.message)
        }
      },
      getTableStructure: function (tabName) {
        var that = this;
        this.$.ajax({
          url: that.$store.state.global.httpServer + "/api/datasource/table/" + tabName + "/structure",
          method: "get",
          type: "json",
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          success: function (response) {
            var fieldData = response.data;
            that.fieldOptions = [];
            fieldData.forEach(function (field) {
              that.fieldOptions.push({
                label: field.name,
                value: field.name
              });
            });
            try {
              //将处理后的字段存储
              that.item.extra[that.item.name + 'Items'] = JSON.stringify(that.fieldOptions);
            } catch (e) {
              console.error(e.message)
            }
          },
          error: function (response) {
            that.$store.commit("dealRequestError", response);
          }
        });
      }
    }
  }
</script>

<style scoped>
  .el-form-item__content {
    z-index: 0
  }
</style>

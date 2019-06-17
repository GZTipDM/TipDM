<!--衍生变量-->
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
      <el-button type="primary" icon="plus" size="mini" title="添加字段"
                 @click="fields.push({name: '',type: 'text'})"></el-button>
      <br/>
      <div style="width:100%;overflow:auto; max-height:150px">
        <template v-for="(item,index) in fields">
          <el-row :gutter="10" style="margin-left: 0px; margin-right: 0px;">
            <el-col :span="10">
              <el-input v-model="item.name" @change="childItemValueChange" size="small"></el-input>
            </el-col>
            <el-col :span="10">
              <el-select v-model="item.type" @change="childItemValueChange" placeholder="" size="small">
                <el-option
                  v-for="item in fieldType"
                  :label="item.label"
                  :key="item.value"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="3">
              <i class="fa fa-trash-o" @click="fields.splice(index,1)"></i>
            </el-col>
          </el-row>
        </template>
      </div>
    </el-form-item>
  </el-form>
</template>

<script>
  export default {
    name: 'TipDerivedVariable',
    props: ["item", "index", "nodeItem"],
    data: function () {
      return {
        paddingStyle: {
          paddingLeft: '5px',
          paddingRight: '5px'
        },
        fields: [],//衍生字段集-结果集
        fieldType: [//关联集
          {label: "字符串", value: "text"},
          {label: "数字", value: "numeric"},
          {label: "时间戳", value: "timestamp"}
        ]
      }
    },
    created: function () {
      this.fillList();
    },
    watch: {
      fields: function () {//检测集合子集的变化
        //更新节点值
        this.childItemValueChange();
        this.updateFieldValue();
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
        this.fields = result;
      },
      childItemValueChange: function () {//更新节点值
        this.item.value = JSON.stringify(this.fields);
        this.updateFieldValue();
      },
      updateFieldValue: function () {
        try {
          var name = this.item.name;
          var outputExtraData = this.nodeItem.outputExtraData;
          var extraData = outputExtraData.filter(node => node.key == name);
          extraData.forEach(function (item) {
            var targetIndex = outputExtraData.findIndex(node => node == item);
            if (targetIndex > -1) {
              outputExtraData.splice(targetIndex, 1);
            }
          });
          this.fields.forEach(function (field) {
            outputExtraData.push({
              "key": name,
              "name": field.name,
              "dataType": field.type,
              "values": null
            });
          });
        } catch (e) {
          console.error(e.message)
        }
      }
    }
  }
</script>

<style scoped>
  .el-form-item__content {
    z-index: 0
  }
</style>

<!--带添加输出字段的文本输入框-->
<template>
  <el-form v-show="item.visible" label-position="top" :model="item" :rules="rules" class="demo-form-stacked"
           :style="paddingStyle">
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
      <el-input size="small" v-model="item.value" @change="updateFieldValue" :placeholder="item.placeholder"></el-input>
    </el-form-item>
  </el-form>
</template>

<script>
  export default {
    name: "TipAddFieldInput",
    props: ["item", "index", "nodeItem"],
    data: function () {
      var valueValidator = (rule, value, callback) => {
        if(this.item.required) {
          try {
            var reg = eval(this.item.rexp);
            if (!reg.test(value)) {
              callback(new Error(this.item.placeholder));
            }
          } catch(e) {
            console.log(e.message)
            callback(new Error("校验异常！"))
          }
        }
        callback();
      };
      return {
        paddingStyle: {
          paddingLeft: '5px',
          paddingRight: '5px'
        },
        rules: {
          value: [
            {required: this.item.required, validator: valueValidator, trigger: 'change'}
          ]
        }
      }
    },
    created: function () {
      this.updateFieldValue();
    },
    methods: {
      updateFieldValue: function () {
        try {
          var name = this.item.name;
          var fieldName = this.item.value;
          var outputExtraData = this.nodeItem.outputExtraData;
          var extraData = outputExtraData.filter(node => node.key == name);
          if (extraData.length > 0) {
            extraData[0]["name"] = fieldName;
          } else {
            outputExtraData.push({
              "key": name,
              "name": fieldName,
              "dataType": "text",
              "values": null
            });
          }
        } catch (e) {
          console.error(e.message)
        }
      }
    }
  }
</script>

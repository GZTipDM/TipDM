<template>
  <el-form v-show="item.visible" label-position="top" :model="item" :rules="rules" class="demo-form-stacked" :style="paddingStyle">
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
      <el-checkbox v-model="item.value">{{item.options}}</el-checkbox>
    </el-form-item>
  </el-form>
</template>
<style>
</style>
<script>
  export default {
    name: 'TipCheckBox',
    props: ["item", "index"],
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
          paddingRight: '5px',
          paddingBottom: '15px'
        },
        isIndeterminate: false,
        rules: {
          value: [
            {required: this.item.required, validator:valueValidator, trigger: 'change'}
          ]
        }
      }
    },
    created: function () {
      console.log(this.item);
      if(this.item.value) {
        this.item.value = true
      } else {
        this.item.value = false
      }
    },
    methods: {
      handleCheckAllChange(event) {
        var arr = [];
        this.optionsList.forEach(function (option) {
          arr.push(option.value);
        });
        this.item.value = event.target.checked ? arr.join(",") : "";
        this.isIndeterminate = false;
      },
    }
  }
</script>

<!--带校验的文本域输入框-->
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
      <el-input type="textarea" :placeholder="item.placeholder" :autosize="{ minRows: 2, maxRows: 6}"
                v-model="item.value" @change="updateLocal"></el-input>
    </el-form-item>
  </el-form>
</template>

<script>
  export default {
    name: 'TipTextArea',
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
          paddingRight: '5px'
        },
        rules: {
          value: [
            {required: this.item.required, validator:valueValidator, trigger: 'change'}
          ]
        }
      }
    },
    methods: {
      updateLocal: function () {
        //this.$store.commit("localStorageSave");
      }
    }
  }
</script>

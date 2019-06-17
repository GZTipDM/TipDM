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
      <editor v-model="item.value" @init="editorInit();" :lang="models" theme="chrome" :placeholder="item.placeholder"
              width="380" height="220"></editor>
    </el-form-item>
  </el-form>
</template>
<style>
</style>
<script>
  export default {
    name: 'TipAceEditor',
    props: ["item", "index", "models"],
    data() {
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
        rules: {
          value: [
            {required: this.item.required, validator:valueValidator, trigger: 'change'}
          ]
        }
      }
    },
    components: {
      editor: require('vue2-ace-editor')
    },
    methods: {
      editorInit: function () {
        require('brace/mode/pgsql');
        require('brace/mode/r');
        require('brace/mode/python');
        require('brace/theme/chrome');
      }
    }
  }
</script>

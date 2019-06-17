<template>
  <el-form v-show="item.visible" label-position="top" :model="item" class="demo-form-stacked" :style="paddingStyle">
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
      <el-radio-group v-model="item.value">
        <el-radio v-for="option in dealOptions(item.options)" :key="option.value" :label="option.value">
          {{option.text}}
        </el-radio>
      </el-radio-group>
    </el-form-item>
  </el-form>
</template>
<style>
</style>
<script>
  export default {
    name: 'TipRadio',
    props: ["item", "index"],
    data: function () {
      return {
        paddingStyle: {
          paddingLeft: '5px',
          paddingRight: '5px'
        }
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
      }
    }
  }
</script>

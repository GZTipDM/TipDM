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
      <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
      <el-checkbox-group v-model="checksArray" @change="handleCheckedCitiesChange">
        <el-checkbox v-for="option in optionsList" :label="option.value" :key="option.value">{{option.text}}
        </el-checkbox>
      </el-checkbox-group>
    </el-form-item>
  </el-form>
</template>
<style>
</style>
<script>
  export default {
    name: "TipCheckBoxGroup",
    props: ["item", "index"],
    data: function () {
      return {
        paddingStyle: {
          paddingLeft: '5px',
          paddingRight: '5px'
        },
        isIndeterminate: false,
        checkAll: true,
        checksArray: []
      }
    },
    computed: {
      optionsList: function () {
        var result = [];
        try {
          var array = this.item.options.split(";");
          for (var int = 0; int < array.length; int++) {
            result.push({text: array[int].split(":")[0], value: array[int].split(":")[1]});
          }
        } catch (e) {
          console.error(e.message);
        }
        return result;
      },
      itemValue: function () {
        return this.item.value;
      }
    },
    watch: {
      itemValue: function () {
        this.checksArray = this.item.value == '' ? [] : this.item.value.split(',');
      }
    },
    created: function () {
      this.checksArray = this.item.value == '' ? [] : this.item.value.split(',');
      this.handleCheckedCitiesChange(this.checksArray);
    },
    methods: {
      handleCheckAllChange(event) {
        try {
          var arr = [];
          this.optionsList.forEach(function (option) {
            arr.push(option.value);
          });
          this.item.value = event.target.checked ? arr.join(",") : "";
          this.isIndeterminate = false;
        } catch (e) {
          console.error(e.message)
        }
      },
      handleCheckedCitiesChange(value) {
        try {
          this.item.value = value.join(",");
          let checkedCount = value.length;
          this.checkAll = checkedCount === this.optionsList.length;
          this.isIndeterminate = checkedCount > 0 && checkedCount < this.optionsList.length;
        } catch (e) {
          console.error(e.message)
        }
      }
    }
  }
</script>

<!--带校验的数值输入框-->
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
      <el-card class="box-card" style="min-height:100px;font-size:12px">
        <div v-for="input in nodeItem.inputs" class="text item"
             style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis;width:100%">
          <span :title="input.key"
                style="font-weight: bold;overflow: hidden; white-space: nowrap; text-overflow: ellipsis;width: 50px; display: block;float: left;">{{input.key
            }}</span> from
          <template v-if="input.isConnected">
            <span :title="input.value">{{input.value}}</span>
          </template>
          <hr
            style="width:110%;height: 1px;border: 1px solid white;border-bottom-color:#dcdcdc;margin:0px 0px 0px -5px"/>
        </div>
      </el-card>
    </el-form-item>
  </el-form>
</template>
<script>
  export default {
    name: 'TipShowInputList',
    props: ["item", "index", "nodeItem"],
    data: function () {
      return {
        paddingStyle: {
          paddingLeft: '5px',
          paddingRight: '5px'
        }
      }
    },
    methods: {
      updateLocal: function () {
        //  this.$store.commit("localStorageSave");
      }
    }
  }
</script>

<style scoped>
  .el-form-item {
    margin-bottom: 5px;
  }

  .el-card__body {
    padding: 0px 5px;
  }
</style>

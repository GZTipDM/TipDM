<template>
  <el-form :model="componentParams" label-width="7em">
    <el-form-item label="组件名称">
      <el-input v-model="componentParams.name" placeholder="请输入组件名称"></el-input>
    </el-form-item>
    <el-form-item label="图标路径">
      <el-input v-model="componentParams.iconPath" placeholder="请输入图标路径"></el-input>
    </el-form-item>
    <el-form-item label="算法服务">
      <el-select v-model="componentParams.targetAlgorithm" filterable placeholder="请选择，可根据算法名称快速搜索">
        <el-option v-for="item in options" :label="item.label" :key="item.value" :value="item.value"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="输入节点数">
      <el-input-number title="定义组件输入数据源的最小数目" v-model="componentParams.minimumInput" :min="0"
                       :max="4"></el-input-number>
    </el-form-item>
    <el-row>
      <el-col :span="12">
        <el-form-item label="是否支持PMML" label-width="9em">
          <el-switch active-text="" inactive-text="" v-model="componentParams.supportPMML"></el-switch>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="是否能够查看源码" label-width="9em">
          <el-switch active-text="" inactive-text="" v-model="componentParams.allowViewSource"></el-switch>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="是否查看运行报告" label-width="9em">
          <el-switch active-text="" inactive-text="" v-model="componentParams.hasReport"></el-switch>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="组件是否可用" label-width="9em">
          <el-switch active-text="" inactive-text="" v-model="componentParams.enabled"></el-switch>
        </el-form-item>
      </el-col>
    </el-row>
    <el-form-item label="组件分类">
      <div class="tree-menu" id="basicClass">
        <ul v-for="componentItem in componentsList[0].children" :key="componentItem.id">
          <ClassTree :item="componentItem"></ClassTree>
        </ul>
      </div>
    </el-form-item>
    <el-form-item label="描述">
      <el-input type="textarea" maxlength="1000" v-model="componentParams.description"></el-input>
    </el-form-item>
  </el-form>
</template>
<style>
</style>
<script>
  import ClassTree from './message/ClassTree.vue'

  export default {
    name: 'BasicMessage',
    data() {
      return {
        options: [],
      }
    },
    created: function () {
      var that = this;
      this.$.ajax({
        url: that.$store.state.global.httpServer + "/api/algorithm/list",
        method: "get",
        type: "json",
        beforeSend: function(xhr) {
          xhr.setRequestHeader("accessToken",localStorage.accessToken);
        },
        success: function (response) {
          var data = response.data;
          that.options = [];
          for (var p in data) {
            var child = {};
            child["value"] = data[p];
            child["label"] = p;
            that.options.push(child);
          }
        },
        error: function (data) {
          that.$notify({
            title: "提示",
            message: "获取算法服务失败！",
            type: "warning"
          });
        }
      });
    },
    computed: {
      componentsDeals() {
        return this.$store.state.systemComponent.componentsDeals;
      },
      componentParams: function () {//组件参数
        return this.componentsDeals.editComponent;
      },
      componentsList: function () {//树形菜单
        return this.$store.state.systemComponent.menuComponentsTree;
      },
      basicMessageSelectClass: function () {//选择分类
        return this.componentsDeals.basicMessageSelectClass;
      }
    },
    methods: {
      changeEngine: function () {//主要是用来重新渲染脚本编辑区的js加载效果
        this.componentsDeals.basicScriptDeals.showEditor = false;
        var that = this;
        setTimeout(function () {
          that.componentsDeals.basicScriptDeals.showEditor = true;
        }, 10);
      }
    },
    components: {
      ClassTree
    }
  }
</script>

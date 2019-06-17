<template>
  <div class="h100p">
    <!--添加tab pop框-->
    <el-popover ref="popover6" placement="left" width="160" v-model="visible1">
      <el-select v-model="tabName" placeholder="请选择添加脚本">
        <el-option
          v-for="(value,key) in scriptKeys"
          :key="key"
          :label="value"
          :value="key">
        </el-option>
      </el-select>
      <p>&nbsp;</p>
      <div style="text-align: right; margin: 0">
        <el-button size="mini" type="text" @click="visible1 = false">取消</el-button>
        <el-button type="primary" size="mini" @click="addTab">确定</el-button>
      </div>
    </el-popover>
    <!--删除tab pop框-->
    <el-popover ref="popover5" placement="top" width="160" v-model="visible2">
      <p>删除当前选中tab页，确认？</p>
      <div style="text-align: right; margin: 0">
        <el-button size="mini" type="text" @click="visible2 = false">取消</el-button>
        <el-button type="primary" size="mini" @click="removeTab">确定</el-button>
      </div>
    </el-popover>
    <el-button-group class="btnGroup">
      <el-button title="添加tab页" type="primary" size="mini" icon="el-icon-plus" v-popover:popover6/>
      <el-button title="删除选中tab页" type="primary" size="mini" icon="el-icon-minus" v-popover:popover5/>
    </el-button-group>
    <el-tabs type="card" @tab-click="handleTabClick" :value="curSelectTab">
      <el-tab-pane v-for="(value, key, index) in editComponent.script" :label="scriptKey[key]" :name="key" :key="key">
        <template v-if="basicScriptDeals.showEditor">
          <ScriptItem :curKey="key" :index="index" :scriptObject="editComponent.script"></ScriptItem>
        </template>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<style>
</style>
<script>
  import ScriptItem from './script/ScriptItem.vue';

  export default {
    name: 'BasicScript',
    data() {
      return {
        scriptKeys: {
          "PREDICT": "预测脚本",
          "EVALUATE": "评估脚本"
        },
        scriptKey: {
          "MAIN": "主体脚本",
          "PREDICT": "预测脚本",
          "EVALUATE": "评估脚本"
        },
        visible1: false,//pop显示属性-默认隐藏
        visible2: false,//确认删除pop框-默认隐藏
        tabName: "PREDICT",//新增tab页名称
        curSelectTab: ""//当前选中tab项
      }
    },
    created: function () {//初始化完成，设置第一项为默认项
      //设置当前默认tab项
      this.setDefaultTabItem();
    },
    computed: {
      editComponent() {
        return this.$store.state.systemComponent.componentsDeals.editComponent;
      },
      scriptObject: function () {//tab页集合
        return this.editComponent.script;
      },
      basicScriptDeals: function () {//数据交互集
        return this.$store.state.systemComponent.componentsDeals.basicScriptDeals;
      }
    },
    components: {
      ScriptItem
    },
    methods: {
      addTab: function () {
        if (this.scriptObject.hasOwnProperty(this.tabName)) {
          this.$notify({
            message: "已存在该tab项，不允许重复添加！",
            type: "warning"
          });
          return false;
        }
        this.scriptObject[this.tabName] = "";//添加对象
        //设置选中项
        this.curSelectTab = this.tabName;
        //隐藏pop弹出框
        this.visible1 = false;
      },
      removeTab: function () {//删除选中tab页
        if (this.curSelectTab) {
          if (this.curSelectTab == "MAIN") {
            this.$notify({
              message: "主体脚本不允许删除！",
              type: "warning"
            });
            return;
          }
          delete(this.scriptObject[this.curSelectTab]);
          this.setDefaultTabItem();
          this.visible2 = false;
        } else {
          this.$notify({
            message: "请选择tab页后重试！",
            type: "warning"
          });
        }
      },
      handleTabClick(tab, event) {//tab页切换点击事件
        this.curSelectTab = tab.name;
      },
      setDefaultTabItem: function () {//设置默认tab项
        try {
          var keys = [];
          var scripts = this.scriptObject;
          for (var key in scripts) {
            keys.push(key);
          }
          if (keys.length > 0) {
            this.curSelectTab = keys[0];
          }
        } catch (e) {
          console.error(e.message)
        }
      }
    }
  }
</script>

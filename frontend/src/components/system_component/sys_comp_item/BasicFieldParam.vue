<template>
  <div>
    <!--添加tab pop框-->
    <el-popover ref="popover6" placement="left" width="160" v-model="visible1">
      <el-input v-model="tabName" placeholder="输入Tab名称" :maxlength="20"></el-input>
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
    <!-- 编辑字段弹窗 -->
    <AddFieldDialog></AddFieldDialog>
    <el-button-group class="btnGroup">
      <el-button @click="tabLeftMove" title="左移选中tab页" type="primary" size="mini"
                 icon="el-icon-arrow-left"/>
      <el-button @click="tabRightMove" title="右移选中tab页" type="primary" size="mini"
                 icon="el-icon-arrow-right"/>
      <el-button title="添加tab页" type="primary" size="mini" icon="el-icon-plus" v-popover:popover6/>
      <el-button title="删除选中tab页" type="primary" size="mini" icon="el-icon-minus" v-popover:popover5/>
    </el-button-group>
    <el-tabs type="card" @tab-click="handleTabClick"
             :value="basicFieldParamDeals.TabItem?(basicFieldParamDeals.TabItem.tabName+basicFieldParamDeals.TabItem.id):''">
      <el-tab-pane v-for="(item, index) in editComponent.tabs" :key="item.tabName" :label="item.tabName"
                   :name="item.tabName+item.id">
        <TabPaneItem :item="item" :index="index"></TabPaneItem>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<style>
</style>
<script>
  import AddFieldDialog from "./field/AddFieldDialog.vue";
  import TabPaneItem from "./field/TabPaneItem.vue";

  export default {
    name: 'BasicFieldParam',
    data() {
      return {
        visible1: false,//pop显示属性-默认隐藏
        visible2: false,//确认删除pop框-默认隐藏
        tabName: ""//新增tab页名称
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
      tabList: function () {//tab页集合
        return this.editComponent.tabs;
      },
      basicFieldParamDeals: function () {//数据交互集
        return this.$store.state.systemComponent.componentsDeals.basicFieldParamDeals;
      }
    },
    watch: {
      tabList: function () {
        if (this.tabList.indexOf(this.basicFieldParamDeals.TabItem) == -1) {
          this.setDefaultTabItem();
        }
      }
    },
    components: {
      AddFieldDialog,
      TabPaneItem
    },
    methods: {
      addTab: function () {
        if (this.tabName.trim() == "" || this.tabName == null) {
          this.$notify({
            title: '提示',
            message: "Tab名称不能为空",
            type: "warning"
          });
          return;
        }
        //新对象
        var newTab = {
          "id": "tab" + this.basicFieldParamDeals.counter++,
          "createTime": "",
          "tabName": this.tabName.trim(),  //tab页名称
          "elements": []
        }
        //添加tab页
        this.tabList.push(newTab);
        //设置添加项为当前选中tab
        this.basicFieldParamDeals.TabItem = this.tabList[this.tabList.length - 1];
        this.tabName = "";
        //隐藏pop弹出框
        this.visible1 = false;
      },
      tabLeftMove: function () {
        this.sureMove(0);
      },
      tabRightMove: function () {
        this.sureMove(1);
      },
      sureMove: function (moveIndex) {
        var tempList = this.editComponent.tabs.slice(0);
        //判断当前是否有选中tab项
        if (this.basicFieldParamDeals.TabItem) {
          tempList.forEach((tab, index) => {
            tab["sort"] = index;
          });
          var index = tempList.findIndex(node => node.tabName + node.id == this.basicFieldParamDeals.TabItem.tabName + this.basicFieldParamDeals.TabItem.id);
          if (moveIndex == 1) {//右移
            if(tempList[index+1]) {
              tempList[index]["sort"] = tempList[index]["sort"]+1;
              tempList[index+1]["sort"] = tempList[index]["sort"]-1;
            }
          } else if (moveIndex == 0) {//左移
            if(this.tabList[index-1]) {
              tempList[index]["sort"] = tempList[index]["sort"]-1;
              tempList[index-1]["sort"] = tempList[index]["sort"]+1;
            }
          }
          tempList.sort(this.compare);
          this.editComponent.tabs.splice(0);
          setTimeout(() => {
            tempList.forEach((temp) => {
              this.editComponent.tabs.push(temp);
            });
          },10);
        } else {
          this.$notify({
            message: "请选择tab页后重试！",
            type: "warning"
          });
        }
      },
      compare(val1, val2) {
        return val1.sort - val2.sort;
      },
      removeTab: function () {//删除选中tab页
        //判断当前是否有选中tab项
        if (this.basicFieldParamDeals.TabItem != "" && this.basicFieldParamDeals.TabItem != null) {
          var index = this.tabList.findIndex(node => node.tabName + node.id == this.basicFieldParamDeals.TabItem.tabName + this.basicFieldParamDeals.TabItem.id);
          this.tabList.splice(index, 1);//从源中移除选中项
          this.basicFieldParamDeals.TabItem = this.tabList.length > 0 ? this.tabList[0] : ""//置空当前选中项
        } else {
          this.$notify({
            title: "提示",
            message: "请选择tab页后重试！",
            type: "warning"
          });
        }
        this.visible2 = false;//隐藏确认删除pop
      },
      handleTabClick(tab, event) {//tab页切换点击事件
        var curTab = this.tabList.filter(node => node.tabName + node.id == tab.name)[0];
        //设置当前项
        this.basicFieldParamDeals.TabItem = curTab;
      },
      setDefaultTabItem: function () {//设置默认tab项
        try {
          if (this.tabList.length > 0) {
            //设置默认项
            this.basicFieldParamDeals.TabItem = this.tabList[0];
          }
        } catch (e) {
          console.error(e.message)
        }
      },
      deepCopy: function (source) {
        var result;
        (source instanceof Array) ? (result = []) : (typeof(source) === "object") ? (source === null ? (result = "") : (result = {})) : (result = source);
        for (var key in source) {
          result[key] = (typeof source[key] === 'object') ? this.deepCopy(source[key]) : source[key];
        }
        return result;
      }
    }
  }
</script>

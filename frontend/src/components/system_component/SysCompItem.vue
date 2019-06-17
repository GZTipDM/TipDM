<template>
    <div class="h100p">
        <el-tabs type="card" value="first">
            <el-tab-pane label="基本信息" name="first">
                <!-- 基本信息表单 -->
                <BasicMessage></BasicMessage>
            </el-tab-pane>
            <el-tab-pane label="输入" name="second">
                <!-- 输入表单 -->
                <BasicInputForm></BasicInputForm>
            </el-tab-pane>
            <el-tab-pane label="输出" name="third">
                <!-- 输出表单 -->
                <BasicOutputForm></BasicOutputForm>
            </el-tab-pane>
            <el-tab-pane label="参数" name="fourth">
                <!--添加设置-->
                <BasicFieldParam></BasicFieldParam>
            </el-tab-pane>
            <el-tab-pane label="脚本" name="fifth">
                <!--添加脚本-->
                <BasicScript></BasicScript>
            </el-tab-pane>
        </el-tabs>
        <div class="footBtn">
            <el-button type="primary" @click="saveComponent" v-if="componentsDeals.componentEdit=='add'">添 加</el-button>
            <el-button type="primary" @click="updateComponent"
                       v-else-if="componentsDeals.componentEdit=='edit'">更 新</el-button>
            <el-button @click="resetForm">重置表单</el-button>
        </div>
    </div>
</template>

<script>
  import BasicMessage from './sys_comp_item/BasicMessage.vue'
  import BasicInputForm from './sys_comp_item/BasicInputForm.vue'
  import BasicOutputForm from './sys_comp_item/BasicOutputForm.vue'
  import BasicFieldParam from './sys_comp_item/BasicFieldParam.vue'
  import BasicScript from './sys_comp_item/BasicScript.vue'

  export default {
    name: 'SysCompItem',
    computed: {
      componentsDeals: function () {
        return this.$store.state.systemComponent.componentsDeals;
      }
    },
    components: {
      BasicMessage,
      BasicInputForm,
      BasicOutputForm,
      BasicFieldParam,
      BasicScript
    },
    methods: {
      dealData: function () { //参数组装
        //编辑组件项
        var editComponent = this.componentsDeals.editComponent;
        var data = {};
        data["enabled"] = editComponent.enabled;//组件啊是否可用
        data["engine"] = editComponent.extra.engine;//引擎
//                data["paralleled"] = editComponent.paralleled;//单机/分布式
        data["supportPMML"] = editComponent.supportPMML;//是否支持PMML
        data["allowViewSource"] = editComponent.allowViewSource,//是否支持源码预览
          data["hasReport"] = editComponent.hasReport,//是否支持查看结果
          data["name"] = editComponent.name;//组件名称
        data["targetAlgorithm"] = editComponent.targetAlgorithm; //对应的算法服务类
        data["minimumInput"] = editComponent.minimumInput; //输入最小个数
        data["parentId"] = editComponent.parentId;  //分类ID
        data["iconPath"] = editComponent.iconPath; //图标
        data["script"] = editComponent.script;//脚本
        data["description"] = editComponent.description; // 这个组件的描述
        data["inputs"] = []; //组件输入
        if (editComponent.inputs) {
          editComponent.inputs.forEach(function (item) {
            data["inputs"].push({
              "key": item.key,  //脚本对应名称
              "cat": item.cat,
              "description": item.description  //输入描述
            });
          });
        }
        data["outputs"] = [];//组件输出
        if (editComponent.outputs) {
          editComponent.outputs.forEach(function (item) {
            data["outputs"].push({
              "key": item.key,  //脚本对应名称
              "cat": item.cat,//输入类别
              "description": item.description,  //输出描述
              "access": item.access,//源数据设置
              "canPreview": item.canPreview,//是否能够预览
              "columns": item.columns//添加字段
            });
          });
        }
        data["tabs"] = [];//参数选项卡
        if (editComponent.tabs) {
          editComponent.tabs.forEach(function (item) {
            var childItem = {};
            childItem["tabName"] = item.tabName;//选项卡名称
            childItem["elements"] = []; //参数
            item.elements.forEach(function (ele) {
              childItem["elements"].push({
                "defaultValue": ele.defaultValue, //默认值
                "elementType": ele.elementType,  //参数类型
                "label": ele.label,
                "name": ele.name,   //参数名称
                "placeholder": ele.placeholder,
                "options": ele.options,
                "required": ele.required, //是否必填
                "sequence": ele.sequence,  //排序索引
                "toolTip": ele.toolTip,  //悬浮提示
                "value": ele.value, //参数值
                "rexp": ele.rexp,//正则表达式
                "extra": ele.extra, //额外参数
                "visible": ele.visible //是否可用
              });
            });
            data["tabs"].push(childItem);
          });
        }
        return data;
      },
      saveComponent: function () {
        try {
          var editComponent = this.componentsDeals.editComponent;
          if (editComponent.name.trim().length <= 0) {
            this.$notify({
              title: "提示",
              message: "组件名不能为空",
              type: "warning"
            });
            return;
          }
          if (!editComponent.targetAlgorithm) {
            this.$notify({
              title: "提示",
              message: "算法服务不能为空",
              type: "warning"
            });
            return;
          }
          var inputLength = 0;
          if (editComponent.inputs) {
            inputLength = editComponent.inputs.length;
          }
          if (editComponent.minimumInput > inputLength) {
            this.$notify({
              title: "提示",
              message: "输入Tab项的个数不能小于最小个数" + editComponent.minimumInput,
              type: "warning"
            });
            return;
          }

          var that = this;
          var data = this.dealData();
          this.$.ajax({
            url: that.$store.state.global.httpServer + "/api/component",
            method: "post",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data),
            beforeSend: function(xhr) {
              xhr.setRequestHeader("accessToken",localStorage.accessToken);
            },
            success: function (data) {
              if (data.status == "SUCCESS") {
                that.$notify({
                  title: '成功',
                  message: data.message,
                  type: 'success'
                });
                //移除视图
                that.componentsDeals.currentView = "";
                //获取更新添加目录
                that.$store.dispatch("getMenuComponentsList", 1);
                that.$store.dispatch("getMenuComponentsList", 2);
              } else if (data.status == "FAIL") {
                that.$notify.error({
                  title: '失败',
                  message: data.message,
                });
              }
            },
            error: function (response) {
              that.$store.commit("dealRequestError", response);
            }
          });
        } catch (e) {
          console.error(e.message)
        }
      },
      updateComponent: function () {//更新组件数据
        try {
          var editComponent = this.componentsDeals.editComponent;
          if (editComponent.name.trim().length <= 0) {
            this.$notify({
              title: "提示",
              message: "组件名不能为空",
              type: "warning"
            });
            return;
          }
          var inputLength = 0;
          if (editComponent.inputs) {
            inputLength = editComponent.inputs.length;
          }
          if (editComponent.minimumInput > inputLength) {
            this.$notify({
              title: "提示",
              message: "输入Tab项的个数不能小于最小个数" + editComponent.minimumInput,
              type: "warning"
            });
            return;
          }
          var that = this;
          var data = this.dealData();
          this.$.ajax({
            url: that.$store.state.global.httpServer + "/api/component/" + that.componentsDeals.editComponent.id ,
            method: "put",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data),
            beforeSend: function(xhr) {
              xhr.setRequestHeader("accessToken",localStorage.accessToken);
            },
            success: function (data) {
              if (data.status == "SUCCESS") {
                that.$notify({
                  title: '成功',
                  message: data.message,
                  type: 'success'
                });
                //移除视图
                that.componentsDeals.currentView = "";
                //重载父类目录
                that.$store.dispatch("getMenuComponentsList", 3);
                that.$store.dispatch("getMenuComponentsList", 2);
              } else if (data.status == "FAIL") {
                that.$notify.error({
                  title: '失败',
                  message: data.message,
                });
              }
            },
            error: function (response) {
              that.$store.commit("dealRequestError", response);
            }
          });
        } catch (e) {
          console.error(e.message)
        }
      },
      resetForm: function () {
        this.componentsDeals.editComponent = this.deepCopy(this.componentsDeals.defaultEditComponent);
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

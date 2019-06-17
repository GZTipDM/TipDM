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
      <el-select size="small" v-model="item.extra.tableName" @change="itemChange" filterable remote
                 placeholder="请输入名称进行搜索" :remote-method="remoteMethod" :loading="loading" style="width: 100%">
        <el-option v-for="item in options4" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>
    </el-form-item>
  </el-form>
</template>

<script>
  export default {
    name: 'TipSearchInput',
    props: ["item", "index", "type"],
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
        paddingStyle: {
          paddingLeft: '5px',
          paddingRight: '5px'
        },
        options4: [],//下拉列表数据源
        loading: false,
        fistCreate: false,
        rules: {
          value: [
            {required: this.item.required, validator:valueValidator, trigger: 'change'}
          ]
        }
      }
    },
    computed: {
      nodeItem: function () {
        return this.$store.state.project.flowData.currentNodeItem;
      }
    },
    created: function () {
      this.options4 = this.item.extra[this.item.name + 'Items'];
      this.fistCreate = true;
    },
    methods: {
      remoteMethod(query) {
        //屏蔽控件渲染过程中触发搜索
        if (query == this.item.extra.tableName) {
          return;
        }
        //屏蔽初次渲染触发搜索
//                if(this.fistCreate) {
//                    this.fistCreate = false;
//                    return;
//                }
        var that = this;
        if (query !== '') {
          this.loading = true;
          setTimeout(() => {
            this.loading = false;
            var url = "";
            switch (this.type) {
              case "database":
                url = that.$store.state.global.httpServer + "/api/datasource/filter?prefix=" + query + "&exclude=HIVE&exclude=HDFS&limit=20";
                break;
              case "hive":
                url = that.$store.state.global.httpServer + "/api/datasource/filter?prefix=" + query + "&exclude=FLAT_FILE&exclude=RDBMS&limit=20";
                break;
            }
            this.$.ajax({
              url: url,
              method: "get",
              beforeSend: function(xhr) {
                xhr.setRequestHeader("accessToken",localStorage.accessToken);
              },
              success: function (response) {
                var result = response.data;
                result.forEach(function (item) {
                  item.id = item.id.toString();
                });
                that.options4 = response.data;
              },
              error: function (response) {
                that.$store.commit("dealRequestError", response);
              }
            });
          }, 200);
        } else {
          this.options4 = [];
        }
      },
      itemChange: function (value) {
        setTimeout(() => {
          if (this.options4.length == 0) {
            this.itemChange(value);
          } else {
            var that = this;
            try {
              that.item.extra[that.item.name + 'Items'] = this.options4;
              //数据源Id
              var curOptionId = value;
              this.item.value = curOptionId;
              //选中数据表的同时修改对应output中项的value值
              if (this.nodeItem.outputs) {
                this.nodeItem.outputs.forEach(function (item) {
                  item.value = curOptionId;
                });
              }
              //根据名称获取数据结构
              this.getTableStructure(curOptionId);
            } catch (e) {
              console.error(e.message)
            }
          }
        }, 200);
      },
      getTableStructure: function (tableId) {
        try {
          var that = this;
          var url = "";
          switch (this.type) {
            case "database":
              url = that.$store.state.global.httpServer + "/api/datasource/table/" + tableId + "/structure";
              break;
            case "hive":
              url = that.$store.state.global.httpServer + "/api/hive/default/structure?tableName=" + tableId;
              break;
          }
          this.$.ajax({
            url: url,
            method: "get",
            type: "json",
            beforeSend: function(xhr) {
              xhr.setRequestHeader("accessToken",localStorage.accessToken);
            },
            success: function (response) {
              var fieldData = response.data;
              that.nodeItem.tabs.forEach(function (tab) {
                tab.elements.forEach(function (ele) {
                  if (ele.elementType == 5 || ele.elementType == 13) {
                    fieldData.forEach(function (item, index) {
                      item["index"] = index;
                      //当获取的数值结构为numeric类型时默认值为0
                      item["otherParam"] = 0;
                    });
                    var oldLabel = ele.label;
                    ele.value = "";
                    ele.label = "";
                    ele.extra[ele.name + 'Items'] = fieldData;
                    ele.label = oldLabel;
                  }
                });
              });
              if (that.nodeItem.inputs.length > 0) {
                that.$notify({
                  type: "warning",
                  message: "可以接受数据输入的节点不支持该组件"
                });
              } else {
                //默认输出所有字段
                that.nodeItem.outputData = [];
                that.nodeItem.outputData.push({
                  key: "database",
                  dataFields: fieldData
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
      }
    }
  }
</script>

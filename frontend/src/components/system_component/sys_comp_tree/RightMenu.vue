<template>
  <div class="context_menu" :style="positionStyle">
    <div class="content">
      <ul>
        <template v-if="!componentItem.component">
          <li @click="renameClass"><i class="fa fa-edit"></i>重命名</li>
          <li @click="addChildClass"><i class="fa fa-folder"></i>添加子类别</li>
          <li @click="createComponent"><i class="fa fa-cubes"></i>新增组件</li>
          <li @click="deleteClassItem"><i class="fa fa-trash-o"></i>删除</li>
        </template>
        <template v-else-if="componentItem.component">
          <li @click="handleDeleteComponentItem"><i class="fa fa-trash-o"></i>删除</li>
        </template>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
    computed: {
      positionStyle: function () {
        var msg = this.$store.state.systemComponent.componentsDeals.componentsContextMenuMsg;
        return {
          left: msg.left + 'px',
          top: msg.top + 'px',
          display: msg.display
        }
      },
      componentsDeals: function () {//交互集
        return this.$store.state.systemComponent.componentsDeals;
      },
      componentItem: function () {//当前要操作的目录项
        return this.componentsDeals.currentCompItem;
      },

    },
    methods: {
      deleteClassItem: function () {//删除分类及分类下的所有文件
        var that = this;
        this.$confirm('此操作将删除该分类及分类下的所有文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          that.deleteChildClass();
        }).catch(() => {
        });
      },
      deleteChildClass: function () {//执行删除分类
        var that = this;
        this.$.ajax({
          url: that.$store.state.global.httpServer + "/api/cat/" + that.componentItem.id,
          method: "delete",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          success: function (data) {
            if (data.status == "SUCCESS") {
              that.$notify({
                type: 'success',
                message: '删除成功!'
              });
              //获取父目录下子目录
              that.$store.dispatch("getMenuComponentsList", 3);
            } else if (data.status == "FAIL") {
              that.$store.commit("dealRequestError", {
                status: 405,
                responseJSON: {
                  message: data.message
                }
              });
            }
          },
          error: function (response) {
            that.$store.commit("dealRequestError", response);
          }
        });
      },
      addChildClass: function () {//添加目录文件夹
        var that = this;
        this.$prompt('请输入分类名称', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPlaceholder: "请输入分类名称",
          inputPattern: /\S{1,}/,
          inputErrorMessage: '分类名称不能为空'
        }).then(({value}) => {
          var data = {};
          data["name"] = value;
          data["parentId"] = this.componentItem.id;
          this.$.ajax({
            url: that.$store.state.global.httpServer + "/api/cat",
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
                //获取添加子目录
                that.$store.dispatch("getMenuComponentsList", 1);
              } else if (data.status == "FAIL") {
                that.$store.commit("dealRequestError", {
                  status: 405,
                  responseJSON: {
                    message: data.message
                  }
                });
              }
            },
            error: function (response) {
              that.$store.commit("dealRequestError", response);
            }
          });
        }).catch(() => {
        });
      },
      renameClass: function () {//重命名分类
        var that = this;
        this.$prompt('请输入分类重命名', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPlaceholder: "请输入分类重命名",
          inputValue: this.componentItem.name,
          inputPattern: /\S{1,}/,
          inputErrorMessage: '分类名称不能为空'
        }).then(({value}) => {
          this.$.ajax({
            url: that.$store.state.global.httpServer + "/api/cat/" + this.componentItem.id + "?name=" + value ,
            method: "patch",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
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
                //获取添加子目录
                that.$store.dispatch("getMenuComponentsList", 3);
              } else if (data.status == "FAIL") {
                that.$store.commit("dealRequestError", {
                  status: 405,
                  responseJSON: {
                    message: data.message
                  }
                });
              }
            },
            error: function (response) {
              that.$store.commit("dealRequestError", response);
            }
          });
        }).catch(() => {
        });
      },
      createComponent: function () {//添加工程叶子节点
        this.componentsDeals.componentEdit = "add";//修改组件状态为添加
        //重置添加组件的默认状态
        this.componentsDeals.editComponent = this.deepCopy(this.componentsDeals.defaultEditComponent);
        //设置添加组件的类
        this.componentsDeals.editComponent.parentId = this.componentsDeals.currentCompItem.id;

        //设置基本信息中的目录树选中目录
        this.componentsDeals.basicMessageSelectClass = this.componentsDeals.currentCompItem;
        //设置当前展示组件
        this.componentsDeals.currentView = "";
        var that = this;
        setTimeout(function () {
          that.componentsDeals.currentView = "SysCompItem";
        }, 10);
      },
      handleDeleteComponentItem: function () {//删除组件警告框
        var that = this;
        this.$confirm('此操作将删除该组件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          that.deleteComponentItem();
        }).catch(() => {
        });
      },
      deepCopy: function (source) {
        var result;
        (source instanceof Array) ? (result = []) : (typeof(source) === "object") ? (source === null ? (result = "") : (result = {})) : (result = source);
        for (var key in source) {
          result[key] = (typeof source[key] === 'object') ? this.deepCopy(source[key]) : source[key];
        }
        return result;
      },
      deleteComponentItem: function () {//删除组件
        var delId = this.componentsDeals.currentCompItem.id;
        if (delId == 0) {
          this.$notify({
            title: "提示",
            message: "未选择组件！！",
            type: "warning"
          });
          return;
        }
        var that = this;
        this.$.ajax({
          url: that.$store.state.global.httpServer + "/api/component/" + delId,
          method: "delete",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          success: function (data) {
            if (data.status == "SUCCESS") {
              that.$notify({
                type: 'success',
                message: '删除成功!'
              });
              that.$store.dispatch("getMenuComponentsList", 3);
              that.componentsDeals.currentView = "";//隐藏
            } else if (data.status == "FAIL") {
              that.$store.commit("dealRequestError", {
                status: 405,
                responseJSON: {
                  message: data.message
                }
              });
            }
          },
          error: function (response) {
            that.$store.commit("dealRequestError", response);
          }
        });
      }
    }
  }
</script>

<style>
  .context_menu {
    background: #fff;
    padding: 6px 0;
    border: 1px solid #ccc;
    box-shadow: 0 0 6px rgba(102, 102, 102, 0.3);
    min-width: 150px;
    z-index: 99;
    position: absolute;
    border-radius: 2px;
  }

  .context_menu .content ul li {
    line-height: 26px;
    height: 26px;
    display: block;
    padding: 0 16px;
    cursor: pointer;
  }

  .context_menu .content ul li i {
    color: #999;
    margin-right: 8px;
    width: 16px;
    text-align: right;
  }

  .context_menu .content ul li:hover {
    background: #fbe8ca;
    color: #000;
  }

  .context_menu .content ul li:hover i {
    color: #666;
  }

  .context_menu .content ul li.iLine {
    height: 10px;
    position: relative;
    cursor: default;
  }

  .context_menu .content ul li.iLine:hover {
    background: none;
  }

  .context_menu .content ul li.iLine:before {
    content: "";
    height: 1px;
    border-top: 1px dotted #dfdfdf;
    width: calc(100% - 20px);
    position: absolute;
    top: 4px;
    left: 10px;
  }


</style>

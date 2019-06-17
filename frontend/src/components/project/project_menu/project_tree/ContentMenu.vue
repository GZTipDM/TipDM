<template>
  <div class="context_menu" :style="positionStyle">
    <div class="content">
      <ul>
        <template v-if="rightContentTargetItem.id==0">
          <li @click="createProject"><i class="fa fa-cube"></i>新建工程</li>
          <li @click="addChildDirectory"><i class="fa fa-folder"></i>添加文件夹</li>
        </template>
        <template v-else-if="!rightContentTargetItem.leaf">
          <li @click="createProject"><i class="fa fa-cube"></i>新建工程</li>
          <hr/>
          <li @click="addChildDirectory"><i class="fa fa-folder"></i>添加文件夹</li>
          <li @click="deleteItem"><i class="fa fa-trash-o"></i>删除文件夹</li>
        </template>
        <template v-else-if="rightContentTargetItem.leaf">
          <li @click="deleteProjectItem"><i class="fa fa-trash-o"></i>删除工程</li>
          <li @click="updateProjectDetail"><i class="fa fa-file"></i>修改工程信息</li>
          <li @click="saveAsNewProject" title="另存为工程"><i class="fa fa-floppy-o"></i>另存为工程</li>
          <li @click="saveAsTemplate"><i class="fa fa-floppy-o"></i>另存为模板</li>
        </template>
      </ul>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from "vuex"

  export default {
    name: 'ContentMenu',
    computed: {
      ...mapGetters([
        "httpServer",
        "permission"
      ]),
      positionStyle: function () {
        var msg = this.$store.state.project.flowData.projectContextMenuMsg;
        return {
          left: msg.left + 'px',
          top: msg.top + 'px',
          display: msg.display
        }
      },
      flowData: function () {
        return this.$store.state.project.flowData;
      },
      rightContentTargetItem: function () {
        return this.flowData.rightContentTargetItem
      },
      showDetail() {
        return this.$store.state.project.projectMenuModel.showDetail;
      }
    },
    methods: {
      //修改工程描述详情
      updateProjectDetail() {
        this.showDetail.updateProjectDetail = true;
      },
      //删除目录及目录下的所有文件
      deleteItem: function () {
        var that = this;
        this.$confirm('此操作将删除该目录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          that.deleteDirectory();
        }).catch(() => {
        });
      },
      //删除文件夹时，需先清空子目录
      deleteDirectory: function () {

        //删除工程目录时，需要先删除目录下的子项
        if (this.rightContentTargetItem.children.length > 0) {
          this.$notify.error({
            message: "删除失败，删除子目录后重试！"
          });
          return;
        }
        var that = this;
        this.$.ajax({
          url: that.httpServer + "/api/project/cat/" + that.rightContentTargetItem.id ,
          method: "delete",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          success: function (data) {
            switch (data.status) {
              case "SUCCESS":
                that.$notify({
                  title: "提示",
                  type: 'success',
                  message: '删除成功!'
                });
                that.$store.dispatch("getMenuProjectList", 3);//重新加载父目录下子目录
                break;
              case "FAIL":
                that.$notify.error({
                  title: '提示',
                  message: data.message
                });
                break;
            }
          },
          error: function (response) {
            that.$store.commit("dealRequestError", response);
          }
        });
      },
      //添加目录文件夹
      addChildDirectory: function () {
        //如果当前项为文件夹时，设置当前项为父类项
        this.flowData.currentParentItem = this.flowData.rightContentTargetItem;
        var that = this;
        this.$prompt('请输入文件夹名称', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPlaceholder: "请输入文件夹名称",
          inputValidator(val) {
            var reg = /^(?! +$).+/;
            if (!reg.test(val) || val == null) {
              return "名称不能为空或空字符串";
            } else if (val.trim().length > 25) {
              return "名称长度限制1-25字符";
            } else {
              return true;
            }
          }
        }).then(({value}) => {
          var data = {};
          data["name"] = value;
          data["parentId"] = this.rightContentTargetItem.id;
          this.$.ajax({
            url: that.httpServer + "/api/project/cat",
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
                that.$store.dispatch("getMenuProjectList", 3);
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
        }).catch(() => {
        });
      },
      //添加工程叶子节点
      createProject: function () {
        this.showDetail.showProjectDialog = true;
      },
      //另存为工程
      saveAsNewProject: function () {
        //设置目标目录为当前项的父目录
        this.flowData.rightContentTargetItem = this.flowData.currentParentItem;
        this.showDetail.saveAsNewProject = true;
      },
      //删除工程目录警告框
      deleteProjectItem: function () {
        var that = this;
        this.$confirm('此操作将删除该工程, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          that.deleteProject();
        }).catch(() => {
        });
      },
      //删除工程
      deleteProject: function () {
        if (this.flowData.isRunning) {
          this.$notify({
            title: "提示",
            message: "有程序正在运行，请稍后再试！",
            type: "warning"
          });
          return;
        }
        var that = this;
        this.$.ajax({
          url: that.httpServer + "/api/project/" + that.rightContentTargetItem.project.id,
          method: "delete",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          success: function (data) {
            if (data.status == "SUCCESS") {
              that.$notify({
                title: "提示",
                type: 'success',
                message: '删除成功!'
              });
              that.$store.dispatch("getMenuProjectList", 3);//更新根目录
              that.$store.commit("cleanCurFlowData");//清理当前工程的数据
            } else if (data.status == "FAIL") {
              that.$notify.error({
                title: '提示',
                message: '删除失败!'
              });
            }
          },
          error: function (response) {
            that.$store.commit("dealRequestError", response);
          }
        });
      },
      //另存为模板
      saveAsTemplate() {
        this.showDetail.saveAsTemplateDialog = true;
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

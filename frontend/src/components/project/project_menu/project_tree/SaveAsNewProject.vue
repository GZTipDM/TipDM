<template>
  <el-dialog title="另存为工程" top="10%" :visible.sync="showDetail.saveAsNewProject" align="center">
    <div style="height:350px; overflow: auto">
      <el-form :rules="rules" :model="projectDetail" ref="projectDetail" label-width="100px">
        <el-form-item label="原工程名称">
          <el-input :value="currentProjectDetail.name" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="新工程名称" prop="projectName">
          <el-input v-model.trim="projectDetail.projectName" placeholder="请定义工程名称"></el-input>
        </el-form-item>
        <el-form-item label="工程位置" align="left" style="height: 210px; overflow: auto;">
          <div class="tree-menu" id="createProjectTree">
            <ul v-for="(menuItem,index) in projectList">
              <SelectTree :item="menuItem"></SelectTree>
            </ul>
          </div>
        </el-form-item>
      </el-form>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="showDetail.saveAsNewProject=false">取消</el-button>
      <el-button :disabled="disabled" type="primary" @click="submitForm">确定</el-button>
    </div>
  </el-dialog>
</template>
<script>
  import SelectTree from "./create_project_dialog/SelectTree.vue";

  export default {
    name: 'SaveAsNewProject',
    data() {
      return {
        rules: {//校验规则
          projectName: [
            {required: true, message: '请输入名称', trigger: 'blur'},
            {min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur'}
          ]
        },
        disabled: false, //确认按钮点击后并不能重复点击
        projectDetail: {
          projectName: ""//另存为工程名称

        }
      }
    },
    computed: {
      flowData: function () {
        return this.$store.state.project.flowData;
      },
      showDetail: function () {//弹出框是否显示
        return this.$store.state.project.projectMenuModel.showDetail;
      },
      rightContentTargetItem: function () {
        return this.flowData.rightContentTargetItem;
      },
      projectList: function () { //工程列表数据
        return this.$store.state.project.projectMenu;
      },
      currentProjectDetail: function () {
        return this.flowData.rightContentTargetProject;
      }
    },
    components: {
      SelectTree
    },
    methods: {
      submitForm(formName) {//确认
        this.disabled = true;
        this.$refs.projectDetail.validate((valid) => {
          if (valid) {
            this.createProject();
          } else {
            console.log('error submit!!');
            this.disabled = false;
            return false;
          }
        });
      },
      createProject: function () {
        try {
          //如果当前项为文件夹时，设置当前项为父类项
          this.flowData.currentParentItem = this.flowData.rightContentTargetItem;
          var that = this;
          var projectId = this.currentProjectDetail.id;
          var parentId = this.rightContentTargetItem.id;
          var projectName = this.projectDetail.projectName;
          this.$.ajax({
            url: that.$store.state.global.httpServer + "/api/project/" + projectId + "/saveAs/" + parentId + "?asName=" + projectName,
            method: "post",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
              xhr.setRequestHeader("accessToken",localStorage.accessToken);
            },
            success: function (data) {
              switch (data.status) {
                case "SUCCESS":
                  that.$notify({
                    title: '成功',
                    message: data.message,
                    type: 'success'
                  });
                  //设置状态为正在切换
                  that.flowData.projectChange = true;
                  //隐藏对话框
                  that.showDetail.saveAsNewProject = false;
                  //获取添加子目录
                  that.$store.dispatch("getMenuProjectList", 3);
                  that.flowData.currentProjectDetail.id = data.data;
                  //设置当前添加项为当前选中项
                  that.setCurrentSelectItem(data.data);
                  //获取添加的工程及工程数据
                  that.$store.dispatch("getCurrentFlowJson", data.data);
                  that.projectDetail.projectName = "";
                  break;
                case "FAIL":
                  that.$notify.error({
                    title: "错误",
                    message: data.message
                  });
                  break;
              }
              //修改按钮的可被点击状态
              that.disabled = false;
            },
            error: function (response) {
              that.$store.commit("dealRequestError", response);
              //修改按钮的可被点击状态
              that.disabled = false;
            }
          });
        } catch (e) {
          console.error(e.message)
        }
      },
      setCurrentSelectItem: function (docId) {
        var that = this;
        try {
          setTimeout(function () {
            var currentItem = that.flowData.currentParentItem.children.filter(node => node.project ? (node.project.id == docId) : false);
            if (currentItem.length == 0) {
              that.setCurrentSelectItem(docId);
            } else {
              that.flowData.currentProjectItem = currentItem[0];
            }
          }, 200);
        } catch (e) {
          console.error(e.message)
        }
      }
    }
  }
</script>

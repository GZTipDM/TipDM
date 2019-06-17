<!--首页快速创建工程弹出框-->
<template>
  <el-dialog title="复制模板工程" :visible.sync="showDetail.copyTemplateDialog" align="center">
    <div style="height:360px; overflow: auto">
      <el-form :model="createDetail" :rules="rules" ref="createDetail" label-width="100px">
        <el-form-item label="模板工程名称">
          <el-input :disabled="true"
                    :value="currentTemplate.project?currentTemplate.project.name:''"></el-input>
        </el-form-item>
        <el-form-item label="工程名称" prop="projectName">
          <el-input v-model="createDetail.projectName" placeholder="请定义名称"></el-input>
        </el-form-item>
        <el-form-item label="工程位置" align="left">
          <div class="tree-menu" id="createProjectTree" style="height: 210px; overflow: auto;">
            <ul v-for="(menuItem,index) in projectList" :key="menuItem.id">
              <SelectTree :item="menuItem"></SelectTree>
            </ul>
          </div>
        </el-form-item>
      </el-form>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="showDetail.copyTemplateDialog=false">取消</el-button>
      <el-button :disabled="disabled" type="primary" @click="submitForm">确定</el-button>
    </div>
  </el-dialog>
</template>
<style scoped>
  .el-form-item {
    margin-bottom: 20px;
  }
</style>
<script>
  import SelectTree from './create_project_dialog/SelectTree.vue'

  export default {
    name: 'CopyTemplateAsProject',
    data() {
      return {
        rules: {//校验规则
          projectName: [
            {required: true, message: '请输入名称', trigger: 'blur'},
            {min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur'}
          ]
        },
        disabled: false, //确认按钮点击后并不能重复点击
        createDetail: {//创建工程的信息
          projectName: "",//工程名
        },
      }
    },
    computed: {
      flowData: function () {
        return this.$store.state.project.flowData;
      },
      showDetail: function () {//弹出框是否显示
        return this.$store.state.main.showDetail;
      },
      projectList: function () { //工程列表数据
        return this.$store.state.project.projectMenu;
      },
      rightContentTargetItem: function () {
        return this.flowData.rightContentTargetItem;
      },
      currentTemplate: function () {//当前编辑模板
        return this.$store.state.main.currentTemplate;
      },
      saveAsTemplateDialog: function () {
        return this.showDetail.saveAsTemplateDialog;
      }
    },
    watch: {
      saveAsTemplateDialog: function () {
        if (this.disabled) {
          this.disabled = false;
        }
      }
    },
    components: {
      SelectTree
    },
    methods: {
      submitForm() {//确认
        this.$refs.createDetail.validate((valid) => {
          if (valid) {
            this.disabled = true;
            this.cloneTemplate();
          } else {
            return false;
          }
        });
      },
      cloneTemplate: function () {
        //如果当前项为文件夹时，设置当前项为父类项
        this.flowData.currentParentItem = this.flowData.rightContentTargetItem;
        var that = this;
        var newProjectName = this.createDetail.projectName;
        var docId = this.rightContentTargetItem.id;
        var templateProId = this.currentTemplate.project.id;
        this.$.ajax({
          url: that.$store.state.global.httpServer + "/api/project/" + templateProId + "/clone/" + docId + "?newName=" + newProjectName,
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
                //隐藏对话框
                that.showDetail.copyTemplateDialog = false;
                that.disabled = false;
                that.$refs.createDetail.resetFields();
                //获取添加子目录
                that.$store.dispatch("getMenuProjectList", 3);
                //获取添加的工程及工程数据
                that.$store.dispatch("getCurrentFlowJson", data.data);
                //设置当前添加项为当前选中项
                that.setCurrentSelectItem(data.data);
                //通过路由跳转到工程页面
                that.$router.push({path: '/home/project'});
                //设置菜单项为工程管理(状态)
                that.change();
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
      },
      change: function () {//设置工程项为选中项
        var that = this;
        this.$store.state.navMenu.forEach(function (node) {
          if (node.name == "工程") {
            node.selected = true;
          } else {
            node.selected = false;
          }
        });
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

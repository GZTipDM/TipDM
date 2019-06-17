<!--首页快速创建工程弹出框-->
<template>
  <el-dialog title="创建工程" :visible.sync="showDetail.showFastCreateProjectDialog" align="center">
    <div style="height:360px; overflow: auto">
      <el-form @submit.native.prevent :model="createDetail" :rules="rules" ref="createDetail" label-width="100px">
        <el-form-item label="工程名称" prop="projectName">
          <el-input v-model="createDetail.projectName" placeholder="请定义名称"
                    @keyup.enter.native="submitForm('createDetail')"></el-input>
        </el-form-item>
        <el-form-item label="工程描述" prop="describe">
          <el-input type="textarea" v-model="createDetail.describe" placeholder="描述" :maxlength="500"></el-input>
        </el-form-item>
        <el-form-item label="工程位置" align="left" style="height: 140px; overflow: auto;">
          <div class="tree-menu" id="createProjectTree">
            <ul v-for="(menuItem,index) in projectList" :key="menuItem.id">
              <SelectTree :item="menuItem"></SelectTree>
            </ul>
          </div>
        </el-form-item>
      </el-form>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="resetForm">重置</el-button>
      <el-button :disabled="disabled" type="primary" @click="submitForm('createDetail')">确定</el-button>
    </div>
  </el-dialog>
</template>
<style scoped>
  .el-form-item {
    margin-bottom: 20px;
  }
</style>
<script>
  import SelectTree from './create_project_dialog/SelectTree.vue';

  export default {
    name: 'FastCreateProjectDialog',
    data() {
      var projectNameValidator = (rule, value, callback) => {
        var reg = /^(?! +$).+/;
        if (!reg.test(value) || value == null) {
          callback(new Error("不能为空或空字符串"));
        } else if (value.trim().length > 100) {
          callback(new Error("长度在 1 到 100 个字符"));
        }
        callback();
      };
      return {
        rules: {//校验规则
          projectName: [
            {required: true, validator: projectNameValidator, trigger: 'blur'},
          ],
          describe: [
            {required: false, message: '请填写描述信息', trigger: 'blur'}
          ]
        },
        disabled: false, //确认按钮点击后并不能重复点击
        createDetail: {//创建工程的信息
          projectName: "",//工程名
          describe: "",//描述
          paralleled: false,//模式，是否支持分布式
          parentId: ""//父类id
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
      rightContentTargetItem: function () {
        return this.flowData.rightContentTargetItem;
      },
      projectList: function () { //工程列表数据
        return this.$store.state.project.projectMenu;
      }
    },
    components: {
      SelectTree
    },
    beforeDestroy: function () {
      this.resetForm();
    },
    methods: {
      resetForm() {//重置
        //this.$refs.createDetail.resetFields();
        this.createDetail.projectName = "";
        this.createDetail.describe = "";
        this.createDetail.paralleled = false;
      },
      submitForm(formName) {//确认
        this.disabled = true;
        this.$refs[formName].validate((valid) => {
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
        //如果当前项为文件夹时，设置当前项为父类项
        this.flowData.currentParentItem = this.flowData.rightContentTargetItem;
        var that = this;
        var data = {};
        data["paralleled"] = this.createDetail.paralleled;
        data["name"] = this.createDetail.projectName;
        data["parentId"] = this.rightContentTargetItem.id;
        data["description"] = this.createDetail.describe;
        this.$.ajax({
          url: that.$store.state.global.httpServer + "/api/project/",
          method: "post",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          data: JSON.stringify(data),
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
                that.showDetail.showFastCreateProjectDialog = false;
                //获取添加子目录
                that.$store.dispatch("getMenuProjectList", 3);
                that.flowData.currentProjectDetail.id = data.data;
                //设置当前添加项为当前选中项
                that.setCurrentSelectItem(data.data);
                //获取添加的工程及工程数据
                that.$store.dispatch("getCurrentFlowJson", data.data);
                that.resetForm();
                //跳转到工程页面
                that.$router.push({path: '/home/project'});
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
        this.$store.state.global.navMenu.forEach(function (node) {
          if (node.id == 3) {
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
              //设置当前操作工程信息
              that.flowData.currentProjectDetail = currentItem[0].project;
//                            that.$store.state.menuProject.workFlowSocket = null;
              that.$store.commit("updateMenuProjectSelectComponent", "ProjectContentMsg");
            }
          }, 200);
        } catch (e) {
          console.error(e.message)
        }
      }
    }
  }
</script>

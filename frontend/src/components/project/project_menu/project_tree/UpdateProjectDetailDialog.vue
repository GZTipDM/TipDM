<template>
  <el-dialog title="修改工程描述" :visible.sync="showDetail.updateProjectDetail" align="center">
    <div style="height:270px; overflow: auto">
      <el-form :rules="rules" :model="projectDetail" ref="projectDetail" label-width="100px">
        <el-form-item label="工程名称">
          <el-input :value="projectDetail.projectName" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="工程描述" prop="projectDesc">
          <el-input type="textarea" :autosize="{ minRows: 6, maxRows: 8}"
                    v-model="projectDetail.projectDesc" :maxlength="500"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="showDetail.updateProjectDetail=false">取消</el-button>
      <el-button :disabled="disabled" type="primary" @click="submitForm">确定</el-button>
    </div>
  </el-dialog>
</template>
<script>
  export default {
    name: 'UpdateProjectDetailDialog',
    data() {
      return {
        rules: {//校验规则
          projectDesc: [
            {required: true, message: '请输入描述信息', trigger: 'blur'},
            {min: 1, max: 500, message: '长度在 1 到 500 个字符', trigger: 'blur'}
          ]
        },
        disabled: false, //确认按钮点击后并不能重复点击
        projectDetail: {
          projectName: "",//工程名
          projectDesc: ""//描述信息
        }
      }
    },
    watch: {
      updateProjectDetail(val) {
        if (val) {
          this.projectDetail.projectName = this.rightContentTargetItem.project ? this.rightContentTargetItem.project.name : "";
          this.projectDetail.projectDesc = this.rightContentTargetItem.project.description;
        } else {
          this.projectDetail.projectDesc = "";
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
      currentProjectDetail: function () {
        return this.flowData.currentProjectDetail;
      },
      updateProjectDetail() {
        return this.showDetail.updateProjectDetail;
      }
    },
    methods: {
      submitForm(formName) {//确认
        this.disabled = true;
        this.$refs.projectDetail.validate((valid) => {
          if (valid) {
            this.updateProject();
          } else {
            console.log('error submit!!');
            this.disabled = false;
            return false;
          }
        });
      },
      updateProject() {
        var that = this;
        this.$.ajax({
          url: that.$store.state.global.httpServer + "/api/project/modify/" + that.rightContentTargetItem.project.id + "/desc?desc=" + that.projectDetail.projectDesc,
          method: "patch",
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
                  message: data.message
                });
                that.$store.dispatch("getMenuProjectList", 3);//重新加载父目录下子目录
                that.showDetail.updateProjectDetail = false;
                if (that.currentProjectDetail.id == that.rightContentTargetItem.project.id) {
                  that.currentProjectDetail.description = that.projectDetail.projectDesc;
                }
                break;
              case "FAIL":
                that.$notify.error({
                  title: '提示',
                  message: data.message
                });
                break;
            }
          },
          error: function (data) {
            that.$store.commit("dealRequestError", data);
          },
          complete: function (data) {
            that.disabled = false;
          }
        });
      }
    }
  }
</script>

<template>
  <el-dialog top="15vh" title="保存为模板" :visible.sync="showDetail.saveAsTemplateDialog" align="center">
    <div style="height:200px;">
      <el-form label-width="100px" :model="details" :rules="rules" ref="details">
        <el-form-item label="工程名称">
          <el-input :value="rightContentTargetItem.name" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="标签" prop="tags">
          <el-input type="textarea" resize="none" :rows="4" :autosize="{ minRows: 4, maxRows: 6 }" v-model="details.tags"
                    placeholder="请输入内容,多个标签之间用英文逗号分隔"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="showDetail.saveAsTemplateDialog = false">取消</el-button>
      <el-button :disabled="disabled" type="primary" @click="submitForm('details')">确定</el-button>
    </div>
  </el-dialog>
</template>
<script>
  export default {
    name: 'SaveAsTemplateDialog',
    data() {
      var tagsValidator = (rule, value, callback) => {
        var reg = /^(?! +$).+/;
        var patten = /[`~!@#\$%\^\&\*\(\)_\+<>\?:"\{\}\.\\\/;'\[\]]/im;
        if (!reg.test(value) || value == null) {
          callback(new Error("不能为空或空字符串"));
        } else if (value.trim().length > 100) {
          callback(new Error("长度在 1 到 100 个字符"));
        } else if(patten.test(value)) {
          callback(new Error("输入不能包含特殊字符！"));
        }
        callback();
      };
      return {
        details: {
          tags: "", //标签
        },
        disabled: false, //确认按钮点击后并不能重复点击
        rules: {//校验规则
          tags: [
            {required: true, validator: tagsValidator, trigger: 'blur'},
          ],
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
      saveAsTemplateDialog() {
        return this.showDetail.saveAsTemplateDialog;
      }
    },
    watch: {
      saveAsTemplateDialog(val) {//避免异常导致确定按钮一直无法点击
        if (this.disabled) {
          this.disabled = false;
        }
      }
    },
    methods: {
      submitForm(formName) {//确认
        this.disabled = true;
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.saveAsTemplate();
          } else {
            console.log('error submit!!');
            this.disabled = false;
            return false;
          }
        });
      },
      saveAsTemplate: function () {
        var that = this;
        var arr = this.details.tags.split(",");
        var tagStr = "";
        arr.forEach(function (item) {
          tagStr += "&tags=" + item;
        });
        this.$.ajax({
          url: that.$store.state.global.httpServer + "/api/project/" + that.rightContentTargetItem.project.id + "/saveAsTemplate?1=1" + tagStr,
          method: "put",
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
                that.showDetail.saveAsTemplateDialog = false;
                that.details.tags = "";
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
      }
    }
  }
</script>

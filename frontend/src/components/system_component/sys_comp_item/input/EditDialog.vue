<template>
  <el-dialog :title="basicInputDeals.editStatue=='edit'?'编辑输入':'添加输入'"
             :visible.sync="basicInputDeals.showDialog" align="center">
    <el-form :model="editItem" :rules="rules2" ref="editItem" label-width="7em"
             style="height:200px;overflow-y: auto;padding-right:10px;">
      <el-form-item label="key" prop="key">
        <el-input v-model="editItem.key" :disabled="editItem.model" maxlength="15"></el-input>
      </el-form-item>
      <el-form-item label="输入类别" align="left">
        <el-select v-model="editItem.cat" style="width: 100%">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input type="textarea" placeholder="输入数据的描述" v-model.trim="editItem.description"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm('editItem')">确 认</el-button>
      <el-button @click="basicInputDeals.showDialog = false">取 消</el-button>
    </span>
  </el-dialog>
</template>
<style>
</style>
<script>
  export default {
    name: 'EditDialog',
    data: function () {
      var validateKey = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入名称'));
        } else {
          if (this.editItem.key !== '') {
            var reg = /^\w+$/;//数字、26个英文字母或者下划线组成的字符串
            if (!reg.test(value)) {
              callback(new Error('请使用数字、26个英文字母或者下划线组成的字符串'));
            }
            callback();
          }
          callback();
        }
      };
      return {
        rules2: {
          key: [
            {validator: validateKey, trigger: 'blur'},
            {min: 1, max: 15, message: '长度在 1 到 15 个字符', trigger: 'blur'}
          ],
          description: [
            {required: true, message: '请输入描述信息', trigger: 'blur'},
            {min: 1, max: 300, message: '长度在 1 到 300 个字符', trigger: 'blur'}
          ]
        },
        options: [
          {value: "DATA", label: "数据"},
          {value: "MODEL", label: "模型"},
          {value: "UNSTRUCT", label: "非结构化文件"}
        ]
      }
    },
    computed: {
      basicInputDeals: function () {//弹出框状态
        return this.$store.state.systemComponent.componentsDeals.basicInputDeals;
      },
      editItem: function () {//当前编辑项
        return this.basicInputDeals.editItem;
      },
      editComponent: function () {
        return this.$store.state.systemComponent.componentsDeals.editComponent;
      }
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.editSave();
          } else {
            return false;
          }
        });
      },
      editSave: function () {//确定
        try {
          if (this.basicInputDeals.editStatue == "add") {//新增时
            if (this.editComponent.inputs.length < this.editComponent.minimumInput) {
              var newItem = new Object();
              newItem.key = this.editItem.key;
              newItem.cat = this.editItem.cat;
              newItem.description = this.editItem.description;
              this.editComponent.inputs.push(newItem);//添加
            } else {
              this.$notify({
                type: 'warning',
                message: '添加输入项超出设定值，请在【基础信息】选项修改后重试!'
              });
            }
          } else if (this.basicInputDeals.editStatue == "edit") {//修改时
            var that = this;
            var inputItem = this.editComponent.inputs.filter(node => node.id == that.editItem.id)[0];
            inputItem.key = this.editItem.key;
            inputItem.cat = this.editItem.cat;
            inputItem.description = this.editItem.description;
          }
          this.$refs.editItem.resetFields();
        } catch (e) {
          console.error(e.message);
        }
        this.basicInputDeals.showDialog = false;
      },
      modelChange: function (val) {
        if (val) {
          this.editItem.key = "model";
        }
      }
    }
  }
</script>

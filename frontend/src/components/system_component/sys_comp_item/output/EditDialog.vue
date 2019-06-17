<template>
  <el-dialog :title="basicOutputDeals.editStatue=='edit'?'编辑输出':'添加输出'" :visible.sync="basicOutputDeals.showDialog"
             align="center">
    <el-form ref="editItem" :model="editItem" :rules="rules2" label-width="7em"
             style="height:350px;overflow-y: auto;padding-right:10px;">
      <el-form-item label="key" prop="key">
        <el-input v-model="editItem.key" :disabled="editItem.model" maxlength="15"></el-input>
      </el-form-item>
      <el-form-item label="输出类别" align="left">
        <el-select v-model="editItem.cat" style="width: 100%">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否可以预览数据" label-width="9em" align="left">
        <el-switch v-model="editItem.canPreview" active-text="" inactive-text=""></el-switch>
      </el-form-item>
      <el-form-item label="元数据设置" label-width="9em" align="left">
        <el-row :gutter="2">
          <el-col :span="22">
            <el-radio-group v-model="editItem.access">
              <el-radio label="SAME">与输入一致</el-radio>
              <el-radio label="APPEND">在输出集追加</el-radio>
              <el-radio label="UNKNOWN">未知</el-radio>
            </el-radio-group>
          </el-col>
          <template v-if="editItem.access=='APPEND'">
            <el-col :span="10">
              字段名
            </el-col>
            <el-col :span="10">
              类型
            </el-col>
            <el-col :span="2">
              <i class="fa fa-plus-square-o" title="添加字段" @click="addFields.push({name:'',type:'text'})"></i>
            </el-col>
            <template v-for="(item,index) in addFields">
              <el-col :span="10">
                <el-input v-model="item.name" placeholder="请输入字段名称" size="small"></el-input>
              </el-col>
              <el-col :span="10">
                <el-select v-model="item.type" size="small">
                  <el-option
                    v-for="item in dataType"
                    :label="item.label"
                    :key="item.value"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-col>
              <el-col :span="2">
                <i class="fa fa-trash-o" @click="addFields.splice(index,1)"></i>
              </el-col>
            </template>
          </template>
        </el-row>
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input type="textarea" placeholder="输出数据的描述" v-model.trim="editItem.description"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm('editItem')">确 认</el-button>
      <el-button @click="basicOutputDeals.showDialog = false">取 消</el-button>
    </span>
  </el-dialog>
</template>
<style>
</style>
<script>
  export default {
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
            {required: true, validator: validateKey, trigger: 'blur'},
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
        ],
        dataType: [
          {label: "字符", value: "text"},
          {label: "数值", value: "numeric"},
          {label: "时间", value: "timestamp"},
          {label: "日期", value: "date"},
        ],
        addFields: []
      }
    },
    computed: {
      basicOutputDeals: function () {//弹出框状态
        return this.$store.state.systemComponent.componentsDeals.basicOutputDeals;
      },
      editItem: function () {//当前编辑项
        return this.basicOutputDeals.editItem;
      },
      editComponent: function () {
        return this.$store.state.systemComponent.componentsDeals.editComponent;
      },
      showDialog() {
        return this.basicOutputDeals.showDialog;
      }
    },
    watch: {
      showDialog(val) {
        try {
          if (val && this.basicOutputDeals.editStatue == 'edit') {
            this.addFields = JSON.parse(this.editItem.columns);
          } else {
            this.addFields = [];
          }
        } catch (e) {
          console.error(e.message)
        }
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
          if (this.basicOutputDeals.editStatue == "add") {//新增时
            var newItem = new Object();
            newItem.key = this.editItem.key;
            newItem.cat = this.editItem.cat;
            newItem.access = this.editItem.access;
            newItem.description = this.editItem.description;
            newItem.canPreview = this.editItem.canPreview;
            newItem.columns = JSON.stringify(this.addFields);
            this.editComponent.outputs.push(newItem);//添加
          } else if (this.basicOutputDeals.editStatue == "edit") {//修改时
            var outputItem = this.editComponent.outputs.filter(item => item.id == this.editItem.id)[0];
            outputItem.key = this.editItem.key;
            outputItem.cat = this.editItem.cat;
            outputItem.access = this.editItem.access;
            outputItem.description = this.editItem.description;
            outputItem.canPreview = this.editItem.canPreview;
            outputItem.columns = JSON.stringify(this.addFields);
          }
          this.$refs.editItem.resetFields();
        } catch (e) {
          console.error(e.message)
        }
        this.basicOutputDeals.showDialog = false;
      },
      modelChange: function (val) {
        if (val) {
          this.editItem.key = "model";
        }
      }
    }
  }
</script>

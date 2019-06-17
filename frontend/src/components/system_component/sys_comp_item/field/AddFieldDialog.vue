<template>
  <el-dialog :title="basicFieldParamDeals.editStatue == 'add'?'添加参数':'编辑参数'"
             :visible.sync="basicFieldParamDeals.showEditDialog" align="center">
    <div style="height:370px; overflow-y:auto;">
      <el-form :model="editItem" label-width="8em" :rules="rules" ref="editItem">
        <el-form-item label="参数名称" prop="name">
          <el-input v-model="editItem.name" maxlength="30"></el-input>
        </el-form-item>
        <el-form-item label="表单控件" align="left">
          <el-select v-model="editItem.elementType" clearable filterable placeholder="请选择，可根据名称快速搜索"
                     style="width:100%">
            <el-option v-for="item in comTypes" :label="item.label" :key="item.value" :value="item.value"
                       :title="item.description"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="显示名称" prop="label">
          <el-input v-model.trim="editItem.label" maxlength="30"></el-input>
        </el-form-item>
        <el-form-item label="默认值">
          <el-input v-model="editItem.value" maxlength="50"></el-input>
        </el-form-item>
        <el-form-item label="显示组件" align="left">
          <el-switch style="margin-top:6px;" active-text="" inactive-text v-model="editItem.visible"></el-switch>
        </el-form-item>
        <el-form-item label="校验输入值" align="left">
          <el-switch style="margin-top:6px;" active-text="" inactive-text @change="editItem.required?'':editItem.rexp=''" v-model="editItem.required"></el-switch>
        </el-form-item>
        <el-form-item label="校验表达式" v-show="editItem.required">
          <el-autocomplete
            style="width:100%"
            v-model="editItem.rexp"
            :fetch-suggestions="querySearch"
            placeholder="输入正则表达式"
            @select="handleSelect"
          ></el-autocomplete>
        </el-form-item>
        <!--<el-form-item label="默认值">-->
        <!--<el-input v-model="editItem.defaultValue"></el-input>-->
        <!--</el-form-item>-->
        <el-form-item label="文本框提示文字">
          <el-input v-model="editItem.placeholder" maxlength="200"></el-input>
        </el-form-item>
        <el-form-item label="选项">
          <el-input :maxlength="300" v-model="editItem.options" placeholder="数据格式：选项一:1;选项二:2"></el-input>
        </el-form-item>
        <el-form-item label="提示信息">
          <el-input v-model="editItem.toolTip" maxlength="200" placeholder="换行用英文分号【;】分隔"></el-input>
        </el-form-item>
        <el-form-item label="顺序" align="left">
          <el-input-number v-model="editItem.sequence" :min="1" :max="99"></el-input-number>
        </el-form-item>
        <hr/>
        <el-form-item align="left" label="额外拓展参数">
          <i class="el-icon-plus" title="添加" @click="extra.push({'key':'','value':''})"></i>
        </el-form-item>
        <template v-for="(item,index) in extra">
          <el-form-item align="left">
            键：
            <el-input v-model="item.key" style="width:20%" maxlength="20"></el-input>
            &nbsp;&nbsp;
            值：
            <el-input v-model="item.value" style="width:50%" maxlength="50"></el-input>
            <i class="el-icon-delete" @click="extra.splice(index,1)"></i>
          </el-form-item>
        </template>
      </el-form>
    </div>
    <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleSubmitTest">确 定</el-button>
        <el-button @click="basicFieldParamDeals.showEditDialog = false">取 消</el-button>
    </span>
  </el-dialog>
</template>
<style>
</style>
<script>
  export default {
    name: 'AddFieldDialog',
    data: function () {
      var validateName = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入参数名称'));
        } else {
          if (this.editItem.name !== '') {
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
        regexps: [],
        comTypes: [],
        rules: {//校验规则
          name: [
            {validator: validateName, trigger: 'blur'},
            {min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur'}
          ],
          label: [
            {required: true, message: '请输入显示名称', trigger: 'blur'},
            {min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur'}
          ]
        },
        extra: [],//额外参数
        addEditItem: {
          id: 1,
          createTime: "",
          name: "",  //参数名称
          elementType: 0,   //参数类型
          label: "",  //label值
          value: "",  //值
          rexp: "", //正则表达式
          defaultValue: "", //默认值
          placeholder: "",//提示内容
          toolTip: "",//悬浮提示
          options: "",//下拉备填项
          required: false,  //是否必填
          sequence: 1,  //顺序
          visible: true, //是否可用
          extra: {}//额外参数
        }
      }
    },
    created: function () {
      var that = this;
      this.$.ajax({
        url: that.$store.state.global.httpServer + "/api/widget/list",
        method: "get",
        type: "json",
        beforeSend: function(xhr) {
          xhr.setRequestHeader("accessToken",localStorage.accessToken);
        },
        success: function (response) {
          var data = response.data;
          that.comTypes = [];
          data.forEach(function (item) {
            var child = {};
            child["value"] = parseInt(item.codeName);
            child["label"] = item.name;
            child["description"] = item.description;
            that.comTypes.push(child);
          });
        },
        error: function (data) {
          that.$notify({
            title: "提示",
            message: "获取组件类型失败！",
            type: "warning"
          });
        }
      });
    },
    computed: {
      basicFieldParamDeals: function () {//数据交互集
        return this.$store.state.systemComponent.componentsDeals.basicFieldParamDeals;
      },
      editItem: function () {//当前编辑项
        return this.basicFieldParamDeals.editItem;
      },
      TabItem: function () {//目标项
        return this.basicFieldParamDeals.TabItem;
      },
      showEditDialog: function () {
        return this.basicFieldParamDeals.showEditDialog;
      },
      tabs: function () {
        return this.$store.state.systemComponent.componentsDeals.editComponent.tabs;
      }
    },
    watch: {
      editItem: function () {//this.editItem 改变时更新
        this.dealExtraList();
      },
      showEditDialog: function (bool) {
        if (bool && this.basicFieldParamDeals.editStatue == "add") {
          this.basicFieldParamDeals.editItem = this.deepCopy(this.addEditItem);
        }
      }
    },
    mounted() {
      this.regexps = this.loadAll();
    },
    methods: {
      handleSubmitTest: function (ev) {//表单校验 ==> 测试
        this.$refs.editItem.validate((valid) => {
          if (valid && this.checkRegexp()) {
            this.enSure();
          } else {
            return false;
          }
        });
      },
      checkRegexp() {
        if(this.editItem.required) {
          try{
            let b = eval(this.editItem.rexp);
            b.test("123");
            return true;
          } catch(e) {
            this.$notify({
              type:"warning",
              message: "检验表达式不合法！"
            });
            return false;
          }
        } else {
          return true;
        }
      },
      checkMultipleName: function () {
        var result = false;
        var that = this;
        this.tabs.forEach(function (tab) {
          tab.elements.forEach(function (ele) {
            if (that.editItem.name == ele.name) {
              result = true;
            }
          });
        });
        return result;
      },
      enSure: function () {
        try {
          if (this.basicFieldParamDeals.editStatue == "add") {//添加
            if (this.checkMultipleName()) {
              this.$notify({
                type: 'warning',
                message: '算法的参数名称（英文名）不能重复！'
              });
              return;
            } else {
              this.editItem.id = "param" + this.basicFieldParamDeals.counter++;
              this.editItem.createTime = "";
              this.editItem.extra = {};
              var that = this;
              this.extra.forEach(function (item) {
                that.editItem.extra[item.key] = item.value;
              });
              this.TabItem.elements.push(this.deepCopy(this.editItem));
            }
          } else if (this.basicFieldParamDeals.editStatue == "edit") {//修改
            //修改项
            var element = this.TabItem.elements.filter(node => node.id == this.editItem.id)[0];
            this.editItem.extra = {};
            var that = this;
            this.extra.forEach(function (item) {
              that.editItem.extra[item.key] = item.value;
            });
            //浅复制-修改
            for (var key in element) {
              element[key] = this.editItem[key];
            }
          }
          this.$refs.editItem.resetFields();
        } catch (e) {
          console.error(e.message)
        }
        //隐藏弹出框
        this.basicFieldParamDeals.showEditDialog = false;
      },
      deepCopy: function (source) {
        var result;
        (source instanceof Array) ? (result = []) : (typeof(source) === "object") ? (source === null ? (result = "") : (result = {})) : (result = source);
        for (var key in source) {
          result[key] = (typeof source[key] === 'object') ? this.deepCopy(source[key]) : source[key];
        }
        return result;
      },
      dealExtraList: function () {//处理额外参数集合
        var extras = [];
        var item = this.editItem.extra;
        for (var key in item) {
          var temp = {};
          temp["key"] = key;
          temp["value"] = item[key];
          extras.push(temp);
        }
        this.extra = extras;
      },
      querySearch(queryString, cb) {
        var regexps = this.regexps;
        var results = queryString ? regexps.filter(this.createFilter(queryString)) : regexps;
        // 调用 callback 返回建议列表的数据
        cb(results);
      },
      createFilter(queryString) {
        return (regexp) => {
          return (regexp.value.indexOf(queryString.toLowerCase()) === 0);
        };
      },
      handleSelect(item) {
        this.editItem.rexp = item.regexp;
      },
      loadAll() {
        return [
          {"value": "1-30长度非空字符串【/^\\S{1,30}$/】", "regexp": "/^\\S{1,30}$/"},
          {"value": "数字、字母、下划线组成的1-20长度的字符串【/^\\w{1,20}$/】", "regexp": "/^\\w{1,20}$/"},
          {"value": "1-3长度的整数【/^-?\\d{0,2}$/】", "regexp": "/^-?\\d{1,3}$/"},
          {"value": "非负数整数1-3长度，小数点后1-2位小数（0、正整数、正浮点数）【/^\\d{1,3}(\\.\\d{1,2})?$/】", "regexp": "/^\\d{1,3}(\\.\\d{1,2})?$/"},
          {"value": "1-10长度任意字符串【/^.{1,10}$/】", "regexp": "/^.{1,10}$/"},
          {"value": "正数、负数、小数【/^(\\-|\\+)?\\d+(\\.\\d+)?$/】", "regexp": "/^(\\-|\\+)?\\d+(\\.\\d+)?$/"}
        ];
      }
    }
  }
</script>

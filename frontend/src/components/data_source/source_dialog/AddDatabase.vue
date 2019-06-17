<template>
  <!--从数据库上传-->
  <el-dialog title="新建数据源" :visible.sync="statue.dbUpload" width="80%">
    <div>
      <el-steps :active="active" simple>
        <el-step title="连接属性" icon="el-icon-edit"></el-step>
        <el-step title="预览数据" icon="el-icon-picture"></el-step>
        <el-step title="字段设置" icon="el-icon-setting"></el-step>
      </el-steps>
    </div>
    <div v-show="active==0" style="height:250px; overflow:auto; padding: 10px 15px;" id="first">
      <el-form :model="dbMsg" :rules="rules" ref="dbMsg" class="demo-ruleForm" label-width="130px">
        <el-form-item label="新建目标表" prop="tableName">
          <el-input v-model="dbMsg.tableName" placeholder="请定义同步到的目标表名"></el-input>
        </el-form-item>
        <el-form-item label="数据库类型" align="left">
          <el-select v-model="dbMsg.dataType" @change="handleSelect" placeholder="请选择类型"
                     :disabled="testResult">
            <el-option
              v-for="item in databaseType"
              :label="item.label"
              :key="item.value"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="URL链接" prop="url">
          <el-input v-model="dbMsg.url" :disabled="testResult"
                    placeholder="请定义数据表链接oracle/mysql"></el-input>
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="dbMsg.username" :disabled="testResult" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="dbMsg.password" :disabled="testResult" type="password"
                    placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="SQL" prop="sql">
          <el-input type="textarea" v-model="dbMsg.sql" :autosize="{ minRows: 2, maxRows: 4}"
                    :disabled="testResult" placeholder="请输入sql查询语句"></el-input>
        </el-form-item>
        <el-form-item label="存储有效期（天）" align="left">
          <el-col :span="9">
            <el-input-number v-model="dbMsg.duration" :min="1" :max="180"></el-input-number>
          </el-col>
          <el-col :span="6" class="fl">
            <label style="float:right;" class="el-form-item__label">预览设置</label>
          </el-col>
          <el-col :span="9" class="fl">
            <el-select v-model="dbMsg.previewMode" placeholder="UTF-8">
              <el-option
                v-for="item in filterPreviewType"
                :label="item.label"
                :key="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
      </el-form>
    </div>
    <div v-show="active==2" style="height:250px; overflow:auto; padding: 10px 15px;" id="second">
      <div style="color:red">注意：【字段名】只能是以字母开头, 由小写英文字母、数字、下划线组成</div>
      <el-row :gutter="20" style="text-align:center">
        <el-col :span="4">原字段</el-col>
        <el-col :span="4">字段名</el-col>
        <el-col :span="4">类型</el-col>
        <el-col :span="4">长度</el-col>
        <el-col :span="4">精度</el-col>
        <el-col :span="4">备注</el-col>
      </el-row>
      <template v-for="(item,index) in dataColumnName">
        <el-row :gutter="20" style="text-align:center;padding:2px;">
          <el-col :span="4">
            <el-input size="small" :disabled="true" :value="item.oldName"></el-input>
          </el-col>
          <el-col :span="4">
            <el-input size="small" v-model.trim="item.name"></el-input>
          </el-col>
          <el-col :span="4">
            <el-select v-model="item.dataType" size="small" placeholder="请选择类型">
              <el-option
                v-for="item in options"
                :label="item.label"
                :key="item.value"
                :value="item.value">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-input-number size="small" v-model="item.length" :min="1"
                             :max="255"></el-input-number>
          </el-col>
          <el-col :span="4">
            <el-input-number size="small" v-model="item.scale" :min="0"
                             :max="10"></el-input-number>
          </el-col>
          <el-col :span="4">
            <el-input size="small" v-model="item.comment"></el-input>
          </el-col>
        </el-row>
      </template>
    </div>
    <div v-show="active==1" style="height:250px; overflow:auto; padding: 10px 0px;" id="third">
      <el-table :row-class-name="'row_style'" :header-row-class-name="'row_header_style'" :data="dataList" height="250" border style="width: 100%" :show-header="true">
        <el-table-column v-for="item in dataListTitle" :key="item.name" :prop="item.name" :label="item.name"
                         width="150"></el-table-column>
      </el-table>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleReset" v-show="active==0">重置</el-button>
      <el-button type="info" @click="handleSubmitTest" v-show="active==0">测试连接</el-button>
      <el-button @click="previous" v-show="active!=0">上一步</el-button>
      <el-button type="primary" @click="next" v-show="active!=2">下一步</el-button>
      <el-button type="primary" @click="handleSubmitSure" v-show="active==2">确定</el-button>
    </div>
  </el-dialog>
</template>
<style scoped>
  .titleStyle {
    border: 1px solid #e0e6ed;
    height: 35px;
    line-height: 35px;
    background-color: #58B7FF;
  }
</style>
<script>
  export default {
    name: 'AddDatabase',
    data: function () {
      //自定义输入名称输入校验
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.ruleForm2.checkPass !== '') {
            this.$refs.ruleForm2.validateField('checkPass');
          }
          callback();
        }
      };
      var validateTableName = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入名称'));
        } else {
          if (this.dbMsg.tableName !== '') {
            //var reg = /^\w+$/;//数字、26个英文字母或者下划线组成的字符串
            var that = this;
            var reg = new RegExp(that.regexp.tableName);//英文字母开头，字母、数字、下划线组成，长度1-32
            if (!reg.test(value)) {
              callback(new Error(that.regexp.tableNameCheckMsg));
            }
            callback();
          }
          callback();
        }
      };
      return {
        databaseType: [
          {label: 'MySQL', value: 'jdbc:mysql://<machine_name>:<port>/<dbname>'},
//                    {model: 'common', label: '达梦6', value: 'jdbc:dm://<hostname>:<port>/database'},
//                    {model: 'common', label: '达梦7', value: 'jdbc:dm://<hostname>:<port>/database'},
//                    {model: 'common', label: 'GBase8a', value: 'jdbc:gbase://<hostname>:<port>/database'},
//                    {model: 'common', label: 'GBase8s', value: 'jdbc:gbase://<hostname>:<port>/database'},
//                    {model: 'common', label: '金仓', value: 'jdbc:kingbase://<hostname>:<port>/database'},
          {label: 'Oracle', value: 'jdbc:oracle:thin:@<machine_name>:<port>:<dbname>'},
          // {model:'common',label:,'SQL Server',value:'jdbc:sqlserver://<machine_name>:<port>;DatabaseName=<dbname>'},
          {
            label: 'SQL Server(jtds)',
            value: 'jdbc:jtds:sqlserver://<machine_name>:<port>/<dbname>;useLOBs=false'
          },
          {label: 'DB2', value: 'jdbc:db2://<machine_name>:<port>/<dbname>'},
          {label: 'PostgreSQL', value: 'jdbc:postgresql://<hostname>:<port>/<dbname>'},
//          {label: 'HIVE', value: 'jdbc:hive2://<machine_name>:<port>'},
        ],
        previewType: [
          {label: "预览前100条", value: 'ONLY100'},
          {label: "分页显示", value: 'PAGEABLE'}
        ],
        testResult: false,
        tabItem: "first",
        options: [
//                    {value: 'character', label: 'character'},
//                    {value: 'bigint', label: 'bigint'},
//                    {value: 'bool', label: 'bool'},
          {value: 'numeric', label: '数值'},
          {value: 'text', label: '字符'},
          {value: 'date', label: '日期'},
          {value: 'timestamp', label: '时间'}
        ],
        rules: {//校验规则
          tableName: [
            {required: true, validator: validateTableName, trigger: 'blur'},
            //{ min: 1, max: 99, message: '长度在 1 到 99 个字符', trigger: 'blur' }
          ],
          url: [
            {required: true, message: '请填写数据库连接url', trigger: 'blur'}
          ],
          username: [
            {required: true, message: '请填写数据库连接用户名', trigger: 'blur'}
          ],
          password: [
            {required: true, message: '请填写数据库连接密码', trigger: 'blur'}
          ],
          sql: [
            {required: true, message: '请填写查询sql', trigger: 'blur'}
          ]
        },
        active: 0,//当前激活步骤
        defaultDbMsg: {
          dataType: "jdbc:mysql://<machine_name>:<port>/<dbname>",
          url: "jdbc:mysql://<machine_name>:<port>/<dbname>",//数据库连接
          tableName: "", //表名
          username: "", //用户名
          password: "", //密码
          sql: "", //查询sql
          duration: 60,//数据源存储有效期
          previewMode: 'ONLY100',//预览设置ONLY100/PAGEABLE
        }
      }
    },
    computed: {
      filterPreviewType() {
        var type = this.databaseType.filter(node=> node.value == this.dbMsg.dataType)[0];
        if(type.label == 'HIVE') {
          return this.previewType.filter(node => node.value == 'ONLY100');
        } else {
          return this.previewType;
        }
      },
      databaseUpload: function () {
        return this.$store.state.datasource.databaseUpload;
      },
      dbMsg: function () { //数据表链接信息
        return this.databaseUpload.dbMsg;
      },
      dataColumnName: function () {//数据列头
        return this.databaseUpload.dataColumnName;
      },
      dataListTitle: function () {//数据列头
        return this.databaseUpload.dataListTitle;
      },
      dataList: function () {//预览数据
        var oldData = this.databaseUpload.dataList;
        var result = [];
        var tempTitles = [];
        try {
          if (oldData.length > 0) {
            tempTitles = Object.keys(oldData[0]);
          }
          oldData.forEach(function (item) {
            var tempItem = {};
            tempTitles.forEach(function (key) {
              if (key.indexOf(".") == -1) {
                tempItem[key] = item[key];
              } else {
                var temp = key.split(".");
                tempItem[temp[1]] = item[key];
              }
            });
            result.push(tempItem);
          });
        } catch (e) {
          console.error(e.message)
        }
        return result;
      },
      statue: function () {//dialog弹出状态
        return this.$store.state.datasource.deals.statue;
      },
      regexp: function () {
        return this.$store.state.global.regexp;
      }
    },
    methods: {
      //上一步
      previous() {
        this.active--;
      },
      //下一步
      next() {
        switch (this.active) {
          case 0:
            this.$refs.dbMsg.validate((valid) => {
              if (valid) {
                this.connectionTest("next");
              } else {
                return false;
              }
            });
            break;
          case 1:
            this.active++;
            break;
        }
      },
      showDbDialog: function () {//显示弹出框
        this.statue.dbUpload = true;
      },
      handleReset: function () {//重置表单
        this.active = 0;
        this.$("#firstTab").animate({scrollTop: 0}, 200);
        this.databaseUpload.dataColumnName = [];//置空参数设置
        this.databaseUpload.dataListTitle = [];//置空预览数据
        this.databaseUpload.dataList = [];//置空预览数据
        this.testResult = false;//重置为可编辑状态
        //恢复默认状态
        this.databaseUpload.dbMsg = this.deepCopy(this.defaultDbMsg);
        this.$refs.dbMsg.resetFields();
      },
      handleSubmitTest: function (ev) {//表单校验 ==> 测试
        this.connectionTest("test");
      },
      handleSubmitSure: function (ev) {//表单校验 ==> 确认添加
        this.$refs.dbMsg.validate((valid) => {
          if (valid) {
            if (this.checkFileList()) {
              this.checkIfExistTable();
            }
          } else {
            this.tabItem = "first";
            $jq("#firstTab").animate({scrollTop: 0}, 200);
            return false;
          }
        });
      },
      connectionTest: function (flag) {//连接测试
        var that = this;
        var conn = this.databaseUpload.dbMsg;
        var data = {};
        data["url"] = conn.url;
        data["userName"] = conn.username;
        data["password"] = conn.password;
        data["sql"] = conn.sql;
        this.$.ajax({
          url: that.$store.state.global.httpServer + "/api/datasource/connection/test",
          method: "post",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          data: JSON.stringify(data),
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          success: function (data) {
            if (data.status == "SUCCESS") {
              if (flag == "test") {
                that.$notify({
                  title: '成功',
                  message: '连接成功。',
                  type: 'success'
                });
              } else if (flag == "next") {
                that.active++;
              }
              var previewData = data.data.data;//列对象
              var columns = data.data.columns;
              var dataColumnName = [];
              columns.forEach(function (col) {
                var columnItem = {};
                var name = col.name;
                var dataType = col.dataType;
                columnItem["oldName"] = name;
                columnItem["name"] = name.indexOf(".") == -1 ? name : name.split(".")[1];
                columnItem["comment"] = "";
                columnItem["dataType"] = dataType;
                columnItem["format"] = "";
                columnItem["length"] = 255;
                columnItem["scale"] = 0;
                dataColumnName.push(columnItem);
              });
              var newColumns = that.deepCopy(dataColumnName);
              newColumns.forEach(function (column) {
                column.name = column.name.toLowerCase().trim();
              });
              that.databaseUpload.dataColumnName = newColumns;
              that.databaseUpload.dataListTitle = that.deepCopy(dataColumnName);
              that.databaseUpload.dataList = previewData;
              //测试连接成功后不可修改链接信息
              that.testResult = true;

            } else {
              that.testResult = false;
              that.$notify({
                title: '失败',
                message: data.message,
                type: 'warning'
              });
            }
          },
          error: function (response) {
            that.$store.commit("dealRequestError", response);
          }
        });
      },
      checkIfExistTable: function () {//检查表是否已存在
        var that = this;
        var tableName = this.databaseUpload.dbMsg.tableName;
        this.$.ajax({//检查表是否存在
          url: that.$store.state.global.httpServer + "/api/datasource/" + tableName + "/exists",
          method: "get",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          success: function (data) {
            if (data.status == "SUCCESS" && data.data == true) { //已存在
              that.$notify({
                title: '警告',
                message: '数据表名已存在，修改后重试！',
                type: 'warning'
              });
            } else if (data.status == "SUCCESS" && data.data == false) {//不存在
              //建表： 添加conn ==> connId ==> 添加表
              // that.addConnection();
              that.addDatabaseTable();
            } else if (data.status == "FAIL") {
              that.$notify({
                title: '警告',
                message: data.message,
                type: 'warning'
              });
            }
          },
          error: function (response) {
            that.$store.commit("dealRequestError", response);
          }
        });
      },
      addDatabaseTable: function () { //创建数据表
        var that = this;
        var tableName = this.databaseUpload.dbMsg.tableName;
        var dataColumnNames = this.databaseUpload.dataColumnName;
        var data = {};
        data["tableName"] = tableName;
        // data["connId"] = connId;
        data["columns"] = [];
        data["duration"] = this.dbMsg.duration;
        data["previewMode"] = this.dbMsg.previewMode;
        data["connection"] = {};
        //timestamp, character, bool, numeric, bigint, text
        dataColumnNames.forEach(function (item) {
          data["columns"].push({
            "comment": item.comment,
            "dataType": item.dataType,
            "length": item.length,
            "name": item.name,
            "scale": item.scale,
            "format": item.format
          });
        });
        var conn = this.databaseUpload.dbMsg;
        data["connection"]["url"] = conn.url;
        data["connection"]["userName"] = conn.username;
        data["connection"]["password"] = conn.password;
        data["connection"]["sql"] = conn.sql;

        this.$.ajax({
          url: that.$store.state.global.httpServer + "/api/datasource/rdbms",
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
              //完成创建，取消弹出框，刷新列表
              that.statue.dbUpload = false; //隐藏弹出框
              that.$store.dispatch("getMenuDatabaseList");
              that.handleReset();
            } else if (data.status == "FAIL") {
              that.$notify.error({
                title: '失败',
                message: data.message
              });
            }
          },
          error: function (response) {
            that.$store.commit("dealRequestError", response);
          }
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
      checkFileList() {//检查是否满足建表条件
        if (!this.testResult) {
          this.$notify({
            message: "测试通过才能添加数据源！",
            type: 'warning'
          });
          return false;
        }
        var filedCheck = this.testFieldRules();
        if (!filedCheck.result) {
          this.$notify({
            message: filedCheck.message,
            type: 'warning'
          });
          return false;
        }
        return true;
      },
      testFieldRules: function () {//校验字段目标列是否合法
        var that = this;
        var reg = new RegExp(that.regexp.fieldName);//小写英文字母开头，字母、数字、下划线组成
        var dataColumnNames = this.databaseUpload.dataColumnName;
        var result = true;//通过
        var message = "字段设置目标列";
        dataColumnNames.forEach(function (item) {
          if (!reg.test(item.name)) {
            result = false;
            message += "【" + item.name + "】";
          }
        });
        message += "不合法";
        return {
          result: result,
          message: message
        }
      },
      handleSelect: function (val) {
        this.dbMsg.url = val;
      }
    }
  }
</script>

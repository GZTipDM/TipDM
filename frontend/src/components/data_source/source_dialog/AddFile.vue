<!--从文件上传-->
<template>
  <el-dialog :title="deals.created?'重新上传文件':'新建数据源'" :visible.sync="statue.fileUpload" width="80%">
    <div>
      <el-steps :active="active" simple>
        <el-step title="文件属性" icon="el-icon-edit"></el-step>
        <el-step title="预览数据" icon="el-icon-picture"></el-step>
        <el-step title="字段设置" icon="el-icon-setting"></el-step>
      </el-steps>
    </div>
    <div v-show="active==0" style="height:250px; overflow:auto; padding: 10px 15px;" id="first">
      <el-form :model="deals" :rules="rules" ref="deals" class="demo-ruleForm" label-width="130px">
        <el-form-item label="上传文件">
          <Uploader v-on:resetForm="handleReset"></Uploader>
        </el-form-item>
        <el-form-item label="新建目标表名" prop="tableName" v-if="!deals.created">
          <el-input v-model="deals.tableName" placeholder="请定义同步到的目标表名"></el-input>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="列分隔符" align="left">
              <el-select v-model="deals.delimiter">
                <el-option label="逗号（,）" value="comma"></el-option>
                <el-option label="分号（;）" value="semicolon"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="文件编码" align="left">
              <el-select v-model="deals.encoding" placeholder="UTF-8">
                <el-option label="UTF-8" value="UTF-8"></el-option>
                <el-option label="GBK" value="GBK"></el-option>
                <el-option label="GB2312" value="GB2312"></el-option>
                <el-option label="ASCII" value="ASCII"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="存储有效期（天）" align="left">
              <el-input-number v-model="deals.duration" :min="1" :max="180"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预览设置" align="left">
              <el-select v-model="deals.previewMode" placeholder="UTF-8">
                <el-option label="预览前100条" value="ONLY100"></el-option>
                <el-option label="分页显示" value="PAGEABLE"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="是否开启首行数据类型识别" label-width="200px;" align="left">
          <el-select v-model="deals.dataType">
            <el-option label="开启" value="true"></el-option>
            <el-option label="关闭" value="false"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </div>
    <div v-show="active==1" style="height:270px; overflow:auto;" id="second">
      <el-table :header-cell-style="{padding:'10px'}" :data="dataList" border style="width: 100%" height="270">
        <el-table-column :show-overflow-tooltip="true" v-for="(item,index) in dataListTitle" :prop="item.key" :key="item.key" :label="item.name"
                         width="150">
        </el-table-column>
      </el-table>
    </div>
    <div v-show="active==2" style="height:250px; overflow:auto; padding: 10px 15px;" id="third">
      <div style="color:red; text-align: left">注意：【字段名】只能是以字母开头, 由小写英文字母、数字、下划线组成</div>
      <el-row :gutter="20" style="text-align:center">
        <el-col :span="4">原字段</el-col>
        <el-col :span="4">字段名</el-col>
        <el-col :span="4">类型</el-col>
        <el-col :span="4">长度</el-col>
        <el-col :span="4">精度</el-col>
        <el-col :span="4">备注</el-col>
      </el-row>
      <template v-for="(item,index) in dataColumnName">
        <el-row :gutter="20" style="text-align:center; padding:2px;">
          <el-col :span="4">
            <el-input size="small" :disabled="true" :value="item.oldName"></el-input>
          </el-col>
          <el-col :span="4">
            <el-input size="small" v-model.trim="item.name" :maxlength="30"></el-input>
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
            <el-input size="small" v-model="item.comment" :maxlength="30"></el-input>
          </el-col>
        </el-row>
      </template>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleReset" v-show="active==0">重置</el-button>
      <el-button @click="previous" v-show="active!=0">上一步</el-button>
      <el-button type="primary" @click="handlePreview" :disabled="!fileUpload.uploadFile"
                 v-show="active==0">下一步
      </el-button>
      <el-button type="primary" @click="next" v-show="active==1">下一步</el-button>
      <el-button type="primary" @click="createUserTable" :disabled="btnSure"
                 v-if="!deals.created" v-show="active==2">确定
      </el-button>
      <el-button type="primary" @click="reUpload" :disabled="btnSure" v-if="deals.created" v-show="active==2">上传
      </el-button>
    </div>
  </el-dialog>
</template>
<script>
  import Uploader from './add_file/Uploader.vue'

  export default {
    name: 'AddFile',
    data: function () {
      //自定义输入名称输入校验
      var validateTableName = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入名称'));
        } else {
          if (this.deals.tableName !== '') {
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
        rules: {
          tableName: [
            {required: true, validator: validateTableName, trigger: 'blur'},
          ]
        },
        options: [
          {value: 'numeric', label: '数值'},
          {value: 'text', label: '字符'},
          {value: 'date', label: '日期'},
          {value: 'timestamp', label: '时间'}
        ],
        btnSure: false,
        active: 0,
        tabItem: "first"
      }
    },
    computed: {
      fileUpload: function () {
        return this.$store.state.datasource.fileUpload;
      },
      dataColumnName: function () {//数据列头集合
        return this.fileUpload.dataColumnName;
      },
      dataListTitle: function () {//数据列头集合
        return this.fileUpload.dataListTitle;
      },
      dataList: function () {//数据集合
        return this.fileUpload.dataList;
      },
      deals: function () {//条件集合
        return this.fileUpload.deals;
      },
      statue: function () {
        return this.$store.state.datasource.deals.statue;
      },
      fileUploadDialog() {
        return this.statue.fileUpload;
      },
      regexp: function () {
        return this.$store.state.global.regexp;
      }
    },
    watch: {
      fileUploadDialog(val) {
        if (this.btnSure) {//避免异常导致确定按钮一直不可用
          this.btnSure = false;
        }
      }
    },
    components: {
      Uploader
    },
    methods: {
      previous() {
        this.active--;
      },
      next() {
        this.active++;
      },
      reUpload() {//重新上传
        this.$("#ctlBtn").trigger("click");
      },
      parseUploadData: function () {//解析一部分文件--生成预览
        var that = this;
        if (window.FileReader) {//检测浏览器是否支持FileReader
          var reader = new FileReader();
          var file = this.fileUpload.uploadFile;
          if (!file) {//如果未选择文件
            this.$notify({
              title: '警告',
              message: '请选择数据文件！',
              type: 'warning'
            });
            return;
          }
          //指定开始位置和结束位置读取文件
          var blob = file.slice(0, 1024 * 10); //读取文件的一部分作为预览数据 10K
          reader.readAsText(blob, this.deals.encoding);
          that.fileUpload.deals.encoding = this.deals.encoding;
          var name = file.name;
          //文件成功读取完毕时触发
          reader.onload = function (e) {
            var data = e.target.result;
            var columnNames = []; //列名集合
            var columnData = []; //列数据
            var splitStr = "\r";
            var dataRows = data.split(splitStr);//所有行数据
            dataRows.splice(dataRows.length - 1, 1);//移除最后一行，避免出现不完整行数
            if (dataRows.length == 0) {
              that.$notify({
                message: "数据异常或换行异常！",
                type: 'warning'
              });
              return;
            }
            /****列头处理开始*****/
            var firstRow = that.dealRow(dataRows[0], that.switchDelimiter(that.deals.delimiter));
            var secondRow = that.dealRow(dataRows[1], that.switchDelimiter(that.deals.delimiter));
            dataRows.splice(0, 1);//移除第一行数据
            for (var i = 0; i < firstRow.length; i++) {
              var colTemp = {};
              colTemp["name"] = that.deals.ifFirstRowAsColumnName == "true" ? firstRow[i].replace(".", "_") : "field" + i;
              colTemp["key"] = 'index' + i;
              colTemp["oldName"] = colTemp["name"];
              colTemp["comment"] = "";
              colTemp["dataType"] = that.dealDataType(secondRow[i]);
              colTemp["format"] = "";
              colTemp["length"] = 255;
              colTemp["scale"] = 0;
              columnNames.push(colTemp);
            }
            var newColumns = that.deepCopy(columnNames);
            newColumns.forEach(function (column) {
              column.name = column.name.toLowerCase();
            });
            that.fileUpload.dataColumnName = that.deepCopy(newColumns);
            that.fileUpload.dataListTitle = that.deepCopy(columnNames);
            /****列头处理结束*****/
            for (var j = 0; j < dataRows.length; j++) {
              var rowTemp = {};
              var rowData = that.dealRow(dataRows[j], that.switchDelimiter(that.deals.delimiter));
              for (var k = 0; k < rowData.length; k++) {
                rowTemp[columnNames[k].key] = rowData[k];
              }
              columnData.push(rowTemp);
            }
            that.fileUpload.dataList = columnData;
            that.$notify({
              message: "数据结构解析成功!",
              type: 'success'
            });
          };
          this.active++;
        } else {
          alert("FileReader Not supported by your browser!");
        }
      },
      //处理数据类型
      dealDataType(value) {
        if (this.deals.dataType == "false") {
          return 'text';
        }
        var type = "text";
        try {
          if (null == value || "NULL" == value || "null" == value || "" == value) {
            type = "text"
          } else if (!isNaN(value)) {//数字类型-timestamp/numeric
            var test2 = new Date(value);
            if (test2 == 'Invalid Date') {
              type = 'numeric';
            } else {
              if (value.length > 10) {
                type = 'timestamp';
              } else {
                type = 'numeric';
              }
            }
          } else {//字符串类型-日期/字符串
            var test = new Date(value);
            if (test == 'Invalid Date') {
              type = "text";
            } else {
              type = 'date';
            }
          }
        } catch (e) {
          console.error(e.message);
        }
        return type;
      },
      dealRow(str, splitStr) {
        var result = [];
        var array = str.split(splitStr);
        if (array.length > 0) {
          let tempArray = [];
          let include = false;
          let jump = true;
          array.forEach((item) => {
            item = item.trim();
            if (item.startsWith("\"") && item.endsWith("\"")) {
              result.push(item.replace(/\"/g, ""));
              jump = false;
            } else if (item.startsWith("\"") && !item.endsWith("\"")) {
              if (!include) {
                include = true;
                tempArray = [];
              }
            } else if (!item.startsWith("\"") && item.endsWith("\"")) {
              if (include) {
                include = false;
                tempArray.push(item.replace(/\"/g, ""));
                result.push(tempArray.join(splitStr));
                jump = false;
              }
            }
            if (include) {
              tempArray.push(item.replace(/\"/g, ""));
            } else {
              if (jump) {
                result.push(item);
              } else {
                jump = !jump;
              }
            }
          })
        }
        return result;
      },
      handlePreview: function (ev) {//表单校验 ==> 生成预览
        this.$refs.deals.validate((valid) => {
          if (valid) {
            this.parseUploadData();
          } else {
            return false;
          }
        });
      },
      createUserTable: function () {
        this.$refs.deals.validate((valid) => {
          if (valid) {
            if (this.checkFileList()) {
              this.btnSure = true;
              this.checkExistTable();
            }
          } else {
            this.tabItem = "first";
            return false;
          }
        });
      },
      checkFileList() {//检查是否满足上传条件
        if (this.fileUpload.fileItems.length == 0) {
          this.$notify({
            title: '警告',
            message: '请选择要上传的数据文件！',
            type: 'warning'
          });
          return false;
        }
        var fileColumns = this.fileUpload.dataColumnName;
        if (fileColumns.length < 1) {
          this.$notify({
            title: '警告',
            message: "请点击查看数据结构，进行字段设置",
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
      checkExistTable() {//检查表是否存在
        var that = this;
        var tableName = this.fileUpload.deals.tableName;
        this.$.ajax({//检查表是否存在
          url: that.$store.state.global.httpServer + "/api/datasource/" + tableName + "/exists",
          method: "get",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          success: function (data) {
            switch (data.status) {
              case "SUCCESS":
                if (data.data == true) {
                  that.$notify({
                    title: '警告',
                    message: '数据表名已存在，修改后重试！',
                    type: 'warning'
                  });
                  that.btnSure = false;
                } else {
                  that.createTable();
                }
                break;
              case "FAIL":
                that.$notify({
                  title: '警告',
                  message: data.message,
                  type: 'warning'
                });
                that.btnSure = false;
                break;
            }
          },
          error: function (response) {
            that.$store.commit("dealRequestError", response);
            that.btnSure = false;
          }
        });
      },
      createTable() {//创建数据表
        var that = this;
        var tableName = this.fileUpload.deals.tableName;
        var data = {};
        data["tableName"] = tableName;
        //data["tableType"] = "FLAT_FILE";
        data["duration"] = this.deals.duration;
        data["previewMode"] = this.deals.previewMode;
        data["fileMD5"] = this.fileUpload.fileMD5;
        data["columns"] = [];
        var fileColumns = this.fileUpload.dataColumnName;
        fileColumns.forEach(function (item) {
          data["columns"].push({
            "comment": item.comment,
            "dataType": item.dataType,
            "length": item.length,
            "name": item.name.trim(),
            "formatter": item.format,
            "scale": item.scale
          });
        });
        this.$.ajax({
          url: that.$store.state.global.httpServer + "/api/datasource/flat",
          method: "post",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          data: JSON.stringify(data),
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          success: function (data) {
            if (data.status == "SUCCESS") {
              that.fileUpload.deals.uploadId = data.data;
              that.checkFileMD5();
            } else if (data.status == "FAIL") {
              that.$notify({
                title: '警告',
                message: data.message,
                type: 'warning'
              });
              that.btnSure = false;
              that.fileUpload.deals.uploadId = "";
            }
          },
          error: function (response) {
            that.$store.commit("dealRequestError", response);
            that.btnSure = false;
          }
        });
      },
      checkFileMD5: function () {  //校验文件是否已存在
        var that = this;
        this.$.ajax({
          url: that.$store.state.global.httpServer + "/api/file/existsMD5",
          method: "get",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          data: {
            tableName: that.fileUpload.deals.tableName,
            fileMD5: that.fileUpload.fileMD5,
            uploadId: that.fileUpload.deals.uploadId
          },
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          success: function (data) {
            switch (data.status) {
              case "SUCCESS":
                if (data.data) {//已存在
                  that.fileUpload.fileItems[0]["status"] = "已上传";
                  that.fileUpload.fileItems[0]["progress"] = "";
                  that.$notify({
                    title: "提示",
                    message: "添加成功",
                    type: "success"
                  });
                  that.statue.fileUpload = false;//隐藏窗口
                  that.tabItem = "first";
                  that.handleReset();
                  //刷新列表
                  that.$store.dispatch("getMenuDatabaseList");
                } else {//不存在
                  that.$("#ctlBtn").trigger("click");
                }
                break;
              case "FAIL":
                that.$notify.error({
                  title: "提示",
                  message: "MD5校验失败！",
                });
                break;

            }
            that.btnSure = false;
          },
          error: function (response) {
            that.$store.commit("dealRequestError", response);
            that.btnSure = false;
          }
        });
      },
      handleReset: function () {//重置表单
        this.fileUpload.dataColumnName = [];//置空参数设置
        this.fileUpload.dataListTitle = [];//置空预览数据
        this.fileUpload.dataList = [];//置空预览数据
        this.fileUpload.fileItems = [];//置空上传文件列表
        this.fileUpload.uploadFile = "";//置空文件
        this.fileUpload.fileMD5 = null; //置空fileMD5
        this.deals.tableName = "";//置空表名
        this.deals.delimiter = "comma";
        this.deals.rowSplit = "enter";
        this.deals.encoding = "UTF-8";
        this.deals.duration = 60;
        this.deals.previewMode = "ONLY100";
        this.$refs.deals.resetFields();
        this.active = 0;
      },
      deepCopy: function (source) {
        var result;
        (source instanceof Array) ? (result = []) : (typeof (source) === "object") ? (source === null ? (result = "") : (result = {})) : (result = source);
        for (var key in source) {
          result[key] = (typeof source[key] === 'object') ? this.deepCopy(source[key]) : source[key];
        }
        return result;
      },
      testFieldRules: function () {
        let that = this;
        let reg = new RegExp(that.regexp.fieldName);//小写英文字母开头，字母、数字、下划线组成
        let fileColumns = this.fileUpload.dataColumnName;
        let result = true;//通过
        let message = "字段设置字段名";
        fileColumns.forEach(function (item) {
          if (!reg.test(item.name)) {
            result = false;
            message += "【" + item.name + "】"
          }
        });
        message += "不合法";
        return {
          result: result,
          message: message
        }
      },
      switchDelimiter(str) {
        let result = str;
        switch (str) {
          case "comma":
            result = ",";
            break;
          case "semicolon":
            result = ";";
            break;
          case "verticalLine":
            result = "|";
            break;
          case "colon":
            result = ":";
            break;
          case "space":
            result = " ";
            break;
          case "tab":
            result = /\t/;
            break;
        }
        return result;
      }
    }
  }
</script>
<style>
  .pre_header {
    padding: 0px;
  }
</style>

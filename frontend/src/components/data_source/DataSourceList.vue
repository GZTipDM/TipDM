<template>
  <div class="h100p">
    <el-table class="tableH" :data="dbListItems" border :height="tableHeight">
      <el-table-column label="表名" prop="showName"></el-table-column>
      <el-table-column label="创建人" align="center" prop="creatorName"></el-table-column>
      <el-table-column label="数据来源" align="center" prop="tableType" :formatter="tableTypeFormat">
      </el-table-column>
      <el-table-column label="同步状态" align="center" prop="status" :formatter="statusFormat">
      </el-table-column>
      <el-table-column label="创建时间" align="center">
        <template slot-scope="scope">
          {{parseTimeToDate(scope.row.createTime)}}
        </template>
      </el-table-column>
      <el-table-column align="left" width="230" label="操作">
        <template slot-scope="scope">
          <div id="funBtn" class="funBtn">
            <template v-if="scope.row.tableType != 'UNSTRUCTURED'">
              <el-button title="预览" @click="handlePreview(scope.$index,scope.row)" type="text"><i
                class="fa fa-eye"></i></el-button>
            </template>
            <el-button title="删除" @click="handleDelete(scope.$index,scope.row)" type="text"><i
              class="fa fa-trash"></i></el-button>
            <el-button title="分享" @click="handleShare(scope.$index,scope.row)" type="text"><i
              class="fa fa-share-alt"></i></el-button>
            <el-button v-if="scope.row.tableType=='HDFS'" title="数据同步"
                       @click="handleSyn(scope.$index,scope.row)" type="text"><i
              class="fa fa-refresh" :class="{'fa-spin': scope.row.status=='SYNCING'}"></i></el-button>
            <template v-else-if="scope.row.supportDataSync">
              <el-button title="数据同步" @click="handleSyn(scope.$index,scope.row)" type="text"><i
                class="fa fa-refresh"
                :class="{'fa-spin': scope.row.status=='SYNCING'}"></i></el-button>
              <el-button title="查看数据库连接信息" @click="previewConn(scope.$index,scope.row)" type="text"><i
                class="fa fa-info"></i></el-button>
            </template>
            <el-button v-if="scope.row.status=='FAILED' && scope.row.tableType=='FLAT_FILE'" title="重新上传"
                       @click="handleUpload(scope.$index,scope.row)" type="text"><i
              class="fa fa-upload"></i></el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagesize">
      <div id="fy" class="block">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                       :current-page="pagination.currentPage" :page-sizes="[10, 20, 30, 50]"
                       :page-size="pagination.perSize" layout="total, sizes, prev, pager, next, jumper"
                       :total="pagination.totalSize"></el-pagination>
      </div>
    </div>
  </div>
</template>
<style>
</style>
<script>
  export default {
    name: "DataSourceList",
    data: function () {
      return {
        tableHeight: 100
      }
    },
    created: function () {//组件创建后获取数据
      if (this.permission.data.indexOf("datasource:search") > -1) {
        this.$store.dispatch("getMenuDatabaseList");
      }
    },
    computed: {
      deals: function () {
        return this.$store.state.datasource.deals;
      },
      dbListItems: function () {//获取数据源列表集合
        return this.$store.state.datasource.databaseList;
      },
      pagination: function () {
        return this.$store.state.datasource.pagination;
      },
      permission: function () {
        return this.$store.state.global.permission;
      },
      fileUpload: function () {
        return this.$store.state.datasource.fileUpload;
      }
    },
    methods: {
      parseTimeToDate: function (timeStep) {//将时间戳转成yyyy-mm-dd hh:mm:ss
        if (timeStep == null || timeStep == "" || timeStep == 0) {
          return null;
        }
        var time = new Date(timeStep);
        var y = time.getFullYear();
        var m = time.getMonth() + 1;
        var d = time.getDate();
        var h = time.getHours();
        var mm = time.getMinutes();
        var s = time.getSeconds();
        return y + '-' + this.addNum(m) + '-' + this.addNum(d) + ' ' + this.addNum(h) + ':' + this.addNum(mm) + ':' + this.addNum(s);
      },
      addNum: function (m) {
        return m < 10 ? '0' + m : m
      },
      handleSizeChange(val) {//选择不同页数时触发事件
        this.pagination.perSize = val;//修改每页条数
        if (!this.checkPermission("datasource:search")) {
          return;
        }
        this.$store.dispatch("getMenuDatabaseList");//重新获取数据
      },
      handleCurrentChange(val) {//当前页变化时触发事件
        this.pagination.currentPage = val;//修改当前页
        if (!this.checkPermission("datasource:search")) {
          return;
        }
        this.$store.dispatch("getMenuDatabaseList");//重新获取数据
      },
      //查看链接信息
      previewConn(index, row) {
        try {
          var that = this;
          this.$.ajax({
            url: that.$store.state.global.httpServer + "/api/datasource/" + row.id + "/connection/info",
            method: "get",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
              xhr.setRequestHeader("accessToken",localStorage.accessToken);
              that.deals.previewConnDetail = [];
            },
            success: function (data) {
              if (data.status == "SUCCESS") {
                if (data.data) {
                  that.deals.previewConnDetail.push(data.data);
                  that.deals.statue.previewConnection = true;
                } else {
                  that.$notify({
                    title: "提示",
                    message: "无连接信息！",
                    type: "warning"
                  });
                }
              } else if (data.status == "FAIL") {
                that.$notify.error({
                  title: "错误",
                  message: data.message
                });
              }
            },
            error: function (response) {
              that.$store.commit("dealRequestError", response);
            }
          });
        } catch (e) {
          console.error(e.message)
        }
      },
      handlePreview: function (index, row) {//预览事件 -获取预览数据-弹出预览窗体-加载预览数据
        this.deals.dealItem = row;
        if (!this.checkPermission("datasource:preview")) {
          return;
        }
        this.deals.statue.preview = true;
      },
      //上传失败的文件重新上传
      handleUpload(index, row) {
        this.fileUpload.deals.created = true;
        this.fileUpload.deals.uploadId = row.uploadId;
        this.deals.statue.fileUpload = true;
      },
      handleDelete: function (index, row) {//删除事件
        if (!this.checkPermission("datasource:delete")) {
          return;
        }
        this.deals.dealItem = row;
        this.deals.statue.delete = true;
      },
      handleShare: function (index, row) {//分享事件
        if (!this.checkPermission("datasource:share")) {
          return;
        }
        this.deals.dealItem = row;
        this.deals.statue.share = true;
      },
      handleSyn: function (index, row) { //同步数据源
        if (!this.checkPermission("datasource:syncTable")) {
          return;
        }
        var that = this;
        this.$.ajax({
          url: that.$store.state.global.httpServer + "/api/datasource/syncTable?tableName=" + row.showName,
          method: "get",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          success: function (data) {
            that.$notify({
              title: "提示",
              message: data.message,
              type: "success"
            });
            that.$store.dispatch("getMenuDatabaseList");//重新获取数据
          },
          error: function (response) {
            that.$store.commit("dealRequestError", response);
          }
        });
      },
      tableTypeFormat: function (row, column) {
        var tableType = row.tableType;
        var label = "";
        switch (tableType) {
          case "UNSTRUCTURED":
            label = "非结构化文件";
            break;
          case "RDBMS":
            label = "关系型数据库";
            break;
          case "FLAT_FILE":
            label = "结构化文件";
            break;
          case "HIVE":
            label = "HIVE数据源";
            break;
          case "HDFS":
            label = "HDFS数据源";
            break;
          default:
            label = tableType;
            break;
        }
        return label;
      },
      statusFormat: function (row, column) {
        var status = row.status;
        var label = "";
        switch (status) {
          case "NOTSYNCHRONIZED":
            label = "未同步";
            break;
          case "FINISH":
            label = "同步完成";
            break;
          case "SYNCING":
            label = "同步中";
            break;
          case "FAILED":
            label = "同步失败";
            break;
          default:
            label = status;
            break;
        }
        return label;
      },
      checkPermission: function (permissionStr) {
        var result = true;
        if (this.permission.data.indexOf(permissionStr) == -1) {
          result = false;
          this.$notify({
            type: "warning",
            message: this.permission.warningMsg.noPermission
          });
        }
        return result;
      }
    }
  }
</script>

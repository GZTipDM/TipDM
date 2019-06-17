<template>
  <div class="h100p">
    <el-table class="tableH" :data="sharedDatabaseList" border :height="tableHeight">
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
      <el-table-column :context="_self" align="left" width="200" label="操作">
        <template slot-scope="scope">
          <div id="funBtn" class="funBtn">
            <el-button title="预览" @click="handlePreview(scope.$index,scope.row)" type="text"><i
              class="fa fa-eye"></i></el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagesize">
      <div id="fy" class="block">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                       :current-page="sharePagination.currentPage" :page-sizes="[10, 20, 30, 50]"
                       :page-size="sharePagination.perSize" layout="total, sizes, prev, pager, next, jumper"
                       :total="sharePagination.totalSize"></el-pagination>
      </div>
    </div>
  </div>
</template>
<style>
</style>
<script>
  export default {
    name: 'SharedSourceList',
    data() {
      return {
        tableHeight: 100
      }
    },
    created: function () {//组件创建后获取数据
      if (this.permission.data.indexOf("datasource:shared") > -1) {
        this.$store.dispatch("getSharedMenuDatabaseList");
      }
    },
    computed: {
      deals: function () {
        return this.$store.state.datasource.deals;
      },
      sharedDatabaseList: function () {//获取数据源列表集合
        return this.$store.state.datasource.sharedDatabaseList;
      },
      sharePagination: function () {
        return this.$store.state.datasource.sharePagination;
      },
      permission: function () {
        return this.$store.state.global.permission;
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
        this.sharePagination.perSize = val;//修改每页条数
        if (!this.checkPermission("datasource:shared")) {
          return;
        }
        this.$store.dispatch("getSharedMenuDatabaseList");//重新获取数据
      },
      handleCurrentChange(val) {//当前页变化时触发事件
        this.sharePagination.currentPage = val;//修改当前页
        if (!this.checkPermission("datasource:shared")) {
          return;
        }
        this.$store.dispatch("getSharedMenuDatabaseList");//重新获取数据
      },
      handlePreview: function (index, row) {//预览事件 -获取预览数据-弹出预览窗体-加载预览数据
        if (!this.checkPermission("datasource:previewShardTable")) {
          return;
        }
        this.deals.dealItem = row;
        this.deals.previewList = [];//预览前清除历史预览数据
        var that = this;
        this.$.ajax({
          url: that.$store.state.global.httpServer + "/api/datasource/" + row.id + "/preview",
          method: "get",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          success: function (data) {
            if (data.status == "SUCCESS") {
              that.deals.previewList = data.data;
              that.deals.statue.preview = true;
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

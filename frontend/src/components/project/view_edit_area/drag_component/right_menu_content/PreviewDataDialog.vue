<template>
  <el-dialog title="预览数据" :visible.sync="viewEditArea.previewDialog" width="85%">
    <el-col :span="24" label="dd" class="fl">
      <div style="height:360px; overflow-y:auto">
        <el-table :row-class-name="'row_style'"  :data="previewList" v-loading="loading" element-loading-text="拼命加载中..." border
                  borderstyle="width: 100%" height="360" align="center">
          <el-table-column v-for="item in previewListTitle" :prop="item.name" :label="item.label"
                           width="200" :key="item.name"></el-table-column>
        </el-table>
      </div>
    </el-col>
    <el-col v-if="currentNodeItem.engine=='R'?true:(currentNodeItem.engine=='PYTHON'?true:false)" :span="24" style="text-align: center; padding: 10px 10px;">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.pageNumber"
        :page-sizes="[25, 50, 100]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total">
      </el-pagination>
    </el-col>
  </el-dialog>
</template>
<style>
</style>
<script>
  export default {
    name: 'PreviewDataDialog',
    data() {
      return {
        previewList: [],//表格数据
        previewListTitle: [],//表头数据
        pagination: {
          pageSize: 25,//每页条数
          pageNumber: 1,//当前页
          total: 0//总条数
        },
        loading: false
      }
    },
    computed: {
      flowData: function () {
        return this.$store.state.project.flowData;
      },
      otherState: function () {
        return this.$store.state.global.otherState;
      },
      viewEditArea: function () {
        return this.$store.state.project.viewEditArea;
      },
      previewDialog() {
        return this.viewEditArea.previewDialog;
      },
      currentNodeItem() {
        return this.flowData.currentNodeItem;
      }
    },
    watch: {
      previewDialog(bool) {
        if (bool) {
          this.getPreviewData();
        } else {
          this.previewList = [];
          this.previewListTitle = [];
          this.pagination.total = 0;
          this.pagination.pageSize = 25;
          this.pagination.pageNumber = 1;
        }
      }
    },
    methods: {
      handleSizeChange(val) {
        if (!this.loading) {
          this.pagination.pageSize = val;
          this.pagination.pageNumber = 1;
          this.getPreviewData();
        } else {
          this.$notify({
            type: "warning",
            message: "数据请求中，稍后再试！"
          });
        }
      },
      handleCurrentChange(val) {
        if (!this.loading) {
          this.pagination.pageNumber = val;
          this.getPreviewData();
        } else {
          this.$notify({
            type: "warning",
            message: "数据请求中，稍后再试！"
          });
        }
      },
      //获取预览数据
      getPreviewData() {
        var that = this;
        var reg = /^[1-9]+[0-9]*]*$/;//判断字符串是否为正整数的正则表达式
        var url = that.$store.state.global.httpServer + "/api/datasource/" + this.viewEditArea.previewTableName + "/preview";
        if (!reg.test(this.viewEditArea.previewTableName)) {
          url = that.$store.state.global.httpServer + "/api/project/" + this.flowData.currentProjectDetail.id + "/" + this.viewEditArea.previewTableName + "/data";
        }
        if (this.otherState.previewProjectFlow) {
          url = that.$store.state.global.httpServer + "/s/" + this.otherState.uuid + "/" + this.viewEditArea.previewTableName + "/data";
        }
        this.$.ajax({
          url: url,
          method: "get",
          dataType: "json",
          data: {
            pageNumber: that.pagination.pageNumber,
            pageSize: that.pagination.pageSize
          },
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
            that.previewList = [];
            that.previewListTitle = [];
            that.loading = true;
          },
          success: function (response) {
            switch (response.status) {
              case "SUCCESS":
                that.previewList = that.dealPreviewListData(response.data.content);
                that.pagination.total = response.data.totalElements;
                that.pagination.pageSize = response.data.size;
                break;
              case "FAIL":
                that.previewList = [];
                that.previewListTitle = [];
                that.pagination.total = 0;
                that.$notify.error({
                  title: "提示",
                  message: response.message
                });
                break;
            }
          },
          error: function (response) {
            that.$store.commit("dealRequestError", response);
          },
          complete: function (e) {
            that.loading = false;
          }
        });
      },
      //组装表头及内容
      dealPreviewListData(list) {
        var that = this;
        var result = [];
        var tempTitles = [];
        try {
          if (list.length > 0) {
            tempTitles = Object.keys(list[0]);
          }
          tempTitles.forEach(function (key) {
            var columnItem = {};
            if (key.indexOf(".") == -1) {
              columnItem["name"] = key;
              columnItem["label"] = key;
            } else {
              var temp = key.split(".");
              columnItem["name"] = temp[1];
              columnItem["label"] = temp[1];
            }
            that.previewListTitle.push(columnItem);
          });
          list.forEach(function (item) {
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
      }
    }
  }
</script>

<template>
  <div>
    <el-dialog width="80%" :title="'预览数据'+(dealItem.previewMode=='PAGEABLE'?'（分页加载）':'（前100条）')" top="10%"
               :visible.sync="dealStatue.preview" class="center">
      <el-col :span="24" label="dd" class="fl">
        <el-table :row-class-name="'row_style'" v-loading="loading" element-loading-text="拼命加载中..." :data="previewList" border
                  borderstyle="width: 100%" height="350">
          <el-table-column v-for="item in previewListTitle" :key="item.name" :prop="item.name" :label="item.label" width="150"
                           align="center"></el-table-column>
        </el-table>
      </el-col>
      <el-col :span="24">
        <el-pagination
          v-if="dealItem.previewMode=='PAGEABLE'"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[25, 50, 100, 200]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total">
        </el-pagination>
      </el-col>
    </el-dialog>
  </div>
</template>
<style>
</style>
<script>
  export default {
    name: 'ItemDataPreview',
    data: function () {
      return {
        view: false,
        previewList: [],
        previewListTitle: [],
        pagination: {
          currentPage: 1,//当前页
          pageSize: 100,//每页条数
          total: 0//总条数
        },//分页数据信息
        loading: false
      }
    },
    watch: {
      preview(val) {
        if (val) {
          this.getPreviewData();
        } else {
          this.previewList = [];//预览前清除历史预览数据
          this.pagination.currentPage = 1;
          this.pagination.pageSize = 100;
          this.pagination.total = 0;
        }
      }
    },
    computed: {
      dealItem() {//当前操作数据源
        return this.$store.state.datasource.deals.dealItem;
      },
      dealStatue: function () {//弹出框状态集
        return this.$store.state.datasource.deals.statue;
      },
      preview() {
        return this.dealStatue.preview;
      }
    },
    methods: {
      handleSizeChange(val) {
        if (!this.loading) {
          this.pagination.currentPage = 1;
          this.pagination.pageSize = val;
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
          this.pagination.currentPage = val;
          this.getPreviewData();
        } else {
          this.$notify({
            type: "warning",
            message: "数据请求中，稍后再试！"
          });
        }
      },
      cancel: function () {//隐藏弹出框
        this.dealStatue.preview = false;
      },
      getPreviewData() {
        var that = this;
        this.$.ajax({
          url: that.$store.state.global.httpServer + "/api/datasource/" + that.dealItem.id + "/preview?pageNumber=" + that.pagination.currentPage + "&pageSize=" + that.pagination.pageSize,
          method: "get",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          beforeSend: function (xhr) {
            that.previewList = [];
            that.loading = true;
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          success: function (data) {
            if (data.status == "SUCCESS") {
              that.pagination.currentPage = data.data.number + 1;
              that.pagination.pageSize = data.data.size;
              that.pagination.total = data.data.totalElements;
              that.dealPreviewListData(data.data.content);
            } else if (data.status == "FAIL") {
              that.$notify.error({
                title: "错误",
                message: data.message
              });
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
      //构建表头及列表数据
      dealPreviewListData(list) {
        var that = this;
        var tempTitles = [];
        this.previewListTitle = [];
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
            that.previewList.push(tempItem);
          });
        } catch (e) {
          console.error(e.message)
        }
      }
    }
  }
</script>

<template>
  <div>
    <el-dialog title="提示" width="30%" top="30vh" :visible.sync="dealStatue.delete"  class="center">
      <span>是否确定删除数据？</span>
      <span slot="footer" class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" @click="deleteSure">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<style>
</style>
<script>
  export default {
    name: 'ItemDelete',
    computed: {
      dealStatue: function () {
        return this.$store.state.datasource.deals.statue;
      }
    },
    methods: {
      cancel: function () {
        this.$store.state.datasource.deals.statue.delete = false;
      },
      deleteSure: function () {
        var that = this;
        //移除的项的id值
        var delItemId = this.$store.state.datasource.deals.dealItem.id;
        this.$.ajax({
          url: that.$store.state.global.httpServer + "/api/datasource/" + delItemId,
          method: "delete",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
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
              that.cancel();
              that.$store.dispatch("getMenuDatabaseList");//刷新数据列表
            } else if (data.status == "FAIL") {
              that.$notify.error({
                title: '失败',
                message: data.message,
              });
            }
          },
          error: function (response) {
            that.$store.commit("dealRequestError", response);
          }
        });
      }
    }
  }
</script>

<style>
  .center {
    text-align: center;
  }
</style>

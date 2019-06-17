<template>
  <div>
    <el-dialog title="数据库连接详情" :visible.sync="dealStatue.previewConnection" class="center">
      <el-card class="box-card">
        <template v-for="item in connDetails">
          <div class="text item">
            url： {{item.url}}
          </div>
          <div class="text item">
            用户名：{{item.userName}}
          </div>
          <!--<div class="text item">-->
          <!--密码：{{item.password}}-->
          <!--</div>-->
          <div class="text item" >
            sql： {{item.sql}}
          </div>
        </template>
      </el-card>
    </el-dialog>
  </div>
</template>
<style>
</style>
<script>
  export default {
    name: 'ItemPreviewConnection',
    computed: {
      deals() {
        return this.$store.state.datasource.deals;
      },
      dealStatue: function () {//弹出框状态集
        return this.deals.statue;
      },
      connDetails() {
        return this.deals.previewConnDetail;
      }

    },
    methods: {
      cancel: function () {//隐藏弹出框
        this.dealStatue.previewConnection = false;
      }
    }
  }
</script>
<style scoped>
  .text {
    font-size: 14px;
    word-wrap:break-word;
  }

  .item {
    padding: 10px 0;
  }
</style>

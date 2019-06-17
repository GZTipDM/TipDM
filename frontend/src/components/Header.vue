<!--头部-->
<template xmlns:v-popover="http://www.w3.org/1999/xhtml">
  <div id="header" class="header">
    <div class="logo">
      <img src="./../assets/img/logo_mob.svg">
    </div>
    <div class="ver">
      <h4>{{title}}</h4>
    </div>
    <el-popover
      ref="popover4"
      placement="bottom"
      width="400"
      trigger="click">
      <el-table :data="headerDeal.gridData" max-height="350">
        <el-table-column property="content" label="消息通知"></el-table-column>
        <el-table-column align="center" width="70" label="操作">
          <template slot-scope="scope">
            <div id="funBtn" class="funBtn">
              <el-button title="删除" @click="headerDeal.gridData.splice(scope.$index,1)" type="text"><i
                class="fa fa-trash"></i></el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-popover>
    <div class="tool">
      <a href="javascript:void(0)" @click="headerDeal.newMessage = false" v-popover:popover4>
        <el-badge :is-dot="headerDeal.newMessage">
          <i class="fa fa-bell-o" style="width:25px;"></i>
        </el-badge>
        <span>消息</span>
      </a>
      <el-tooltip id="userInfo" class="item" :enterable="true" popper-class="userInfoTip" placement="bottom-end">
        <div slot="content">
          <p>{{username}}</p>
        </div>
        <a href="javascript:void(0)">
          <i class="fa fa-user"></i>
          <span>用户</span>
        </a>
      </el-tooltip>
      <a href="javascript:void(0)" @click="exitSys"><i class="fa fa-sign-out"></i><span>注销</span></a>
      <a href="http://python.tipdm.org/bzzx/index.jhtml" target="_blank" ><i class="fa fa-question-circle"></i><span>帮助</span></a>
    </div>
  </div>
</template>
<style>
  .el-badge__content.is-fixed.is-dot {
    right: 20px;
    top: 14px;
  }
</style>
<script>
  import {mapGetters} from 'vuex'

  export default {
    name: 'Header',
    data: function () {
      return {}
    },
    computed: {
      ...mapGetters([
        'httpServer',
        'httpOauth',
        'httpClient',
        'permission',
        'username',
        'headerDeal',
        'title',
        'mode',
      ])
    },
    created: function () {
      //进入页面后启动接收消息处理状态
      this.$store.dispatch("openWebsocket");
    },
    methods: {
      exitSys: function () {//退出
        this.$confirm('确认退出系统?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.sureExit();
        }).catch(() => {
        });
      },
      sureExit: function () {
        location.href = `${this.httpOauth}/logout?accessToken=${localStorage.accessToken}
        &redirect=${this.httpClient}`;
      }
    }
  }
</script>

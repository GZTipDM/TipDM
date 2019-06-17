<template>
  <div id="app">
    <div id="loader-wrapper">
      <div id="loader"></div>
      <div class="loader-section section-left"></div>
      <div class="loader-section section-right"></div>
      <div class="load_title">正在加载...<br></div>
    </div>
    <div class="mainContent" v-cloak>
      <router-view/>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'App',
    beforeCreate: function () {
      var that = this;
      this.$.ajaxSettings.async = false;//同步执行
      //设置全局变量
      this.$.getJSON("./../../static/config.json", function (data) {
        document.title = data.title;
        that.$store.state.global.httpServer = data.httpServer;
        that.$store.state.global.httpOauth = data.httpOauth;
        that.$store.state.global.httpClient = data.httpClient;
        that.$store.state.global.socketServer = data.socketServer;
        that.$store.state.global.socketPort = data.socketPort;
        that.$store.state.global.title = data.title;
        that.$store.state.datasource.fileUpload.fileSize = data.databaseUploadFileSize;
        that.$store.state.global.mode = data.mode;
      });
      this.$.ajaxSettings.async = true;//异步执行
    }
  }
</script>
<style>
  @import "assets/css/loader.css";
  @import "assets/css/font-awesome.min.css";/*全局图标的*/
  @import "assets/css/animate.min.css"; /*css动画效果的*/
  @import "assets/css/reset.css";/*全局样式*/
  @import "assets/css/form.css";/**/

  @import "assets/css/project.css";/*工程主体样式*/
  @import "assets/css/components_m.css";/*组件主体样式*/
  @import "assets/css/control.css";
  [v-cloak] {
    display: none;
  }
  html,body,#app,.mainContent{width:100%;height:100%;margin:0;padding:0;}
</style>

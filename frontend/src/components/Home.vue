<!--主页-->
<template>
  <div class="warp">
    <Header></Header>
    <div class="main">
      <MenuBar></MenuBar>
      <template>
        <router-view></router-view>
      </template>
    </div>
  </div>
</template>

<script>
  import Header from './Header.vue';
  import MenuBar from './MenuBar.vue';
  export default {
    name: "Home",
    computed: {
      exceptionDeal() {
        return this.$store.state.global.exceptionDeal;
      },
      overtime() {
        return this.exceptionDeal.overtime;
      },//401超时
      accessRights() {
        return this.exceptionDeal.accessRights;
      },//403无权限
      notFound() {
        return this.exceptionDeal.notFound;
      },//404 error
      otherException() {
        return this.exceptionDeal.otherException;
      },//405 other
      checkExcuteStatue() {
        return this.exceptionDeal.checkExcuteStatue;
      }
    },
    watch: {
      overtime(value) {//超时401
        if (value) {
          this.$notify({
            message: this.exceptionDeal.exceptionMessage,
            type: "warning"
          });
          setTimeout(() => {
            this.exceptionDeal.overtime = !value;
            localStorage.removeItem("accessToken");
            location.href = this.$store.state.global.httpClient;
          }, 3000);
        }
      },
      accessRights(value) {//访问权限403
        if (!value) {
          this.$notify({
            message: this.exceptionDeal.exceptionMessage,
            type: "warning"
          });
          setTimeout(() => {
            this.exceptionDeal.accessRights = !value;
          }, 500);
        }
      },
      notFound(value) {//访问权限404
        if (value) {
          this.$notify({
            message: this.exceptionDeal.exceptionMessage,
            type: "warning"
          });
          setTimeout(() => {
            this.exceptionDeal.notFound = !value;
          }, 500);
        }
      },
      otherException(value) {//访问权限405
        if (value) {
          this.$notify.error({
            message: this.exceptionDeal.exceptionMessage
          });
          setTimeout(() => {
            this.exceptionDeal.otherException = !value;
          }, 500);
        }
      },
      checkExcuteStatue(bool) {
        this.$message({
          type: "info",
          message: "您的调度请求正在处理中"
        });
      }
    },
    mounted() {
      this.$('body').addClass('loaded');
      this.$('#loader-wrapper .load_title').remove();
    },
    components: {
      Header,
      MenuBar
    },
    created: function () {
      this.$.ajaxSettings.async = false;//同步执行
      this.getPermissionData();
      this.$.ajaxSettings.async = true;//异步执行
    },
    methods: {
      getPermissionData: function () {//获取token权限
        try {
          var that = this;
          this.$.ajax({
            url: that.$store.state.global.httpServer + "/token/info",
            method: "get",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
              xhr.setRequestHeader("accessToken",localStorage.accessToken);
            },
            success: function (rs) {
              switch (rs.status) {
                case "SUCCESS":
                  try {
                    //用户名
                    that.$store.state.global.username = rs.data.username;
                    //权限信息
                    var permissions = rs.data.permissions;
                    that.$store.state.global.permission.data = permissions;
                    var UserList = rs.data.shareable;
                    var rootNode = UserList.filter(item => item.pId == 0);
                    var result = [];
                    rootNode.forEach(function (item) {
                      if (item.explain == "false") {
                        item["children"] = [];
                        that.dealData(UserList, item);
                      }
                      item["key"] = "0_" + item.id;
                      result.push(item);
                    });
                    that.$store.state.global.sharedUser = result;
                  } catch (e) {
                    console.error(e.message)
                  }
                  break;
                case "FAIL":
                  if (rs.message == "token无效或已过期") {
                    that.$store.commit("dealRequestError", {
                      status: 401,
                      responseJSON: {
                        message: rs.message
                      }
                    });
                  }
                  break;
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
      dealData: function (UserList, item) {//组装树形结构
        var that = this;
        var childNode = UserList.filter(node => node.pId == item.id);
        childNode.forEach(function (childItem) {
          if (childItem.explain == "false") {//分组
            childItem["children"] = [];
            that.dealData(UserList, childItem);
          }
          childItem["key"] = item.id + "_" + childItem.id;
          item["children"].push(childItem);
        });
      }
    }
  }
</script>
<style>
  @import "../assets/css/home.css";/*框架主体样式*/
</style>

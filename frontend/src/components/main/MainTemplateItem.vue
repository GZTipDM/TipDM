<template>
  <a href="javascript:void(0)" class="pListLi" @mouseover="showDeleteIcon=true" @mouseout="showDeleteIcon=false">
    <template v-if="true">
      <div class="close" v-show="showDeleteIcon" @click="deleteTemplate($event)"><i class="el-icon-circle-close-outline"></i>
      </div>
    </template>
    <div @click="copyTemplate($event)">
      <div class="id"><i class="fa fa-diamond"></i></div>
      <div class="title">{{item.project.name}}</div>
      <div class="desc" :title="item.project.description">{{item.project.description}}</div>
      <div class="author"><i class="fa fa-user-circle-o"></i> {{item.project.creatorName}}</div>
      <div class="sort"><span>{{item.tags}}</span></div>
      <div class="copy"><span class="animated"><i class="fa fa-clipboard"></i>从模板新建</span></div>
    </div>
  </a>
</template>
<style>
</style>
<script>
  export default {
    name: "MainTemplateItem",
    props: ["item"],
    data() {
      return {
        showDeleteIcon: false//是否显示删除图标
      }
    },
    computed: {
      menuMain: function () {
        return this.$store.state.main;
      },
      flowData: function () {
        return this.$store.state.project.flowData;
      },
      rightContentTargetItem: function () {
        return this.flowData.rightContentTargetItem;
      },
      permission: function () {
        return this.$store.state.global.permission;
      },
      delLimit: function () {
        var result = false;
        try {
          result = (this.$.inArray('template:delete', this.permission.data) > -1);
        } catch (e) {
          console.error(e.message)
        }
        return result;
      }
    },
    created() {
      console.log(this.$.inArray('template:delete', this.permission.data) > -1)
    },
    methods: {
      copyTemplate(ev) {//复制模板为自己工程
        ev.preventDefault();
        if (this.flowData.isRunning) {
          this.$notify({
            type: "warning",
            message: "工程运行中，请稍后再试！"
          });
          return;
        }
        //设置添加位置
        this.flowData.rightContentTargetItem = this.$store.state.project.projectMenu[0];
        //显示弹框
        this.menuMain.showDetail.copyTemplateDialog = true;
        //设置复制项
        this.menuMain.currentTemplate = this.deepCopy(this.item);
      },
      deepCopy: function (source) {
        var result;
        (source instanceof Array) ? (result = []) : (typeof(source) === "object") ? (source === null ? (result = "") : (result = {})) : (result = source);
        for (var key in source) {
          result[key] = (typeof source[key] === 'object') ? this.deepCopy(source[key]) : source[key];
        }
        return result;
      },
      deleteTemplate: function (ev) {
        ev.preventDefault();
        this.$confirm('此操作将删除该模板, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.sureDelete();
        }).catch(() => {
        });
      },
      sureDelete: function () {
        var that = this;
        this.$.ajax({
          url: that.$store.state.global.httpServer + "/api/template/" + that.item.id,
          method: "delete",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          success: function (data) {
            switch (data.status) {
              case "SUCCESS":
                that.$notify({
                  message: "删除成功！",
                  type: 'success'
                });
                that.$store.dispatch("getTemplateList", 0);
                break;
              case "FAIL":
                that.$notify.error({
                  title: "错误",
                  message: data.message
                });
                break;
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

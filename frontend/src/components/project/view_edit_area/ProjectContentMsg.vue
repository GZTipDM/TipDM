<!--点击画布显示信息-->
<template>
  <el-card class="ProjectMsgCard" style="height:100%;">
    <div slot="header">
      <span>工程信息</span>
    </div>
    <div>
      <template v-if="currentProjectDetail.id">
        <el-form class="ProjectMsgForm" label-position="top" :model="currentProjectDetail">
          <el-form-item label="名称" style="margin-bottom: 10px;">
            <div class="content_item">
              {{currentProjectDetail.name}}
            </div>
          </el-form-item>
          <el-form-item label="模式" style="margin-bottom: 10px;">
            <div class="content_item">
              {{currentProjectDetail.paralleled == true ? "分布式" : "单机"}}
            </div>
          </el-form-item>
          <el-form-item label="创建时间" style="margin-bottom: 10px;">
            <div class="content_item">
              {{parseTimeToDate(currentProjectDetail.createTime)}}
            </div>
          </el-form-item>
          <el-form-item label="最近打开时间" style="margin-bottom: 10px;">
            <div class="content_item">
              {{parseTimeToDate(currentProjectDetail.lastOpenTime)}}
            </div>
          </el-form-item>
          <el-form-item label="描述" style="margin-bottom: 10px;">
            <div class="content_item">
              {{currentProjectDetail.description}}
            </div>
          </el-form-item>
        </el-form>
      </template>
      <template v-else>
        <span style="color:red;margin-top:15px;display:block">未选择工程</span>
      </template>
    </div>
  </el-card>
</template>

<script>
  export default {
    name: 'ProjectContentMsg',
    computed: {
      currentProjectDetail: function () {
        return this.$store.state.project.flowData.currentProjectDetail;
      },
      contentHeight() {
        var parentHeight = this.$store.state.project.flowData.propsContentHeight;
        return parentHeight - 60;
      },

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
      }
    }
  }
</script>
<style scoped>
  .content_item {
    padding-left: 20px;
  }

  .el-card__body {
    padding: 5px;
  }
</style>

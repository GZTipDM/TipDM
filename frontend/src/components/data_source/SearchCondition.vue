<template>
    <span>
        <div class="tfCell">
            <span class="cell-input" id="bm">
                <el-input placeholder="请输入表名" v-model="searchCondition.tableName" :maxlength="25"></el-input>
            </span>
        </div>
        <div class="tfCell">
            <span id="zt" class="cell-input" style="width:110px">
                <el-select v-model="searchCondition.statue" clearable placeholder="选择状态">
                    <el-option v-for="item in options" :label="item.label" :value="item.value"
                               :key="item.value"> </el-option>
                </el-select>
            </span>
        </div>
        <div class="tfCell" id="cjsj">
            <span class="cell-input">
                <el-date-picker v-model="searchCondition.datetime" format="yyyy-MM-dd HH:mm:ss" type="datetimerange"
                                :picker-options="pickerOptions2" start-placeholder="开始日期"
                                end-placeholder="结束日期" align="right"
                                style="width:370px"> </el-date-picker>
            </span>
        </div>
        <div class="tfCell">
            <div id="ss" class="fl">
                <el-button type="primary" @click="searchDBList">搜 索</el-button>
            </div>
        </div>
    </span>
</template>
<style>
</style>
<script>
  export default {
    name: 'SearchCondition',
    data: function () {
      return {
        options: [
          {value: 'NOTSYNCHRONIZED', label: '未同步'},
          {value: 'FINISH', label: '同步完成'},
          {value: 'SYNCING', label: '同步中'},
          {value: 'FAILED', label: '同步失败'}
        ],
        pickerOptions2: {
          shortcuts: [{
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }]
        }
      }
    },
    computed: {
      searchCondition: function () {//搜索条件
        return this.$store.state.datasource.searchCondition;
      },
      permission: function () {
        return this.$store.state.global.permission;
      }
    },
    methods: {
      searchDBList: function () {//查询
        if (this.permission.data.indexOf("datasource:search") == -1) {
          this.$notify({
            type: "warning",
            message: this.permission.warningMsg.noPermission
          });
          return;
        }
        if (this.searchCondition.datetime === null) {
          this.searchCondition.datetime = ["", ""];
        } else {
          var sTime = this.searchCondition.datetime[0];
          var eTime = this.searchCondition.datetime[1];
          this.searchCondition.datetime = [
            this.parseTimeToDate(sTime),
            this.parseTimeToDate(eTime)
          ];
        }
        this.$store.dispatch("getMenuDatabaseList");
      },
      parseTimeToDate: function (timeStep) {//将时间戳转成yyyy-mm-dd hh:mm:ss
        if (timeStep == null || timeStep == "" || timeStep == undefined || timeStep == "NaN") {
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

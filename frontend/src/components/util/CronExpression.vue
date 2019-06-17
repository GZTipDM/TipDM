<template>
  <el-tabs value="second" type="card">
    <!--<el-tab-pane label="秒" name="first">-->
      <!--<el-radio class="radio" v-model="cronData.second" label="*">每秒 允许的通配符[, - * /]</el-radio>-->
      <!--<br/>-->
      <!--<el-radio class="radio" v-model="cronData.second" label="1-2">周期从1-2秒</el-radio>-->
      <!--<br/>-->
      <!--<el-radio class="radio" v-model="cronData.second" label="0/1">从0秒开始，每1秒执行一次</el-radio>-->
      <!--<br/>-->
    <!--</el-tab-pane>-->
    <el-tab-pane label="分" name="second">
      <el-radio class="radio" v-model="cronData.minute" label="*">每分钟 允许的通配符[, - * /]</el-radio>
      <br/>
      <el-radio class="radio" v-model="cronData.minute" label="1-2">周期从1-2分钟</el-radio>
      <br/>
      <el-radio class="radio" v-model="cronData.minute" label="0/1">从0分钟开始，每1分钟执行一次</el-radio>
      <br/>
    </el-tab-pane>
    <el-tab-pane label="小时" name="third">
      <el-radio class="radio" v-model="cronData.hour" label="*">每小时 允许的通配符[, - * /]</el-radio>
      <br/>
      <el-radio class="radio" v-model="cronData.hour" label="1-2">周期从1-2小时</el-radio>
      <br/>
      <el-radio class="radio" v-model="cronData.hour" label="0/1">从0小时开始，每1小时执行一次</el-radio>
      <br/>
    </el-tab-pane>
    <el-tab-pane label="日" name="fourth">
      <el-radio class="radio" v-model="cronData.day" label="?">不指定</el-radio>
      <br/>
      <el-radio class="radio" v-model="cronData.day" label="*">每天 允许的通配符[, - * / L W]</el-radio>
      <br/>
      <el-radio class="radio" v-model="cronData.day" label="1-2">周期 从1 - 2日</el-radio>
      <br/>
      <el-radio class="radio" v-model="cronData.day" label="1/1">从1日开始，每1天执行一次</el-radio>
      <br/>
      <el-radio class="radio" v-model="cronData.day" label="1W">每月1号最近的那个工作日</el-radio>
      <br/>
      <el-radio class="radio" v-model="cronData.day" label="L">本月最后一天</el-radio>
      <br/>
    </el-tab-pane>
    <el-tab-pane label="月" name="fiveth">
      <el-radio class="radio" v-model="cronData.month" label="?">不指定</el-radio>
      <br/>
      <el-radio class="radio" v-model="cronData.month" label="*">每月 允许的通配符[, - * /]</el-radio>
      <br/>
      <el-radio class="radio" v-model="cronData.month" label="1-2">周期 从1 - 2月</el-radio>
      <br/>
      <el-radio class="radio" v-model="cronData.month" label="1/1">从1日开始，每1月执行一次</el-radio>
      <br/>
    </el-tab-pane>
    <el-tab-pane label="周" name="sixth">
      <el-radio class="radio" v-model="cronData.week" label="?">不指定</el-radio>
      <br/>
      <el-radio class="radio" v-model="cronData.week" label="*">每周 允许的通配符[, - * / L #]</el-radio>
      <br/>
      <el-radio class="radio" v-model="cronData.week" label="1-2">周期 从星期1 - 2</el-radio>
      <br/>
      <el-radio class="radio" v-model="cronData.week" label="1#1">第1周的星期1</el-radio>
      <br/>
      <el-radio class="radio" v-model="cronData.week" label="1L">本月最后一个星期1</el-radio>
      <br/>
    </el-tab-pane>
    <el-tab-pane label="年" name="seventh">
      <el-radio class="radio" v-model="cronData.year" label="">不指定 允许的通配符[,-*]非必填</el-radio>
      <br/>
      <el-radio class="radio" v-model="cronData.year" label="*">每年</el-radio>
      <br/>
      <el-radio class="radio" v-model="cronData.year" label="2017-2018">周期从2017-2018年</el-radio>
      <br/>
    </el-tab-pane>
  </el-tabs>
</template>
<style>
</style>
<script>
  export default {
    name: 'CronExpression',
    props: ["item"],
    data() {
      return {
        cronData: {
          second: '*',
          minute: '*',
          hour: '*',
          day: '*',
          month: '*',
          week: '?',
          year: '*'
        }
      }
    },
    computed: {
      second: function () {
        return this.cronData.second;
      },
      minute: function () {
        return this.cronData.minute;
      },
      hour: function () {
        return this.cronData.hour;
      },
      day: function () {
        return this.cronData.day;
      },
      month: function () {
        return this.cronData.month;
      },
      week: function () {
        return this.cronData.week;
      },
      year: function () {
        return this.cronData.year;
      }
    },
    watch: {
//      second: function (val) {
//        switch (val) {
//          case '*':
//            this.cronData.minute = "*";
//            this.cronData.hour = "*";
//            this.cronData.day = "*";
//            this.cronData.month = "*";
//            this.cronData.week = "?";
//            this.cronData.year = "*";
//            break;
//        }
//        this.ItemChange();
//      },
      minute: function (val) {
        switch (val) {
          case '*':
            this.cronData.hour = "*";
            this.cronData.day = "*";
            this.cronData.month = "*";
            this.cronData.week = "?";
            this.cronData.year = "*";
            break;
          case '1-2':
          case '0/1':
            if (this.cronData.second == "*") {
              this.cronData.second = 0;
            }
            break;
        }
        this.ItemChange();
      },
      hour: function (val) {
        switch (val) {
          case '*':
            this.cronData.day = "*";
            this.cronData.month = "*";
            this.cronData.week = "?";
            this.cronData.year = "*";
            break;
          case '1-2':
          case '0/1':
            if (this.cronData.second == "*") {
              this.cronData.second = 0;
            }
            if (this.cronData.minute == "*") {
              this.cronData.minute = 0;
            }
            break;
        }
        this.ItemChange();
      },
      day: function (val) {
        switch (val) {
          case '*':
            this.cronData.month = "*";
            this.cronData.week = "?";
            this.cronData.year = "*";
            break;
          case '?':
          case '1-2':
          case '1/1':
          case '1W':
          case 'L':
            if (this.cronData.second == "*") {
              this.cronData.second = 0;
            }
            if (this.cronData.minute == "*") {
              this.cronData.minute = 0;
            }
            break;
        }
        this.ItemChange();
      },
      month: function (val) {
        switch (val) {
          case '*':
            this.cronData.week = "?";
            this.cronData.year = "*";
            break;
          case '?':
          case '1-2':
          case '1/1':
            if (this.cronData.second == "*") {
              this.cronData.second = 0;
            }
            if (this.cronData.minute == "*") {
              this.cronData.minute = 0;
            }
            if (this.cronData.hour == "*") {
              this.cronData.hour = 0;
            }
            break;
        }
        this.ItemChange();
      },
      week: function (val) {
        switch (val) {
          case '*':
            this.cronData.year = "*";
            break;
          case '?':
          case '1/2':
          case '1#1':
          case '1L':
            if (this.cronData.second == "*") {
              this.cronData.second = 0;
            }
            if (this.cronData.minute == "*") {
              this.cronData.minute = 0;
            }
            if (this.cronData.hour == "*") {
              this.cronData.hour = 0;
            }
            if (this.cronData.day == "*") {
              this.cronData.day = 0;
            }
            if (this.cronData.month == "*") {
              this.cronData.month = 0;
            }
            break;
        }
        this.ItemChange();
      },
      year: function (val) {
        switch (val) {
          case '*':
            break;
          case '':
          case '2017-2018':
            if (this.cronData.second == "*") {
              this.cronData.second = 0;
            }
            if (this.cronData.minute == "*") {
              this.cronData.minute = 0;
            }
            if (this.cronData.hour == "*") {
              this.cronData.hour = 0;
            }
            if (this.cronData.day == "*") {
              this.cronData.day = 0;
            }
            if (this.cronData.day == "*") {
              this.cronData.day = 0;
            }
            if (this.cronData.month == "*") {
              this.cronData.month = 0;
            }
            if (this.cronData.week == "*") {
              this.cronData.week = 0;
            }
            break;
        }
        this.ItemChange();
      }
    },
    methods: {
      ItemChange: function () {
        var arr = [];
        for (var key in this.cronData) {
          arr.push(this.cronData[key]);
        }
        this.item.expression = arr.join(" ");
      }
    }
  }
</script>

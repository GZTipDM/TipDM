<!--登录导航页-->
<template>
  <span>
    <div class="topTool">
      <div class="ver" style="margin-top: 60px; font-size: 2em"><h1 id="projectName">
        {{title}}
      </h1></div>
      <div class="regLog"><a href="javascript:void(0)" @click="login">登录</a><a href="###" @click="register">注册</a>
      </div>
    </div>
    <div id="gBanner" class="gBanner">
      <div class="hd">
        <ul>
        </ul>
      </div>
      <div class="bd">
        <ul>
          <li class="li1">
            <div class="content animated"><img src="../assets/out_resource/images/hk.svg"></div>
            <div class="img"></div>
            <div class="tit">全面的 &nbsp; 数据挖掘算法集<br><span>轻松实现数据挖掘，关联分析、聚类预测、分类预测等多种模型构建</span></div>
            <div class="bgImg animated infinite flash"></div>
          </li>
          <li class="li2">
            <div class="content animated"><img src="../assets/out_resource/images/zujian.svg"></div>
            <div class="img"></div>
            <div class="headline-bg">
              <canvas id="waves" class="waves"></canvas>
            </div>
            <div class="tit">简便的 &nbsp; 流程化建模<br><span>易学习、好理解、极速建模分析</span></div>
            <div class="bgImg"></div>
          </li>
          <li class="li3">
            <div class="content animated">
              <div id="viewChart" class="viewChart" style="width:400px;height: 335px;"></div>
            </div>
            <div class="img"></div>
            <div class="tit">丰富的 &nbsp; 可视化展示<br><span>创意设计与技术完美结合，数据直观美观</span></div>
            <div class="bgImg animated infinite pulse"></div>
          </li>
        </ul>
      </div>
    </div>
  </span>
</template>

<script>
  require('../assets/out_resource/js/jquery-1.4.2.min')
  require('../assets/out_resource/js/jquery.SuperSlide');
  import {mapGetters} from 'vuex'

  const isDev = process.env.NODE_ENV !== 'production';
  export default {
    name: "LoginBanner",
    data: function () {
      return {
        loading1: false,
        clientId: "2f59ad62-e916-4411-87cc-e58223205445",
        clientSecret: "9O_i2eE3Q9yq9C-eKqsVyA"
      }
    },
    computed: {
      ...mapGetters([
        'httpServer',
        'httpClient',
        'httpOauth',
        'title'
      ])
    },
    created() {
      if ("Chrome" != this.myBrowser()) {
        alert("推荐使用Chrome内核浏览器!");
      }
      //单点登录
      this.onload();
      if(isDev) {
        this.applyAccessToken();
//        this.checkToken();
      }
    },
    mounted() {
      this.$('body').addClass('loaded');
      this.$('#loader-wrapper .load_title').remove();
      $("#gBanner").slide({
        mainCell: ".bd ul",
        titCell: ".hd ul",
        autoPlay: true,
        effect: "left",
        autoPage: true,
        interTime: "5000"
      });
      this.fillEchart();
      setInterval(this.gbAnimate(), 100);
    },
    methods: {
      applyAccessToken() {
        if (!localStorage.getItem("accessToken")) {//判断本地accessToken是否存在，不存在
          var code = this.getParameter("code");//从网址栏获取返回的code
          if (code != null) {//地址栏存在code，进入
            var url = this.httpOauth + "/accessToken";
            var httpClient = this.$store.state.global.httpClient;
            var that = this;
            this.$.ajax({
              url: url,
              type: "POST",
              dataType: "json",
              data: {
                grant_type: "authorization_code",
                client_id: that.clientId,
                client_secret: that.clientSecret,
                code: code,
                redirect_uri: httpClient
              },
              success: function (data) {
//                data = JSON.parse(data.replace(/&quot;/g,"\""));
                localStorage.setItem("accessToken", data.access_token);
                location.href = httpClient + "/home/main";
              }
            });
          }
        } else {//本地accessToken已存在,校验时效性
          this.checkToken();//校验token时效性，失效则重新授权
        }
      },
      onload() {
        var sessionId = this.getParameter("TIPDM_SESSIONID");
        if (sessionId) {
          localStorage.setItem("accessToken", sessionId);
          this.checkToken();
        }
        var loggedOut = this.getParameter("logged_out");
        if (loggedOut) {
          localStorage.removeItem("accessToken");
        }
      },//单点登录信息
      fillEchart() {
        var viewChart = this.$echarts.init(document.getElementById('viewChart'));
        var option = {
          tooltip: {trigger: 'item', formatter: "{b}：{d}%"},
          calculable: true,
          series: [
            {
              type: 'pie',
              radius: [30, 140],
              center: ['50%', '50%'],
              roseType: 'area',
              x: '50%',
              max: 40,
              sort: 'ascending',
              data: [
                {value: 10, name: 'class1'},
                {value: 5, name: 'class2'},
                {value: 15, name: 'class3'},
                {value: 25, name: 'class4'},
                {value: 20, name: 'class5'},
                {value: 35, name: 'class6'},
                {value: 30, name: 'class7'},
                {value: 40, name: 'class8'}
              ],
              itemStyle: {normal: {label: {textStyle: {color: '#000'}}}}
            }
          ]
        };
        viewChart.setOption(option);
        viewChart.resize();
      },
      gbAnimate() {
        var k = $('#gBanner .hd ul li');
        var y = $('#gBanner .bd ul li .content');
        if ($(k[0]).hasClass("on")) {
          $(y[0]).addClass('zoomInDown');
        } else {
          $(y[0]).removeClass('zoomInDown');
        }
        if ($(k[1]).hasClass("on")) {
          $(y[1]).addClass('rollIn');
        } else {
          $(y[1]).removeClass('rollIn');
        }
        if ($(k[2]).hasClass("on")) {
          $(y[2]).addClass('rotateIn');
        } else {
          $(y[2]).removeClass('rotateIn');
        }
      },
      getParameter(name) {
        var url = window.location.href;
        var reg = new RegExp(name + '=([^&]*)(?:&)?');
        return url.match(reg) && url.match(reg)[1];
      },
      login() {
//        if(isDev) {
          location.href = this.httpOauth + "/authorize?client_id="+this.clientId+"&response_type=code&redirect_uri=" + this.$store.state.global.httpClient + "/login_banner";
//        } else if(!isDev) {
//          location.href = this.httpClient + "/authorize";
//        }
      },//登录，重新获取授权
      myBrowser() {
        var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
        var isOpera = userAgent.indexOf("Opera") > -1;
        if (isOpera) {
          return "Opera"
        } //判断是否Opera浏览器
        if (userAgent.indexOf("Firefox") > -1) {
          return "FF";
        } //判断是否Firefox浏览器
        if (userAgent.indexOf("Chrome") > -1) {
          return "Chrome";
        }
        if (userAgent.indexOf("Safari") > -1) {
          return "Safari";
        } //判断是否Safari浏览器
        if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera) {
          return "IE";
        } //判断是否IE浏览器
      },
      checkToken() {
        var that = this;
        $.ajax({
          url: that.httpServer + "/token/check",
          method: "get",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          success: function (data) {
            if (data == null) {
              localStorage.removeItem("accessToken");
              that.$store.commit("dealRequestError", {
                status: 401,
                responseJSON: {
                  message: "服务器无法连接，请联系管理员！"
                }
              });
            } else {
              if (data.status == "SUCCESS") {
                location.href = "/home/main";
              } else if (data.status == "FAIL") {
                localStorage.removeItem("accessToken");
                console.log("token 失效");
              }
            }
          },
          error: function (error) {
            console.error(error);
          }
        });
      }
    }
  }
</script>
<style>
  @import "../assets/out_resource/css/guide.css";
</style>

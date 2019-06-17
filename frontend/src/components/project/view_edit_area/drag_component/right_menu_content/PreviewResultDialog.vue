<template>
  <el-dialog :title="title" top="6%" :visible.sync="previewData.previewResultDialog" class="center" width="85%">
    <el-col :span="24" label="dd" class="fl">
      <div id="report" style="height:400px;overflow: auto; line-height:1;background:#f5f5f5;"
           v-if="previewData.previewResultData != ''">
        <iframe crossorigin="anonymous" v-if="isNotReport" :src="previewData.previewResultData" width="100%" height="100%" scrolling="auto"></iframe>
        <div v-else v-html="content" id="content" class="reportContent" contenteditable="true"></div>
      </div>
      <div style="height:400px; overflow: auto;" v-else>
        无报告生成
      </div>
    </el-col>
    <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="downHtmlToPdf" size="small" v-show="!isNotReport" :disabled="downStatus">下 载</el-button>
        </span>
  </el-dialog>
</template>
<script>
  var html2canvas = require("html2canvas");
  var jsPDF = require("jspdf");
  export default {
    name: 'PreviewResultDialog',
    data() {
      return {
        content: "",
        downStatus: false,
        title: '算法运行报告'
      }
    },
    computed: {
      previewData: function () {
        return this.$store.state.project.viewEditArea;
      },
      previewResultDialog: function () {
        return this.previewData.previewResultDialog;
      },
      isNotReport(){
          console.log(this.previewData.previewResultData)
          if(this.previewData.previewResultData.indexOf("/overview") != -1){
              this.title = "数据探索分析报告";
              return true;
          } else {
              this.title = "算法运行报告";
              return false;
          }
      }
    },
    watch: {
      previewResultDialog(val) {
        if (val) {
          this.downHtmlStream();
          this.downStatus = false;
        } else {
          this.content = "";
        }
      }
    },
    methods: {
      downHtmlStream() {
        var that = this;
        this.$.ajax({
          type: "get",
          async: false,
          mimeType: 'text/plain; charset=utf-8',
          url: that.previewData.previewResultData,
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          success: function (data) {
            that.content = data;
          },
          error: function (response) {
            that.$store.commit("dealRequestError", response);
          }
        });
      },
      downHtmlToPdf() {
        this.downStatus = true;
        var that = this;
        setTimeout(function () {
          //                html2canvas(document.getElementsByTagName('IFRAME')[0].contentWindow.document.body,{
          html2canvas(document.getElementById("content"), {
            allowTaint: false,
            useCORS: true,
            logging: true
          }).then(canvas => {
            var contentWidth = canvas.width;
            var contentHeight = canvas.height;
            //一页pdf显示html页面生成的canvas高度;
            var pageHeight = contentWidth / 592.28 * 841.89;
            //未生成pdf的html页面高度
            var leftHeight = contentHeight;
            //pdf页面偏移
            var position = 0;
            //a4纸的尺寸[595.28,841.89]，html页面生成的canvas在pdf中图片的宽高
            var imgWidth = 555.28;
            var imgHeight = 555.28 / contentWidth * contentHeight;

            var pageData = canvas.toDataURL('image/jpeg', 1.0);

            var pdf = new jsPDF('', 'pt', 'a4');

            try{
              //有两个高度需要区分，一个是html页面的实际高度，和生成pdf的页面高度(841.89)
              //当内容未超过pdf一页显示的范围，无需分页
              if (leftHeight < pageHeight) {
                pdf.addImage(pageData, 'JPEG', 20, 0, imgWidth, imgHeight);
              } else {
                while (leftHeight > 0) {
                  pdf.addImage(pageData, 'JPEG', 20, position, imgWidth, imgHeight)
                  leftHeight -= pageHeight;
                  position -= 841.89;
                  //避免添加空白页
                  if (leftHeight > 0) {
                    pdf.addPage();
                  }
                }
              }
            } catch(e) {console.log(e.message)}
            pdf.save('report.pdf');
          })
          that.downStatus = false;
        }, 500);
      }
    }
  }
</script>

<template>
  <div>
    <div id="picker" class="checks">选择文件</div>
    <button id="ctlBtn">开始上传</button>
    <!--用来存放文件信息-->
    <div id="thelist" class="uploader-list" style="text-align:left;">
      <div v-for="(item,index) in fileItems" :key="item.id">
        <span style="padding: 0px 5px;">{{item.fileName}}</span>
        <span style="padding: 0px 5px;">{{item.status}}</span>
        <span style="padding: 0px 5px;">{{item.progress}}</span>
        <el-button title="删除" @click="deleteItem($event,index)" type="text"><i
          class="fa fa-trash"></i></el-button>
      </div>
    </div>
  </div>
</template>
<style>
  button#ctlBtn {
    display: none;
  }
</style>
<script>
  import WebUploader from 'webuploader'
  import 'webuploader/css/webuploader.css'

  export default {
    name: 'Uploader',
    data() {
      return {
        fileSize: 10//限制文件上传大小
      }
    },
    created() {
      this.fileSize = this.$store.state.datasource.fileUpload.fileSize;
    },
    mounted: function () {
      var that = this;
      var state = 'pending';
      var fileMD5 = null;//上传文件的MD5值
      //var existFile = false; //校验服务器是否存在file
      var uploader = WebUploader.create({
        pick: {
          id: '#picker',
          multiple: false //是否开起同时选择多个文件能力
        },
        accept: {
          title: "暂时支持csv文件",
          extensions: "csv" //允许的文件后缀
        },
        chunked: true,// 开起分片上传
        chunkSize: 1024 * 1024,//如果要分片，每片大小 1*1024KB 1M
        //auto: true, //是否开启自动上传
        server: that.$store.state.global.httpServer + "/api/file/flat/upload",
        formData: {//文件上传请求的参数表，每次发送都会发送此对象中的参数
        },
        method: 'post',
        fileNumLimit: 2,  //设置为2方便切换，实际限制一个文件上传
        fileSizeLimit: that.fileSize * 1024 * 1024,    // 10 M 队列文件大小限制
        fileSingleSizeLimit: that.fileSize * 1024 * 1024   // 10 M 单个文件大小限制

      });
      //文件被添加到队列之前调用
      uploader.on('beforeFileQueued', function (file) {
        uploader.reset();//重置队列
        that.fileUpload.fileItems = [];//置空列表
      });
      // 当文件被加入队列以后触发
      uploader.on('fileQueued', function (file) {
        uploader.md5File(file)
        .progress(function (percentage) {// 及时显示进度
        }).then(function (val) {// 完成
          console.log('md5 result:', val);
          that.fileUpload.fileMD5 = val;
          fileMD5 = val;
        });
        var sourceFile = file.source.source;//获取上传的文件
        that.fileUpload.dataColumnName = [];//清空预览数据列头
        that.fileUpload.dataList = [];//清空预览数据
        that.fileUpload.uploadFile = sourceFile;
        that.fileUpload.fileItems = [{
          id: file.id,
          fileName: file.name,
          status: '等待上传...',
          progress: ''
        }];
      });
      // 文件上传过程中创建进度条实时显示。
      uploader.on('uploadProgress', function (file, percentage) {
        that.fileUpload.fileItems = [{
          id: file.id,
          fileName: file.name,
          status: '上传中...',
          progress: percentage * 100 + '%'
        }];
      });
      //当某个文件的分块在发送前触发，主要用来询问是否要添加附带参数，大文件在开起分片上传的前提下此事件可能会触发多次。
      uploader.on('uploadBeforeSend', function (object, data, headers) {
        headers.accessToken = localStorage.accessToken;
        data.fileMD5 = fileMD5; //完整文件的MD5值
        data.tableName = that.fileUpload.deals.tableName;//表名称
        data.encoding = that.fileUpload.deals.encoding;
        data.delimiter = that.fileUpload.deals.delimiter;
        data.uploadId = that.fileUpload.deals.uploadId;
        data.header = that.fileUpload.deals.ifFirstRowAsColumnName;
      });
      //文件上传成功时触发
      uploader.on('uploadSuccess', function (file, response) {
        console.log(response);
        //{message: "文件上传成功", status: "SUCCESS", _raw: "{"message":"文件上传成功","status":"SUCCESS"}"}
        that.fileUpload.fileItems = [{
          id: file.id,
          fileName: file.name,
          status: '已上传',
          progress: ''
        }];
        if (response.status == 'SUCCESS') {
          that.$notify({
            title: '成功',
            message: response.message,
            type: 'success'
          });
        } else if (response.status == 'FAIL') {
          that.$notify.error({
            title: '提示',
            message: response.message
          });
        }
      });
      //当所有文件上传结束时触发。
      uploader.on('uploadFinished', function () {
        //完成创建，取消弹出框，刷新列表
        that.$store.state.datasource.deals.statue.fileUpload = false; //隐藏弹出框
        that.$emit("resetForm");//重置表单
        that.$store.dispatch("getMenuDatabaseList");
      });
      //当某个文件上传到服务端响应后，会派送此事件来询问服务端响应是否有效。如果此事件handler返回值为false, 则此文件将派送server类型的uploadError事件。
      uploader.on('uploadAccept', function (object, ret) {
        if(ret.status === "FAIL") {
          that.$notify.error({
            message: ret.message
          });
        }
      });
      uploader.on('error', function (type) {
        switch (type) {
          case "Q_EXCEED_SIZE_LIMIT":
            that.$notify({
              title: "提示",
              message: "文件大小超出限制，文件限制大小不超过" + that.fileSize + "M！",
              type: "warning"
            });
            break;
          case "Q_EXCEED_NUM_LIMIT":
            that.$notify({
              title: "提示",
              message: "文件只支持单文件上传！",
              type: "warning"
            });
            break;
          case "Q_TYPE_DENIED":
            that.$notify({
              title: "提示",
              message: "文件只支持csv文件上传！",
              type: "warning"
            });
            break;
        }
      });

      //上传出错时触发
      uploader.on('uploadError', function (file) {
        that.fileUpload.fileItems = [{
          id: file.id,
          fileName: file.name,
          status: '上传出错',
          progress: ''
        }];
      });
      //上传完全时触发
      uploader.on('uploadComplete', function (file) {
         console.log("uploadComplete...");
      });

      uploader.on('all', function (type) {
        if (type === 'startUpload') {
          state = 'uploading';
        } else if (type === 'stopUpload') {
          state = 'paused';
        } else if (type === 'uploadFinished') {
          state = 'done';
        }

        if (state === 'uploading') {
          that.$('#ctlBtn').text('暂停上传');
        } else {
          that.$('#ctlBtn').text('开始上传');
        }
      });
      that.$('#ctlBtn').on('click', function (ev) {
        ev.preventDefault();//阻止默认动作即该链接不会跳转
        if (state === 'uploading') {
          uploader.stop();
        } else {
          if (ev.keyCode == undefined) {//避免键盘事件触发子组件文件上传
            uploader.upload();
          }
        }
      });
    },
    computed: {
      fileItems: function () {//文件列表，单文件
        return this.$store.state.datasource.fileUpload.fileItems;
      },
      fileUpload: function () {
        return this.$store.state.datasource.fileUpload;
      }
    },
    methods: {
      deleteItem: function (event,index) {//移除待上传的文件
        event.preventDefault();
        //移除列表中该元素
        this.fileUpload.fileItems.splice(index, 1);
        //清除vuex中的uploadFile
        this.fileUpload.uploadFile = "";
      }
    }
  }
</script>

<style scoped>
  .checks {
    float: left;
  }

  .upload {
    float: left;
    padding: 10px;
    margin: 0px 10px;
  }
</style>

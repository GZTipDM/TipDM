<!--带校验的通过remoteUrl链接填充的下拉单选框-->
<template>
  <el-form v-show="item.visible" label-position="top" :model="item" :rules="rules" class="demo-form-stacked"
           :style="paddingStyle">
    <el-form-item :label="item.label" prop="value">
      <template v-if="item.toolTip">
        <el-tooltip effect="light" placement="bottom-end"
                    class="hint_information">
          <div slot="content">
            <template v-for="i in item.toolTip.split(';')">
              {{i}} <br/>
            </template>
          </div>
          <i class="fa fa-question-circle row_information" style="cursor:pointer;"></i>
        </el-tooltip>
      </template>
      <el-select v-model="item.value" :placeholder="item.placeholder" size="small" style="width:100%">
        <el-option v-for="option in dealOptions(item.options)" size="small" :key="option.value" :value="option.value"
                   :label="option.text"></el-option>
      </el-select>
    </el-form-item>
  </el-form>
</template>
<script>
  //下拉单选框 my-selected-single item.value 的格式为"aa:AA;bb:BB;cc:CC"
  export default {
    name: 'TipFillSelectedByConnection',
    props: ["item", "index"],
    data: function () {
      var valueValidator = (rule, value, callback) => {
        if(this.item.required) {
          try {
            var reg = eval(this.item.rexp);
            if (!reg.test(value)) {
              callback(new Error(this.item.placeholder));
            }
          } catch(e) {
            console.log(e.message)
            callback(new Error("校验异常！"))
          }
        }
        callback();
      };
      return {
        paddingStyle: {
          paddingLeft: '5px',
          paddingRight: '5px'
        },
        rules: {
          value: [
            {required: this.item.required, validator: valueValidator, trigger: 'change'}
          ]
        }
      }
    },
    created: function () {
      //当remoteUrl已经配置，并且options为空的时候，才会请求数据
      if (this.item.extra.remoteUrl && this.item.options.trim().length == 0) {
        this.getData();
      }
    },
    methods: {
      dealOptions: function (optionStr) {
        var result = [];
        try {
          var array = optionStr.split(";");
          for (var int = 0; int < array.length; int++) {
            result.push({text: array[int].split(":")[0], value: array[int].split(":")[1]});
          }
        } catch (e) {
          console.error(e.message);
        }
        return result;
      },
      getData: function () {
        var that = this;
        this.$.ajax({
          url: that.$store.state.global.httpServer + that.item.extra.remoteUrl,
          method: "get",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          success: function (data) { //[{key:'',value:''},{key:'',value:''}]
            if (data.status == "SUCCESS") {
              var result = [];
              try {
                data.data.forEach(function (item) {
                  result.push(item.key + ':' + item.value);
                });
                that.item.options = result.join(";");
              } catch (e) {
                console.log(e.message)
              }
            } else if (data.status == "FAIL") {
              that.$notify.error({
                title: '失败',
                message: data.message
                //message: "组件通过remoteUrl获取数据异常",
              });
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
<style scoped>
  .el-form-item {
    margin-bottom: 5px;
  }
</style>

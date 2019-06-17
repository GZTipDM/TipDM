<!--生成关联关系组件-->
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
      <el-form label-suffix="：" style="padding-left: 10px;">
        <el-form-item label="选择器获取数据URL">
          <el-input size="small" v-model="result.url" placeholder="服务端URL" @change="setItemValue"></el-input>
        </el-form-item>
        <el-form-item label="指定显示label">
          <el-input size="small" v-model="result.props.label" @change="setItemValue"></el-input>
        </el-form-item>
        <el-form-item label="指定值value">
          <el-input size="small" v-model="result.props.value" @change="setItemValue"></el-input>
        </el-form-item>
        <el-form-item label="指定子项children">
          <el-input size="small" v-model="result.props.children" @change="setItemValue"></el-input>
        </el-form-item>
        <el-form-item label="参数key">
          <el-input size="small" v-model="result.props.key" @change="setItemValue"></el-input>
        </el-form-item>
        <el-form-item label="选择器">
          <el-button size="mini" type="primary" @click="getOptions" title="更新级联选择器数据"><i class="fa fa-refresh"></i>
          </el-button>
          <el-cascader
            size="small"
            change-on-select
            :options="result.options"
            v-model="result.selectOptions"
            @change="handleItemChange"
            :props="result.props">
          </el-cascader>
        </el-form-item>
      </el-form>
    </el-form-item>
  </el-form>
</template>

<script>
  export default {
    name: 'TipDatabaseCascader',
    props: ["item", "index", "type"],
    data: function () {
      return {
        paddingStyle: {
          paddingLeft: '5px',
          paddingRight: '5px'
        },
        rules: {
          value: [
            {required: this.item.required, message: '内容不能为空', trigger: 'blur'}
          ]
        },
        result: {
          url: "",
          options: [],
          props: {
            label: "",
            value: "",
            children: "",
            key: ""
          },
          selectOptions: []
        }
      }
    },
    created() {
      try {
        var obj = JSON.parse(this.item.extra[this.item.name + 'Items']);
        this.result.url = obj.url ? obj.url : "";
        this.result.options = obj.options ? obj.options : [];
        this.result.props = obj.props ? obj.props : this.result.props;
        this.result.selectOptions = obj.selectOptions ? obj.selectOptions : [];
      } catch (e) {
        console.log("首次绘制该组件");
      }
    },
    methods: {
      handleItemChange(val) {
        var that = this;
        var currentItem = this.result.options;
        setTimeout(_ => {
          if (val.length == 1) {
            currentItem = currentItem.filter(node => node[that.result.props.value] == val[0]);
          } else {
            for (var i = 0; i < val.length - 1; i++) {
              currentItem = currentItem.filter(node => node[that.result.props.value] == val[i])[0][that.result.props.children];
            }
            currentItem = currentItem.filter(node => node[that.result.props.value] == val[val.length - 1]);
          }
          if (currentItem[0][that.result.props.children]) {
            this.$.ajax({
              url: that.result.url + '?' + that.result.props.key + '=' + currentItem[0][that.result.props.key],
              method: "get",
              type: "json",
              beforeSend: function (xhr) {
                xhr.setRequestHeader("accessToken",localStorage.accessToken);
                currentItem[0][that.result.props.children] = [];
              },
              success: function (data) {
                var result = [];
                data.forEach(function (item) {
                  currentItem[0][that.result.props.children].push(item);
//                                    result.push(item[that.result.props.value]);
                  result.push(item);
                });
                that.item.value = JSON.stringify(result);
              },
              error: function (response) {
                that.$store.commit("dealRequestError", response);
              }
            });
          }
        }, 300);
      },
      getOptions: function () {
        var that = this;
        var url = that.result.url + '?' + that.result.props.key + '=0';
        this.$.ajax({
          url: url,
          method: "get",
          type: "json",
          beforeSend: function (xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
            that.result.options = [];
            that.result.selectOptions = [];
          },
          success: function (data) {
            data.forEach(function (item) {
              that.result.options.push(item);
            });
          },
          error: function (response) {
            that.$store.commit("dealRequestError", response);
          }
        });
      },
      setItemValue() {
        var that = this;
        this.item.extra[this.item.name + 'Items'] = JSON.stringify(that.result);
      }
    }
  }
</script>

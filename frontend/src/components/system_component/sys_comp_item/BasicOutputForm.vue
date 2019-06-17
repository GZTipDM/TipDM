<template>
  <div>
    <div class="mb15">
      <el-button size="small" type="primary" @click="showInputEditDialog"><i class="el-icon-plus"></i> 添加输出</el-button>
      <!-- 添加字段弹窗 -->
      <EditDialog></EditDialog>
    </div>
    <!-- 输入表单 -->
    <el-table :data="outputList" border style="width: 100%">
      <el-table-column prop="cat" label="输出类别" width="240" :formatter="catFormat"></el-table-column>
      <el-table-column prop="key" label="key"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column fixed="right" label="操作" width="140">
        <template slot-scope="scope">
          <el-button size="small" type="text" @click="editRow(scope.$index,scope.row)"><i class="el-icon-edit"></i> 编辑
          </el-button>
          <el-button size="small" type="text" @click="deleteRow(scope.$index, outputList)"><i
            class="el-icon-delete2"></i> 移除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<style>
</style>
<script>
  import EditDialog from './output/EditDialog.vue'

  export default {
    name: 'BasicOutputForm',
    computed: {
      outputList: function () {//输入集合
        return this.$store.state.systemComponent.componentsDeals.editComponent.outputs;
      },
      basicOutputDeals: function () {
        return this.$store.state.systemComponent.componentsDeals.basicOutputDeals;
      }


    },
    components: {
      EditDialog
    },
    methods: {
      catFormat: function (row, column) {
        var cat = row.cat;
        var label = "";
        switch (cat) {
          case "DATA":
            label = "数据";
            break;
          case "MODEL":
            label = "模型";
            break;
          case "UNSTRUCT":
            label = "非结构化文件";
            break;
        }
        return label;
      },
      showInputEditDialog: function () {//弹出编辑框
        //添加
        this.basicOutputDeals.editStatue = "add";
        this.basicOutputDeals.editItem.id = "", //0表示为新增
          this.basicOutputDeals.editItem.type = "OUTPUT",//类型
          this.basicOutputDeals.editItem.key = "",//脚本对应名称
          this.basicOutputDeals.editItem.description = "",//描述
          this.basicOutputDeals.editItem.model = false,//输入模型
          this.basicOutputDeals.editItem.canPreview = false//是否能够预览
        //显示弹出框
        this.basicOutputDeals.showDialog = true;
      },
      deleteRow: function (index, rows) {
        //移除
        rows.splice(index, 1);
      },
      editRow: function (index, row) {
        //设置当前编辑项
        this.basicOutputDeals.editItem = this.deepCopy(row);
        //编辑
        this.basicOutputDeals.editStatue = "edit";
        //显示弹出框
        this.basicOutputDeals.showDialog = true;
      },
      deepCopy: function (source) {//将对象复制到一个新对象并返回
        var result = new Object();
        for (var key in source) {
          result[key] = source[key];
        }
        return result;
      }
    }
  }
</script>

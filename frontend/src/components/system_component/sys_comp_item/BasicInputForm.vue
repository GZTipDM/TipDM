<template>
  <div>
    <div class="mb15">
      <el-button size="small" type="primary" @click="showInputEditDialog"><i class="el-icon-plus"></i> 添加输入</el-button>
      <!-- 添加字段弹窗 -->
      <EditDialog></EditDialog>
    </div>
    <!-- 输入表单 -->
    <el-table :data="inputList" border style="width: 100%">
      <!--<el-table-column prop="id" label="ID" > </el-table-column>-->
      <el-table-column prop="cat" label="输入类别" width="240" :formatter="catFormat"></el-table-column>
      <el-table-column prop="key" label="key"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column fixed="right" label="操作" width="140">
        <template slot-scope="scope">
          <el-button size="small" type="text" @click="editRow(scope.$index,scope.row)"><i class="el-icon-edit"></i> 编辑
          </el-button>
          <el-button size="small" type="text" @click="deleteRow(scope.$index, inputList)"><i
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
  import EditDialog from './input/EditDialog.vue'

  export default {
    name: 'BasicInputForm',
    computed: {
      inputList: function () {//输入集合
        return this.$store.state.systemComponent.componentsDeals.editComponent.inputs;
      },
      basicInputDeals: function () {
        return this.$store.state.systemComponent.componentsDeals.basicInputDeals;
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
        this.basicInputDeals.editStatue = "add";
        this.basicInputDeals.editItem.id = "", //0表示为新增
          this.basicInputDeals.editItem.type = "INPUT",//类型
          this.basicInputDeals.editItem.key = "",//脚本对应名称
          this.basicInputDeals.editItem.description = "",//描述
          this.basicInputDeals.editItem.model = false,//输入模型
          this.basicInputDeals.editItem.canPreview = false//是否能够预览
        //显示弹出框
        this.basicInputDeals.showDialog = true;
      },
      deleteRow: function (index, rows) {
        //移除
        rows.splice(index, 1);
      },
      editRow: function (index, row) {
        //设置当前编辑项
        this.basicInputDeals.editItem = this.deepCopy(row);
        //编辑状态
        this.basicInputDeals.editStatue = "edit";
        //显示弹出框
        this.basicInputDeals.showDialog = true;
      },
      deepCopy: function (source) {
        var result;
        (source instanceof Array) ? (result = []) : (typeof(source) === "object") ? (source === null ? (result = "") : (result = {})) : (result = source);
        for (var key in source) {
          result[key] = (typeof source[key] === 'object') ? this.deepCopy(source[key]) : source[key];
        }
        return result;
      }
    }
  }
</script>

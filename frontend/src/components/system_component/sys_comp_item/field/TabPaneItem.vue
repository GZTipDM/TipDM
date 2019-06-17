<template>
  <div>
    <div class="mb15">
      <el-button size="small" type="primary" @click="showEditFieldDialog"><i class="el-icon-plus"></i> 添加参数</el-button>
    </div>
    <!--字段列表-表格-->
    <el-table :data="item.elements" border style="width: 100%">
      <el-table-column prop="name" label="参数名称"></el-table-column>
      <el-table-column prop="label" label="显示名称"></el-table-column>
      <el-table-column prop="value" label="默认值"></el-table-column>
      <el-table-column prop="toolTip" label="提示信息"></el-table-column>
      <el-table-column fixed="right" header-align="center" label="操作" width="140">
        <template slot-scope="scope">
          <el-button size="small" type="text" @click="handleEdit(scope.$index, scope.row)"><i class="el-icon-edit"></i>
            编辑
          </el-button>
          <el-button size="small" type="text" @click="handleDelete(scope.$index, item.elements)"><i
            class="el-icon-delete2"></i> 删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<style>
</style>
<script>
  export default {
    name: 'TabPaneItem',
    props: ["item", "index"],
    computed: {
      basicFieldParamDeals: function () {//数据交互集
        return this.$store.state.systemComponent.componentsDeals.basicFieldParamDeals;
      }
    },
    methods: {
      showEditFieldDialog: function () {//添加字段
        this.basicFieldParamDeals.editStatue = "add";//设置状态为添加
        this.basicFieldParamDeals.showEditDialog = true;//弹出编辑框
      },
      handleEdit(index, row) {//编辑
        this.basicFieldParamDeals.editStatue = "edit";//设置状态为编辑
        this.basicFieldParamDeals.editItem = this.deepCopy(row);//设置当前对象
        this.basicFieldParamDeals.showEditDialog = true;//弹出编辑框
      },
      handleDelete(index, rows) {//删除
        rows.splice(index, 1);
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

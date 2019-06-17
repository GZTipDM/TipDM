<template>
  <div>
    <el-dialog title="数据源分享" :visible.sync="dealStatue.share">
      <div style="height:250px;overflow:auto">
        <el-input placeholder="输入关键字进行过滤" v-model="filterText">
        </el-input>
        <el-tree class="filter-tree"
                 show-checkbox
                 node-key="key"
                 :data="sharedUser"
                 :props="defaultProps"
                 default-expand-all
                 :filter-node-method="filterNode"
                 ref="tree">
                <span class="custom-tree-node" slot-scope="{ node, data }">
                    <span>
                      <i v-if="data.explain=='true'" class="fa fa-user-o"></i>
                      <i v-else-if="data.explain=='false'" class="fa fa-users"></i>
                      {{data.name}}
                    </span>
                </span>
        </el-tree>
      </div>
      <span slot="footer" class="dialog-footer">
          <el-button @click="resetChecks">清 空</el-button>
          <el-button type="primary" @click="handleSure">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<style>
</style>
<script>
  export default {
    name: 'ItemShare',
    data: function () {
      return {
        filterText: '',//过滤
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      }
    },
    created() {
      console.log(this.sharedUser);
    },
    computed: {
      deals: function () {
        return this.$store.state.datasource.deals;
      },
      dealStatue: function () {
        return this.deals.statue;
      },
      share: function () {
        return this.dealStatue.share;
      },
      sharedUser: function () {
        return this.$store.state.global.sharedUser;
      }
    },
    watch: {
      filterText(val) {
        this.$refs.tree.filter(val);
      },
      share(value) {//当窗体关闭时，清空选中
        if (!value) {
          this.resetChecks();
        }
      }
    },
    methods: {
      cancel: function () {
        this.dealStatue.share = false;
      },
      filterNode(value, data) {
        if (!value) return true;
        return data.name.indexOf(value) !== -1;
      },
      resetChecks() {
        this.$refs.tree.setCheckedKeys([]);
      },
      handleSure() {
        //已选择节点集合
        var selectIds = this.$refs.tree.getCheckedNodes().filter(node => node.explain == "true");
        var userStr = [];
        selectIds.forEach((item) => {
          userStr.push({
            userId: item.id,
            userName: item.name
          });
        });
        if (selectIds.length == 0) {
          this.$notify({
            title: "提示",
            message: "请选择要分享的用户！",
            type: "warning"
          });
          return;
        }
        var that = this;
        this.$.ajax({
          url: that.$store.state.global.httpServer + "/api/datasource/" + that.deals.dealItem.id + "/share",
          method: "post",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          data: JSON.stringify(userStr),
          beforeSend: function(xhr) {
            xhr.setRequestHeader("accessToken",localStorage.accessToken);
          },
          success: function (data) {
            if (data.status == "SUCCESS") {
              that.$notify({
                title: "提示",
                message: "分享成功！",
                type: "success"
              });
              that.cancel();
            } else if (data.status == "FAIL") {
              that.$notify.error({
                title: "提示",
                message: data.message
              });
            }
          }
        });
      }
    }
  }
</script>

<style>
</style>

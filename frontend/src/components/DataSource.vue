<!--数据源页-->
<template>
  <span>
    <div id="mCom" class="mCom">
      <el-tabs @tab-click="handleClick" value="first" type="card" v-loading="showLoading"
               element-loading-text="拼命加载中...">
        <el-tab-pane label="我的数据源" name="first">
          <span>
            <div class="topForm">
              <div id="upBtn">
                <AddDatabase></AddDatabase>
                <AddFile></AddFile>
                <el-menu mode="horizontal" @select="handleSelect" class="el-menu-vertical-demo">
                  <el-submenu index="1">
                    <template slot="title"><i class="el-icon-plus"></i>新增数据源</template>
                    <el-menu-item index="csv"><i
                      class="fa fa-database"></i>&nbsp;&nbsp;CSV文件
                    </el-menu-item>
                    <el-menu-item index="sql"><i
                      class="fa fa-database"></i>&nbsp;&nbsp;SQL数据库
                    </el-menu-item>
                  </el-submenu>
                </el-menu>
              </div>
              <div class="fr">
                <SearchCondition></SearchCondition>
              </div>
              <div class="clearfix"></div>
            </div>
            <div id="t" class="hfix">
              <DataSourceList></DataSourceList>
            </div>
          </span>
        </el-tab-pane>
        <el-tab-pane label="共享数据源" name="second">
          <span>
            <div class="topForm">
              <div class="fr">
                <SharedSearchCondition></SharedSearchCondition>
              </div>
              <div class="clearfix"></div>
            </div>
            <div id="t2" class="hfix">
              <SharedDataSourceList></SharedDataSourceList>
            </div>
          </span>
        </el-tab-pane>
      </el-tabs>
      <!--删除dialog-->
      <ItemDelete/>
      <!--预览dialog-->
      <ItemDataPreview/>
      <!--分享dialog-->
      <ItemShare/>
      <!--预览链接信息-->
      <ItemPreviewConnection/>
    </div>
  </span>
</template>
<style>
  .el-menu--horizontal > .el-submenu:hover .el-submenu__title {
    border-bottom-color: #eef1f6;
  }
</style>
<script>
  import DataSourceList from './data_source/DataSourceList.vue';
  import SharedDataSourceList from './data_source/SharedSourceList.vue';
  import SearchCondition from './data_source/SearchCondition.vue';
  import SharedSearchCondition from './data_source/SharedSearchCondition.vue';
  import ItemDelete from './data_source/item_dialog/ItemDelete.vue';
  import ItemDataPreview from './data_source/item_dialog/ItemDataPreview.vue';
  import ItemPreviewConnection from './data_source/item_dialog/ItemPreviewConnection.vue';
  import ItemShare from './data_source/item_dialog/ItemShare.vue';
  import AddDatabase from './data_source/source_dialog/AddDatabase.vue'
  import AddFile from './data_source/source_dialog/AddFile.vue'

  export default {
    name: "DataSource",
    beforeMount: function () {//挂载前调用
      this.$store.dispatch("checkToken");
      this.showLoading = false;
    },
    data: function () {
      return {
        showLoading: true,
      }
    },
    computed: {
      statue: function () {
        return this.$store.state.datasource.deals.statue;
      },
      permission: function () {
        return this.$store.state.global.permission;
      },
      fileUpload: function () {
        return this.$store.state.datasource.fileUpload;
      }
    },
    created: function () {
      var routerPath = this.$router.currentRoute.path;
      var curMenu = this.$store.state.global.navMenu.filter(node => node.linkTo == routerPath);
      this.$store.state.global.curMenuItem = curMenu[0];
    },
    components: {
      DataSourceList,
      SharedDataSourceList,
      SearchCondition,
      SharedSearchCondition,
      ItemDelete,
      ItemDataPreview,
      ItemPreviewConnection,
      ItemShare,
      AddDatabase,
      AddFile,
    },
    methods: {
      handleClick(tab, event) {
        if (tab.name == 'second') {
          if (this.permission.data.indexOf("datasource:shared") > -1) {
            this.$store.dispatch("getSharedMenuDatabaseList");
          }
        }
      },
      handleSelect(key, keyPath) {
        if (!this.checkPermission("datasource:createTable")) {
          return;
        }
        switch (key) {
          case "unstructured":
            this.statue.unstructuredUpload = true;
            break;
          case "csv":
            this.fileUpload.deals.created = false;
            this.fileUpload.deals.uploadId = "";
            this.statue.fileUpload = true;
            break;
          case "sql":
            this.statue.dbUpload = true;
            break;
        }
      },
      checkPermission: function (permissionStr) {
        var result = true;
        if (this.permission.data.indexOf(permissionStr) == -1) {
          result = false;
          this.$notify({
            type: "warning",
            message: this.permission.warningMsg.noPermission
          });
        }
        return result;
      }
    }
  }
</script>

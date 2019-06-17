import $ from 'jquery';

const state = {
  databaseList: [],//数据源列表集合
  sharedDatabaseList: [],//分享数据源列表集合
  pagination: {
    currentPage: 1,//当前页
    perSize: 10,//每页条数
    totalSize: 0 //数据总条数
  },//分页信息
  sharePagination: {
    currentPage: 1,//当前页
    perSize: 10,//每页条数
    totalSize: 0 //数据总条数
  },//分享数据源分页信息
  searchCondition: {
    tableName: '',//表名称
    statue: '',//同步状态
    datetime: ''//时间段
  },//搜索条件信息集合
  sharedSearchCondition: {
    tableName: '',//表名称
    statue: '',//同步状态
    datetime: ''//时间段
  },//分享列表搜索条件信息集合
  deals: {
    statue: {
      fileUpload: false,//从文件上传
      dbUpload: false,//从数据源上传
      hdfsUpload: false,//从HDFS上传
      hbaseUpload: false,//从HBASE上传
      elasticsearchUpload: false,//从ElasticSearch上传
      unstructuredUpload: false,//从非结构化数据上传
      hiveUpload: false,//从hive上传
      csvToHive: false,//从csv上传到hive
      preview: false,//预览
      edit: false,//编辑
      delete: false,//删除
      share: false,//分享
      synSetting: false,//数据同步设置
      previewConnection: false //预览数据源连接
    },//dialog展示状态
    dealItem: {}, //操作的行数据
    shareData: [], //分享的组织列表
    previewConnDetail: []//预览数据链接信息
  },//列表行操作
  elasticSearchUpload: {
    deals: {
      hiveTable: "",//新建目标表
      esNodeIp: "",//节点ip
      esNodePort: "",//节点端口
      esTableMulti: false,// Elasticsearch原表,默认单表
      duration: 60,//数据源存储有效期
      previewMode: 'ONLY100',//预览设置ONLY100/PAGEABLE
      tsColName: "",//时间列字段
      tsColPattern: "yyyyMMdd HH:mm:ss",//时间列格式
      timeRangeValue: 1,//时间范围 输入数值
      timeRangeType: "D",//时间范围 ，下拉框,默认“日”
      selectedCols: "",//选择的列
      esIndexPre: "",//index前缀
      esIndexType: "yyyyMM",//index类型,下拉框，
      esType: "",// type名称
      esTable: "",//Elasticsearch表名
      esResourceLimit: ""//记录条数
    }//数据交互处理
  },//从ElasticSearch上传
  unstructuredUpload: {
    deals: {
      table: "",//目标表名
      path: "",//文件路径
      dirPath: "/",//搜索目录
      duration: 60,//数据源存储有效期
      previewMode: 'ONLY100',//预览设置ONLY100/PAGEABLE
    }//数据交互处理
  },//从ElasticSearch上传
  hdfsUpload: {
    deals: {
      tableName: "",//表名
      hdfsPath: "/",//hdfs路径
      colSplit: ",",//分隔符
      duration: 60,//数据源存储有效期
      previewMode: 'ONLY100',//预览设置ONLY100/PAGEABLE
    },//数据交互处理
    dataColumnName: []//表结构信息
  },//从HDFS上传
  hbaseUpload: {
    deals: {
      tableName: "",//表名
      hTble: "",//HBase表名
      hTbleColumns: "rk,fm1:col1,fm1:col2,fm2:col1",//HBase表列描述
      hMaster: "master:60000",//HBase主节点
      rootDir: "/hbase",//HBase根目录
      zookeepers: "slave1,slave2,slave3",//Zookeeper集群
      port: "2181",//Zookeeper端口
      splitter: ",",//分隔符
      duration: 60,//数据源存储有效期
      previewMode: 'ONLY100',//预览设置ONLY100/PAGEABLE
    },//数据交互处理
    dataColumnName: []//表结构信息
  },//从HBASE上传
  fileUpload: {
    deals: {
      tableName: "",//表名
      delimiter: "comma",//列分隔符
      rowSplit: "enter", //列分隔符
      encoding: "UTF-8",//文件编码
      ifFirstRowAsColumnName: "true", //是否第一行数据作为列名
      uploadId: "",//表创建后，上传文件识别id
      created: false, //数据源是否已经被创建
      duration: 60,//数据源存储有效期
      previewMode: 'ONLY100',//预览设置ONLY100/PAGEABLE
      dataType: 'true'//是否开启首行数据类型识别
    },
    fileSize: 10,//上传文件大小
    fileMD5: null,//文件MD5校验
    fileItems: [],//选择上传的文件
    uploadFile: "",//选择上传的文件
    dataColumnName: [],//格式化后的列头
    dataListTitle: [],//预览数据表头
    dataList: []//预览数据
  },//从文件上传
  databaseUpload: {
    dbMsg: {
      dataType: "jdbc:mysql://<machine_name>:<port>/<dbname>",
      url: "jdbc:mysql://<machine_name>:<port>/<dbname>",//数据库连接
      tableName: "", //表名
      username: "", //用户名
      password: "", //密码
      sql: "", //查询sql
      duration: 60,//数据源存储有效期
      previewMode: 'ONLY100',//预览设置ONLY100/PAGEABLE
    },//链接信息
    dataColumnName: [],//格式化后的列头
    dataListTitle: [],//预览数据表头
    dataList: []//预览数据
  },//从数据源上传
  hiveUpload: {
    dbMsg: {
      dataType: "jdbc:hive2://<machine_name>:<port>",
      url:"jdbc:hive2://<machine_name>:<port>",//数据库连接
      tableName: "", //表名
      username: "", //用户名
      password: "", //密码
      sql: "", //查询sql
      duration: 60,//数据源存储有效期
      previewMode: 'ONLY100',//预览设置ONLY100/PAGEABLE
    },//链接信息
    dataColumnName:[],//格式化后的列头
    dataListTitle: [],//预览数据表头
    dataList:[]//预览数据
  },
  csvToHive: {
    deals: {
      tableName: "",//表名
      delimiter: ",",//列分隔符
      rowSplit: "enter", //行分隔符
      encoding: "UTF-8",//文件编码
      ifFirstRowAsColumnName: "true", //是否第一行数据作为列名
      includeHeader: "true", //是否包含表头
      uploadId: "",//表创建后，上传文件识别id
      created: false, //数据源是否已经被创建
      duration: 60,//数据源存储有效期
      previewMode: 'ONLY100',//预览设置ONLY100/PAGEABLE
    },
    fileSize: 10,//上传文件大小
    fileMD5: null,//文件MD5校验
    fileItems: [],//选择上传的文件
    uploadFile: "",//选择上传的文件
    dataColumnName:[],//格式化后的列头
    dataListTitle: [],//预览数据表头
    dataList:[]//预览数据
  }
}

// getters
const getters = {}

// actions
const actions = {
  getMenuDatabaseList: function ({state, commit, rootState}) {
    var showName = state.searchCondition.tableName == "NaN" ? "" : state.searchCondition.tableName;
    var statue = state.searchCondition.statue;
    var beginTime = state.searchCondition.datetime[0] == "NaN" ? "" : state.searchCondition.datetime[0];
    var endTime = state.searchCondition.datetime[1] == "NaN" ? "" : state.searchCondition.datetime[1];
    $.ajax({
      url: rootState.global.httpServer + "/api/datasource/search",
      method: "get",
      dataType: "json",
      contentType: "application/json; charset=utf-8",
      data: {
        accessToken: localStorage.accessToken,
        showName: showName,
        status: statue,
        beginTime: beginTime,
        endTime: endTime,
        pageNumber: state.pagination.currentPage,
        pageSize: state.pagination.perSize

      },
      beforeSend: function(xhr) {
        xhr.setRequestHeader("accessToken",localStorage.accessToken);
      },
      success: function (response) {
        if (response.status == "SUCCESS") {
          state.pagination.totalSize = response.data.totalElements;
          state.databaseList = response.data.content;
        } else if (response.status == 401) {
          commit("dealRequestError", {
            status: 401,
            responseJSON: {
              message: response.message
            }
          });
        }
      },
      error: function (response) {
        commit("dealRequestError", response);
      }
    });
  },
  getSharedMenuDatabaseList({state, commit, rootState}) {
    var showName = state.sharedSearchCondition.tableName == "NaN" ? "" : state.sharedSearchCondition.tableName;
    var statue = state.sharedSearchCondition.statue;
    var beginTime = state.sharedSearchCondition.datetime[0] == "NaN" ? "" : state.sharedSearchCondition.datetime[0];
    var endTime = state.sharedSearchCondition.datetime[1] == "NaN" ? "" : state.sharedSearchCondition.datetime[1];

    $.ajax({
      url: rootState.global.httpServer + "/api/datasource/shared",
      method: "get",
      dataType: "json",
      contentType: "application/json; charset=utf-8",
      data: {
        showName: showName,
        status: statue,
        beginTime: beginTime,
        endTime: endTime,
        pageNumber: state.sharePagination.currentPage,
        pageSize: state.sharePagination.perSize
      },
      beforeSend: function(xhr) {
        xhr.setRequestHeader("accessToken",localStorage.accessToken);
      },
      success: function (response) {
        if (response.status == "SUCCESS") {
          state.sharePagination.totalSize = response.data.totalElements;
          state.sharedDatabaseList = response.data.content;
        } else if (response.status == 401) {
          commit("dealRequestError", {
            status: 401,
            responseJSON: {
              message: response.message
            }
          });
        }
      },
      error: function (response) {
        commit("dealRequestError", response);
      }
    });
  }
}

// mutations
const mutations = {}

export default {
  state,
  getters,
  actions,
  mutations
}

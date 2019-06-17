import $ from 'jquery';
import io from 'socket.io-client';

/**
 * author jiaozhengsheng@163.com
 * @type {{createNew: (function(*, *))}}
 */
var SocketioClient = {
  createNew: function (host, port) {
    var client = {};
    client.host = host;
    client.port = port;
    client.socket = io(`http://${host}:${port}?accessToken=${localStorage.accessToken}`/*, {'force new connection': true, transports: ['websocket'] }*/);
    client.socket.on('connect', () => {
      console.log(client.socket.id); // 'G5p5...'
    });
    client.socket.on('connect', () => {
      console.log("连接成功");
    });
    client.socket.on('connect_error', (error) => {
      console.error("连接错误" + error);
    });
    client.socket.on('connect_timeout', (timeout) => {
      console.log("连接超时");
    });
    client.socket.on('error', (error) => {//错误发生无法被其它事件类型处理
      // ...
    });
    client.socket.on('disconnect', (reason) => {
      console.log("关闭连接");
    });
    client.socket.on('reconnect', (attemptNumber) => {
      console.log("重新连接");
    });
    client.socket.on('reconnect_error', (error) => {
      console.log("连接错误")
      // ...
    });
    client.socket.on('reconnect_failed', () => {
      console.log("连接失败")
      // ...
    });
    client.socket.on('ping', () => {
      // console.log("向服务器端发送数据包");
    });
    client.socket.on('pong', (latency) => {
      // console.log("接收服务器数据包");
    });
    // 向服务端发送消息
    client.send = function (event, msg, callback) {
      if (callback) {
        client.socket.emit(event, msg, (data) => callback);
      } else {
        client.socket.emit(event, msg);
      }
    }
    // 接收服务端消息
    client.receive = function (event, callback) {
      client.socket.on(event, callback);
    }
    //移除事件监听
    client.removeListener = function (event, callback) {
      if (callback) {
        client.socket.off(event, callback);
        console.log("callback事件：" + event + "关闭监听");
      } else {
        client.socket.off(event);
        console.log("event事件：" + event + "关闭监听");
      }
    }
    return client;
  }
}
var fullSocket = null;//全局socket
const state = {
  exceptionDeal: {
    overtime: false, //是否超时，默认否401
    accessRights: true, //是否有访问权限403
    notFound: false,//接口是否找不到404
    otherException: false,//其它异常405
    exceptionMessage: "",//后台返回异常信息
    checkExcuteStatue: ""//检测提交流程等待状态
  },//异常信息处理
  httpServer: "",  //服务端ip
  httpOauth: "",  //授权服务ip
  httpClient: "",  //客户端ip
  socketServer: "",//socketIP地址
  socketPort: "",//socket端口
  title: "",//标题
  username: null,//当前的登录用户名
  mode: "online",//反馈问题模式：在线（online）/离线（offline）
  permission: {
    data: [],//权限列表
    warningMsg: {
      noPermission: "无此权限"
    }//提示信息
  },//token权限信息
  sharedUser: [],//可分享的用户列表集合
  navMenu: [
    {
      id: 1,//菜单绑定id，暂无用
      name: "首页",//菜单绑定内容
      class: "fa-home",//菜单绑定类
      linkTo: "/home/main",//菜单绑定路由
      selected: true,//菜单绑定-是否选中
      permission: "main:menu"//权限字符串
    },
    {
      id: 2,
      name: "数据源",
      class: "fa-database",
      linkTo: "/home/datasource",
      selected: false,
      permission: "datasource:menu"//权限字符串
    },
    {
      id: 3,
      name: "工程",
      class: "fa-flask",
      linkTo: "/home/project",
      selected: false,
      permission: "project:menu"//权限字符串
    },
    {
      id: 4,
      name: "系统组件",
      class: "fa-cubes",
      linkTo: "/home/components",
      selected: false,
      permission: "components:menu"//权限字符串
    }
  ],//导航主菜单栏
  curMenuItem: {},//当前选中菜单项
  headerDeal: {
    newMessage: false,//是否有最新消息
    gridData: [],//消息集合
    showNotice: false,//反馈弹框
    feedbackDialog: false,//反馈弹框
    feedbackOnlineDialog: false//反馈弹框
  },//主框架头部信息集合
  regexp: {
    fieldName: "^[a-z][a-z0-9_]{0,31}$",//小写英文字母开头，字母、数字、下划线组成,长度1-32
    fieldNameCheckMsg: "小写英文字母开头，小写字母、数字、下划线组成,长度1-32",
    tableName: "^[a-z][a-z0-9_]{0,24}$",//表名小写英文字母开头，由字母、数字、下划线组成，长度1-32
    tableNameCheckMsg: "小写英文字母开头，小写字母、数字、下划线组成,长度1-25",
    //tableName: "^[a-zA-Z][a-zA-Z0-9_]{0,31}$",//英文字母开头，字母、数字、下划线组成，长度1-32
  },//正则表达式校验集
  otherState: {
    previewProjectFlow: false,//是否是查看流程状态
    uuid: ""
  }
}

const getters = {
  httpServer: state => state.httpServer,
  httpClient: state => state.httpClient,
  httpOauth: state => state.httpOauth,
  permission: state => state.permission,
  username: state => state.username,
  headerDeal: state => state.headerDeal,
  title: state => state.title,
  mode: state => state.mode
}

const actions = {
  checkToken({state, commit}) {
    $.ajax({
      url: state.httpServer + "/token/check",
      method: "get",
      dataType: "json",
      contentType: "application/json; charset=utf-8",
      beforeSend: function(xhr) {
        xhr.setRequestHeader("accessToken",localStorage.accessToken);
      },
      success: function (data) {
        if (data.status == "FAIL") {
          commit("dealRequestError", {
            status: 401,
            responseJSON: {
              message: data.message
            }
          });
        }
      },
      error: function (response, textStatus, errorThrown) {
        // if (textStatus == 'timeout') {
        if (response.status == 0) {
          commit("dealRequestError", {
            status: 401,
            responseJSON: {
              message: "服务器连接超时，请联系管理员！"
            }
          });
        } else {
          commit("dealRequestError", response);
        }
      }
    });
  },
  openWebsocket({dispatch, state}) {
    fullSocket = SocketioClient.createNew(state.socketServer, state.socketPort);
    dispatch("receiveWSMessage");
  },//打开全局socket链接
  receiveFlowMessage({state, rootState}) {
    //接收流程运行消息并处理
    fullSocket.receive(rootState.project.flowData.workFlowId, function (data) {
      var resMsg = JSON.parse(data);
      console.log(data);
      switch (resMsg.category) {
        case "WORKFLOW":
          switch (resMsg.type) {
            case "NODE":
              var nodeItem = rootState.project.flowData.nodes.filter(node => node.id == resMsg.nodeId)[0];
              var targetLinks = rootState.project.flowData.links.filter(node => node.target == resMsg.nodeId);
              switch (resMsg.state) {
                case "INIT":
                case "RUNNING":
                  nodeItem.runStyle = "status-working";
                  break;
                case "COMPLETE":
                  nodeItem.runStyle = "status-sus";
                  //当前节点运行完成时，设置所有指向该节点的线条颜色为原状态
                  targetLinks.forEach(function (item) {
                    item.runStyle = "";
                  });
                  break;
                case "ERROR":
                  //运行失败时，设置当前节点运行状态为失败
                  nodeItem.runStyle = "status-erro";
                  //当前节点运行完成时，设置所有指向该节点的线条颜色为原状态
                  targetLinks.forEach(function (item) {
                    item.runStyle = "";
                  });
                  break;
              }
              break;
            case "LINK":
              if (rootState.project.flowData.isRunning) {
                var linkItem = rootState.project.flowData.links.filter(link => link.id == resMsg.nodeId)[0];
                linkItem.runStyle = "pane-link-flow";
              }
              break;
            case "WORKFLOW":
              switch (resMsg.state) {
                case "COMPLETE":
                  //当流程运行结束时，修改运行状态
                  rootState.project.flowData.isRunning = false;
                  rootState.project.flowData.nodes.forEach(function (node) {
                    if (node.runStyle == "status-working") {
                      node.runStyle = "status-disW";
                    }
                  });
                  rootState.project.flowData.links.forEach(function (link) {
                    link.runStyle = "";
                  });
                  break;
              }
              break;
          }
          break;
        case "MODEL":
          var parentItem = rootState.model.modelData.unsavedModelTree[0].children;
          var index = parentItem.findIndex(node => node.nodeId == resMsg.nodeId);
          if (index != -1) {
            parentItem.splice(index, 1);//去重
          }
          parentItem.push({
            catId: [0],//保存目录
            modelName: resMsg.modelName,//模型名称
            label: resMsg.modelName,
            nodeId: resMsg.nodeId,//节点id
            workFlowId: resMsg.workFlowId,//流程id
            isModel: true,//是否是模型
            type: 'created',//创建
            leaf: true
          });
          break;
      }
      // 在此处更新页面显示（提示效果）
    });
  },//ws所有返回消息在该方法中接收处理
  receiveWSMessage({state, rootState}) {
    //接收数据源同步按钮点击后的返回同步状态信息
    fullSocket.receive('dataSyncResult', function (data) {
      var response = JSON.parse(data);
      state.headerDeal.gridData.unshift({
        content: response.message
      });
      state.headerDeal.newMessage = true;
      //修改对应的数据源的同步状态
      var filterItem = rootState.datasource.databaseList.filter(node => node.showName == response.data);
      if (filterItem.length > 0) {
        if (response.status == 'SUCCESS') {
          filterItem[0].status = "FINISH";
        } else if (response.status == 'FAIL') {
          filterItem[0].status = "FAILED";
        }
      }
    });
  },//ws所有返回消息在该方法中接收处理
  startApplication({state, rootState, commit, dispatch}, payload) {
    $.ajax({
      url: state.httpServer + "/api/project/execute/" + payload.projectId + "/apply",
      method: "post",
      dataType: "json",
      beforeSend: function(xhr) {
        xhr.setRequestHeader("accessToken",localStorage.accessToken);
      },
      success: function (response) {
        if (response.status == "SUCCESS") {
          rootState.project.flowData.isRunning = true;
          var workFlowId = response.data;
          rootState.project.flowData.workFlowId = workFlowId;
          dispatch("builderSocketConnection", workFlowId);
          payload["executionId"] = workFlowId;
          dispatch("runFlow", payload);
        } else if (response.status == "FAIL") {
          //出现错误，则修改状态为运行结束
          rootState.project.flowData.isRunning = false;
          commit("dealRequestError", {
            status: 405,
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
  sendLocalStorageToServer({state}, payload) {
    var projectId = payload.projectId;
    var localJSON = localStorage.getItem(state.username + "_" + projectId);
    fullSocket.send('workflowSync', localJSON);
    localStorage.removeItem(state.username + "_" + projectId);
  },//更新本地缓存流程json到服务端
  builderSocketConnection({state,dispatch}, workFlowId) {
    try {
      dispatch("receiveFlowMessage")//先注册监听
      fullSocket.send("started", workFlowId);//再发送事件
    } catch (e) {
      console.error(e.message)
    }
  },
}

const mutations = {
  dealRequestError(state, payload) {
    switch (payload.status) {
      case 401://超时
        state.exceptionDeal.overtime = true;
        break;
      case 403://无权限
        state.exceptionDeal.accessRights = false;
        break;
      case 404://接口找不到
        state.exceptionDeal.notFound = true;
        break;
      case 405://其它异常
        state.exceptionDeal.otherException = true;
        break;
      case 500://500
        state.exceptionDeal.otherException = true;
      case 400://500
        state.exceptionDeal.otherException = true;
        break;
    }
    if(payload.message) {
      state.exceptionDeal.exceptionMessage = payload.message;
    } else if (payload.responseJSON) {
      state.exceptionDeal.exceptionMessage = payload.responseJSON.message;
    } else if (payload.responseText) {
      state.exceptionDeal.exceptionMessage = JSON.parse(payload.responseText).message;
    } else {
      state.exceptionDeal.exceptionMessage = "异常！";
    }
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}

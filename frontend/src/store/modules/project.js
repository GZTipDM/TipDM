import $ from 'jquery';
// initial state
/**
 * author jiaozhengsheng@163.com
 * @type {{projectMenuModel: {targetClassItem: {}, showDetail: {exportProjectsDialog: boolean, createHistoryVersion: boolean, updateProjectDetail: boolean, showProjectDialog: boolean, saveAsNewProject: boolean, deleteTargetVersion: boolean, importProjectsDialog: boolean, backToTargetVersion: boolean, saveAsTemplateDialog: boolean}}, personalComponents: {component: boolean, children: Array, id: number, label: string, enabled: boolean}[], menuDeals: {topContentHeight: number, footContentHeight: number}, flowData: {pointLayer: {x: number, y: number}, currentParentItem: string, selectComponent: string, currentXY: {top: number, left: number}, projectChange: boolean, rightContentTargetItem: {}, source: {sourceNodeId: string, sourcePortId: string, cat: string, key: string}, propsContentStyle: {width: number}, projectContextMenuMsg: {top: number, left: number, display: string}, contentStyle: {curHeight: number, isMini: boolean, curWidth: number, width: number, height: number}, nodeChange: boolean, isRunning: boolean, nodeRightContentMsg: {top: number, left: number, display: string}, links: Array, currentProjectDetail: {}, currentPath: {curPathId: string, curPathIndex: string}, targetParentItem: string, isConnecting: boolean, positionChange: boolean, ifAddDom: boolean, target: {model: boolean, targetNodeId: string, targetPortId: string}, currentProjectItem: {}, workFlowId: number, nodes: Array, currentNodeItem: {}, currentNodeId: {curNodeId: string, curNodeIndex: string}, addMenuItem: string, rightContentTargetProject: {}}, viewEditArea: {previewImageDialog: boolean, showEChartsDialog: boolean, previewResultData: string, previewCreateUrl: boolean, previewLogsDialog: boolean, previewResultDialog: boolean, previewRealLogDialog: boolean, previewLogsData: string, previewCodeDatas: string, previewRealLogData: string, previewImageUrl: string, createUrl: string, previewCodeData: string, previewTableName: string, previewCodeDialog: boolean, previewDialog: boolean}, systemComponents: {component: boolean, children: Array, id: number, label: string, enabled: boolean}[], modelMenu: {contentMenuMessage: {top: number, left: number, display: string}, currentModelItem: string}, projectMenu: {creatorId: number, creatorName: string, leaf: boolean, loading: boolean, delete: boolean, parentId: number, path: string, sequence: number, createTime: number, children: Array, name: string, id: number, open: boolean}[]}}
 */
const state = {
  menuDeals: {
    topContentHeight: 60,//工程菜单高度
    footContentHeight: 60//底部最小高度
  },//菜单交互
  projectMenu: [
    {
      id: 0,
      createTime: 0,
      creatorId: 0,
      creatorName: "admin",
      name: "我的工程",
      parentId: 0,
      path: "",
      sequence: 0,
      leaf: false,
      delete: false,
      open: false,
      loading: false,
      children: []
    }
  ],//工程菜单
  systemComponents: [
    {
      id: 0,
      label: "系统组件",
      enabled: true,
      component: false,
      children: []
    }
  ],//系统组件菜单
  personalComponents: [
    {
      id: 0,
      label: "个人组件",
      enabled: true,
      component: false,
      children: []
    }
  ],//个人组件
  projectMenuModel: {
    showDetail: {//弹出框信息
      showProjectDialog: false,//新建工程弹出框
      saveAsTemplateDialog: false,//另存为模板弹出框
      saveAsNewProject: false,//另存为新的工程
      createHistoryVersion: false, //创建历史版本
      backToTargetVersion: false, //恢复到指定版本
      deleteTargetVersion: false, //删除指定版本
      updateProjectDetail: false, //更新工程描述信息
      importProjectsDialog: false,//导入工程
      exportProjectsDialog: false//导出工程
    },
    targetClassItem: {}//创建工程存放目录
  },//工程菜单模块交互信息
  modelMenu: {
    contentMenuMessage: {
      left: 300,
      top: 200,
      display: 'none'
    },//模型目录右键菜单属性
    currentModelItem: ""//当前操作的模型项
  },//模型菜单交互信息
  viewEditArea: {
    previewDialog: false, //数据预览展示框
    previewTableName: "",//数据预览表名
    previewLogsDialog: false, //查看日志展示框
    previewLogsData: "",//日志信息
    previewResultDialog: false, //查看结果展示框
    previewResultData: "",//结果信息
    previewImageDialog: false, //可视化
    previewImageUrl: "", //可视化url
    previewCodeDialog: false, //源码预览
    previewCodeData: "", //源码内容
    previewCodeDatas: "", //dbutil.R
    previewCreateUrl: false, //查看URL
    createUrl: "", //查看成功的Url
    showEChartsDialog: false, //图片组件双击弹出框
    previewRealLogDialog: false, //查看节点实时日志
    previewRealLogData: "" //查看节点的实时日志数据
  },//编辑区交互信息
  flowData: {//流程数据对象
    contentStyle: {
      width: 100,
      height: 100,
      isMini: false, //是否最小
      curHeight: 0, //高度
      curWidth: 0 //宽度
    },//编辑区容器属性
    propsContentStyle: {
      width: 210//默认宽度
    },//流程右侧属性宽度
    projectContextMenuMsg: {
      left: 300,
      top: 200,
      display: 'none'
    },//工程树目录右键菜单属性
    currentProjectItem: {},//当前要操作的工程树菜单项 操作的当前文档树
    currentParentItem: "",//操作当前文档树的父类项
    targetParentItem: "",//目标文档树父类项
    rightContentTargetItem: {},//右键指向文档树项
    rightContentTargetProject: {},//右键指向工程
    nodeRightContentMsg: {
      left: 300,
      top: 200,
      display: "none"
    },//流程中节点右键菜单信息
    isRunning: false,//当前流程是否在运行
    workFlowId: 0,//工程流程Id
    currentProjectDetail: {},//当前操作工程的基本信息
    projectChange: false,//工程是否切换
    nodes: [],//节点源集合
    links: [],//连线源集合
    currentXY: {
      left: 0,
      top: 0
    },//当前选中节点左上角坐标
    currentNodeItem: {},//当前点击节点项
    currentNodeId: {//当前节点
      curNodeId: "",
      curNodeIndex: ""
    },//当前选中节点信息
    currentPath: {//当前选中连线
      curPathId: "",
      curPathIndex: ""
    },//当前选中连线信息
    ifAddDom: false, //是否为添加节点
    addMenuItem: "",//从菜单栏添加的菜单项源
    isConnecting: false,//线条指向的点是否可以被连接
    pointLayer: {
      x: 0,
      y: 0
    },//鼠标相对于节点偏移位置信息
    target: {
      targetNodeId: "",
      targetPortId: "",
      model: false//是否可接入模型
    },//箭头指向的节点id及连接点id
    source: {
      sourceNodeId: "",
      sourcePortId: "",
      key: "",//通过key判断输出是否为模型
      cat: ""//类型DATA\MODEL\UNSTRACT
    },//箭头按下的节点啊id及连接点id
    selectComponent: "",//根据设置值用来切换右侧视图 目前值：project_props、project_content_msg
    nodeChange: true, //必须，用来切换销毁重绘右侧组件
    positionChange: false //必须，进行位移校验，判断是否修改坐标
  }
}

// getters
const getters = {}

// actions
const actions = {
  //获取系统组件
  getSysComponentData({state, rootState, commit, dispatch},currentParentItem) {
    return new Promise((resolve, reject) => {
      if(!currentParentItem) {
        currentParentItem = state.systemComponents[0];
      }
      var url = rootState.global.httpServer + "/api/cat/" + currentParentItem.id + "/childs";
      $.ajax({
        url: url,
        method: "get",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        beforeSend: function(xhr) {
          xhr.setRequestHeader("accessToken",localStorage.accessToken);
        },
        success: function (response) {
          if (response.status == "SUCCESS") {
            var childrenList = response.data;
            currentParentItem.children = [];
            childrenList.forEach(function (child) {
              if (!child.component) {//判断是分类还是组件
                child["children"] = [];
              }
              child["label"] = child["name"];
              currentParentItem.children.push(child);
            });
            if(currentParentItem.children.length>0) {
              currentParentItem.children.forEach(item => {
                if(!item.component) {
                  dispatch("getSysComponentData",item);
                }
              })
            }
            resolve(response);
          } else if (response.status == "FAIL") {
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
    });
  },
  getMenuProjectList({state, rootState, commit}, index) {
    //目标父节点
    var currentParentItem = state.flowData.currentParentItem;
    switch (index) {
      case 0://获取根节点下数据列表
        currentParentItem = state.projectMenu[0];
        break;
      case 1://获取当前项子节点列表
        currentParentItem = state.flowData.currentProjectItem;
        break;
      case 2://获取指向目录下子节点列表
        currentParentItem = state.flowData.targetParentItem;
        break;
      case 3://重新加载当前项父节点下子节点列表
        currentParentItem = state.flowData.currentParentItem;
        break;
    }
    var url = "";
    if (currentParentItem.id == 0) {//根节点
      url = rootState.global.httpServer + "/api/project/0/child";
    } else {//子节点
      url = rootState.global.httpServer + "/api/project/" + currentParentItem.id + "/child";
    }
    $.ajax({
      url: url,
      method: "get",
      dataType: "json",
      contentType: "application/json; charset=utf-8",
      beforeSend: function(xhr) {
        xhr.setRequestHeader("accessToken",localStorage.accessToken);
      },
      success: function (response) {
        if (response.status == "SUCCESS") {
          var childrenList = response.data;
          currentParentItem.children = [];
          childrenList.forEach(function (child) {
            if (!child.leaf) {
              child["children"] = [];
              child["open"] = false;
              child["loading"] = false;
            }
            currentParentItem.children.push(child);
          });
        } else if (response.status == "FAIL") {
          store.commit("dealRequestError", {
            status: 405,
            responseJSON: {
              message: response.message
            }
          });
        }
        currentParentItem.loading = false;//取消加载状态
      },
      error: function (response) {
        currentParentItem.loading = false;//取消加载状态
        commit("dealRequestError", response);
      }
    });
  },
  //获取当前工程的json数据  如果指定projectId  则根据id获取工程数据，否则根据当前工程详情获取id
  getCurrentFlowJson({state, rootState, commit, dispatch}, projectId) {
    //设置切换状态
    state.flowData.projectChange = true;
    var curProItemId = 0;
    if (projectId) {
      curProItemId = projectId;
    } else {
      var curProjectItem = state.flowData.currentProjectDetail;
      curProItemId = curProjectItem.id;
    }
    var url = rootState.global.httpServer + "/api/project/" + curProItemId;

    dispatch("fillProjectJsonData", {
      url: url
    });
  },
  fillCurrentJson({state, rootState, commit, dispatch}, payload) {
    try {
      var json = JSON.parse(payload.json);
      state.flowData.nodes = json.nodes;
      state.flowData.links = json.links;
      if (json.style) {
        state.flowData.contentStyle.height = json.style.height;
      } else {
        state.flowData.contentStyle.height = 120;
      }
      //如果是查看流程则不对流程组件进行更新校验
      if (rootState.global.otherState.previewProjectFlow == false) {
        setTimeout(function () {
          dispatch('checkNodeUpdate', {
            summary: json.summary
          });
        }, 500);
      }
    } catch (e) {
      console.error(e.message)
    }
  },//填充当前流程json
  //根据Url填充json
  fillProjectJsonData({state, rootState, commit, dispatch}, payload) {
    $.ajax({
      url: payload.url,
      method: "get",
      dataType: "json",
      contentType: "application/json; charset=utf-8",
      beforeSend: function(xhr) {
        xhr.setRequestHeader("accessToken",localStorage.accessToken);
      },
      success: function (response) {
        if (response.status == "SUCCESS") {
          state.flowData.currentProjectDetail = response.data;
          if (response.data.json) {
            dispatch("fillCurrentJson", {
              json: response.data.json
            });
          } else {
            state.flowData.contentStyle.height = 120;
            state.flowData.nodes = [];
            state.flowData.links = [];
          }
          //切换完成
          state.flowData.projectChange = false;
        }
      },
      error: function (response) {
        //如果文件不存在则重置并生成
        if (response.status == 500) {
          state.flowData.contentStyle.height = 120;
          state.flowData.nodes = [];
          state.flowData.links = [];
          dispatch("saveCurrentFlowJson");
        } else {
          commit("dealRequestError", response);
        }
        //切换完成
        state.flowData.projectChange = false;
      }
    });
  },
  //同步流程json数据到服务端
  saveCurrentFlowJson({state, rootState, commit, dispatch}, payload) {
    try {
      var curProjectItem = state.flowData.currentProjectDetail;
      var data = {};
      var nodes = state.flowData.nodes;
      var links = state.flowData.links;
      data["style"] = state.flowData.contentStyle;
      data["nodes"] = nodes;
      data["links"] = links;
      let formData = new FormData();
      formData.append("content", JSON.stringify(data));
      $.ajax({
        url: rootState.global.httpServer + "/api/project/" + curProjectItem.id,
        method: "post",
        data: formData,
        processData: false,
        contentType: false,
        beforeSend: function(xhr) {
          xhr.setRequestHeader("accessToken",localStorage.accessToken);
        },
        success: function (response) {
          //保存完成后运行流程
          if (payload && payload.run) {
            dispatch("startApplication",payload);
          }
        },
        error: function (response) {
          state.flowData.isRunning = false;
          commit("dealRequestError", response);
        }
      });
    } catch (e) {
      state.flowData.isRunning = false;
      console.log(e.message);
    }
  },
  //运行流程 payload: {runIndex:index,projectId:projectId,componentId:componentId}
  //runIndex: 1,全部运行 2,运行到当前节点 3,运行此节点 4，从此节点开始运行
  runFlow({state, rootState, commit, dispatch}, payload) {
    try {
      //运行前将流程恢复默认状态
      commit("backFlowToDefault");
      var url = rootState.global.httpServer;
      switch (payload.runIndex) {
        case 1: //全部运行
          url += `/api/project/execute/${payload.projectId}?executionId=${payload.executionId}`;
          break;
        case 2: //运行到此处
          url += `/api/project/execute/${payload.projectId}/endAt/${payload.componentId}?executionId=${payload.executionId}`;
          break;
        case 3: //运行该节点
          url += `/api/project/execute/${payload.projectId}/only/${payload.componentId}?executionId=${payload.executionId}`;
          break;
        case 4: //从此节点运行
          url += `/api/project/execute/${payload.projectId}/startAt/${payload.componentId}?executionId=${payload.executionId}`;
          break;
      }
      if (state.flowData.nodes.length == 0) {
        state.flowData.isRunning = false;
        return;
      }
      var interval = null;
      $.ajax({
        url: url,
        method: "get",
        dataType: "json",
        beforeSend: function (xhr) {
          xhr.setRequestHeader("accessToken",localStorage.accessToken);
          interval = setInterval(function () {
            rootState.global.exceptionDeal.checkExcuteStatue = !rootState.global.exceptionDeal.checkExcuteStatue;
            clearInterval(interval);
          }, 5000)
        },
        success: function (response) {},
        error: function (response) {
          //出现错误，则修改状态为运行结束
          state.flowData.isRunning = false;
          commit("dealRequestError", response);
        },
        complete: function (data) {
          if (interval != null) {
            clearInterval(interval);
          }
        }
      });
    } catch (e) {
      state.flowData.isRunning = false;
      console.error(e.message)
    }
  },
  //停止流程
  stopFlow: function ({state, rootState, commit, dispatch}) {
    if (state.flowData.workFlowId == 0) {
      console.log("流程没有运行");
      return;
    }
    $.ajax({
      url: rootState.global.httpServer + "/api/project/shutdown/" + state.flowData.workFlowId ,
      method: "get",
      dataType: "json",
      contentType: "application/json; charset=utf-8",
      beforeSend: function(xhr) {
        xhr.setRequestHeader("accessToken",localStorage.accessToken);
      },
      success: function (response) {
        console.log(response);
        state.flowData.workFlowId = 0;
        state.flowData.links.forEach(function (item) {
          item.runStyle = "";
        });
      },
      error: function (response) {
        commit("dealRequestError", response);
      }
    });
  },
  //删除选中节点及连线
  deleteFlowNode({state, rootState, commit, dispatch}) {
    if (state.flowData.currentNodeId.curNodeId != "") {
      var arrs = [];//创建临时变量用来存储节点相关线的id，
      state.flowData.links.forEach(function (element) {//满足条件的pathId添加到临时数组中
        if (element.source == state.flowData.currentNodeId.curNodeId || element.target == state.flowData.currentNodeId.curNodeId) {
          arrs = arrs.concat(element.id)
        }
      });
      //这里不直接遍历store.links的原因是：
      // 在遍历过程中会对源进行操作，源对象改变后遍历对象同时改变，造成部分线条无法删除
      arrs.forEach(function (ele) {//遍历数组完成线的删除，
        commit('updateFlowCurrentPath', {
          curPathId: ele,
          curPathIndex: state.flowData.links.findIndex(node => node.id == ele)
        });
        dispatch("removeFlowPath");//移除连线
      });
      state.flowData.selectComponent = "";//置空属性菜单
      //state.menuProject.flowData.data.splice(state.menuProject.flowData.data.findIndex(node => node.id==state.menuProject.flowData.currentNodeId.curNodeId),1);//删除节点相关数据
      state.flowData.nodes.splice(state.flowData.currentNodeId.curNodeIndex, 1);//删除选中节点
      //置空选中节点
      commit('updateCurrentNode', {
        curPathId: "",
        curPathIndex: ""
      });
    }
  },
  //删除连线
  removeFlowPath({state, commit}) {
    if (state.flowData.currentPath.curPathId != "") {
      var path = state.flowData.links[state.flowData.currentPath.curPathIndex];
      //恢复线条指向的点的状态
      state.flowData.nodes.filter(node => node.id == path.target)[0].inputs.filter(node => node.id == path.inputPortId)[0].isConnected = false;
      //移除选中的连线
      state.flowData.links.splice(state.flowData.currentPath.curPathIndex, 1);
      //选中线条属性置空
      commit('updateFlowCurrentPath', {
        curPathId: "",
        curPathIndex: ""
      });
    }
  },
  //检查节点是否需要更新参数
  checkNodeUpdate({state, rootState, commit, dispatch}, payload) {
    var nodes = state.flowData.nodes;
    //组件最近更新时间集
    var summary = payload.summary;
    //遍历节点
    nodes.forEach(function (node) {
      //过滤节点对应组件项
      var sumItem = summary.filter(sum => sum.serverId == node.serverId);
      //组件项存在
      if (sumItem.length > 0) {
        //node最后更新时间（创建时间）
        var tabCreateTime = new Date(node.tabs[0].createTime).getTime();
        var lastUpdateTime = node.lastUpdateTime ? node.lastUpdateTime : tabCreateTime;
        //当组件最后更新时间大于流程中节点最后更新时间时
        if (sumItem[0]['last_update_time'] > lastUpdateTime) {
          //获取当前组件信息，并对流程节点进行更新，更新规则为：
          //以最新组件为准，组装各个模块项；input/output根据key；
          // 参数根据name将流程中的参数值及额外参数更新到对应项
          $.ajax({
            url: rootState.global.httpServer + "/api/component/" + node.serverId ,
            method: "get",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
              xhr.setRequestHeader("accessToken",localStorage.accessToken);
            },
            success: function (response) {
              if (response.status == "SUCCESS") {
                try {
                  var tempComp = response.data;
                  var inputs = [];//输入点集合
                  var inputData = [];//输入集
                  var outputData = []; //输出集
                  if (tempComp.inputs.length > 0) {
                    var inNum = tempComp.inputs.length;//输入点数量
                    var inEveryLeft = 180 / (inNum + 1);
                    tempComp.inputs.forEach(function (input, index) {
                      var inputItem = {
                        id: node.id + "_1_" + (index + 1),
                        isConnected: false,
                        left: inEveryLeft * (index + 1),
                        description: input.description,
                        type: input.type,
                        key: input.key,
                        cat: input.cat,
                        model: input.model,
                        canPreview: input.canPreview,
                        value: node.id + "_1_" + (index + 1)
                      };
                      var nodeInput = node.inputs.filter(node => node.key == input.key);
                      //更新流程节点已存在数据
                      if (nodeInput.length > 0) {
                        inputItem.isConnected = nodeInput[0].isConnected;
                        inputItem.description = nodeInput[0].description;
                        inputItem.model = nodeInput[0].model;
                        inputItem.canPreview = nodeInput[0].canPreview;
                      }
                      inputs.push(inputItem);
                      var inputDataItem = {
                        key: input.key,
                        dataFields: []
                      };
                      var nodeInputData = node.inputData.filter(node => node.key == inputDataItem.key);
                      if (nodeInputData.length > 0) {
                        inputDataItem.dataFields = nodeInputData[0].dataFields;
                      }
                      inputData.push(inputDataItem);
                      var outputDataItem = {
                        key: input.key,
                        dataFields: []
                      };
                      var nodeOutputData = node.outputData.filter(node => node.key == outputDataItem.key);
                      if (nodeOutputData.length > 0) {
                        outputDataItem.dataFields = nodeOutputData[0].dataFields;
                      }
                      outputData.push(outputDataItem);
                    });
                  } else {
                    if (tempComp.targetAlgorithm == "com.tipdm.framework.dmserver.core.algo.unparallel.io.Input") {
                      var outputDataItem = {
                        key: "database",
                        dataFields: []
                      };
                      var nodeOutputData = node.outputData.filter(node => node.key == outputDataItem.key);
                      if (nodeOutputData.length > 0) {
                        outputDataItem.dataFields = nodeOutputData[0].dataFields;
                      }
                      outputData.push(outputDataItem);
                    }
                  }
                  var outputs = [];//输出点集合
                  if (tempComp.outputs) {
                    var outNum = tempComp.outputs.length;
                    var outEveryLeft = 180 / (outNum + 1);
                    tempComp.outputs.forEach(function (output, index) {
                      var modelPath = "";
                      if (output.value) {//当添加为模型时，value值为模型路径
                        modelPath = output.value;
                      } else {
                        if (node.outputs[0].value != node.outputs[0].id) {
                          modelPath = node.outputs[0].value;
                        } else {
                          modelPath = node.id + "_0_" + (index + 1);
                        }
                        // //如果更新的组件为数据源时，输出值保持不变
                        // if (tempComp.targetAlgorithm == "com.tipdm.framework.dmserver.core.algo.unparallel.io.Input") {
                        //   modelPath = node.outputs[0].value;
                        // } else {
                        //   modelPath = node.id + "_0_" + (index + 1);
                        // }
                      }
                      var outputItem = {
                        id: node.id + "_0_" + (index + 1),
                        left: outEveryLeft * (index + 1),
                        description: output.description,
                        type: output.type,
                        key: output.key,
                        cat: output.cat,
                        access: output.access,
                        columns: output.columns,
                        model: output.model,
                        canPreview: output.canPreview,
                        value: modelPath
                      };
                      var nodeOutput = node.outputs.filter(node => node.key == outputItem.key);
                      //更新流程节点已存在数据
                      if (nodeOutput.length > 0) {
                        outputItem.description = nodeOutput[0].description;
                        outputItem.model = nodeOutput[0].model;
                        outputItem.canPreview = nodeOutput[0].canPreview;
                      }
                      outputs.push(outputItem);
                    });
                  }
                  var tabs = [];//tab属性
                  var nodeEles = [];
                  node.tabs.forEach(function (tab) {
                    tab.elements.forEach(function (ele) {
                      nodeEles.push(ele);
                    });
                  });
                  tempComp.tabs.forEach(function (tabItem) {
                    tabItem.elements.forEach(function (ele) {
                      //如果控件为数据源dataGrid，则添加字段属性 规则 element.name+"Items"字符串
                      if (ele.elementType == 5) {
                        ele.extra[ele.name + "Items"] = [];
                      } else if (ele.elementType == 0) {
                        ele.extra["tableName"] = "";
                      }
                      var nodeEle = nodeEles.filter(node => node.name == ele.name);
                      if (nodeEle.length > 0) {
                        ele.value = nodeEle[0].value;
                        for (var key in nodeEle[0].extra) {
                          ele.extra[key] = nodeEle[0].extra[key];
                        }
                      }
                    });
                    tabs.push(tabItem);
                  });
                  // node.name = tempComp.name;//节点名称
                  node.allowViewSource = tempComp.allowViewSource;//是否支持R源码预览
                  node.supportPMML = tempComp.supportPMML;//是否支持PMML
                  node.hasReport = tempComp.hasReport;//报告
                  node.iconPath = tempComp.iconPath ? (tempComp.iconPath.length > 0 ? tempComp.iconPath : "fa-cubes") : "fa-cubes";
                  node.targetAlgorithm = tempComp.targetAlgorithm;//算法服务
                  node.minimumInput = tempComp.minimumInput;//输入最小个数
                  node.description = tempComp.description;//组件描述
                  node.inputs = inputs;
                  node.outputs = outputs;
                  node.tabs = tabs;
                  node.inputData = inputData;//节点的输入数据集，可能存在多个
                  node.outputData = outputData;//节点的输出数据集，结构与输入保持一致
                  //node.outputExtraData = [];//当前节点的额外输出字段
                  node.lastUpdateTime = new Date().getTime()//添加更新日期时间戳
                  console.log(node.name + "--组件已更新。。。");
                } catch (e) {
                  console.error(e.message)
                }
              }
            },
            error: function (response) {
              commit("dealRequestError", response);
            }
          });
        }
      }
    });
  },
  checkNodeElements({state}) {//校验参数合法性
    return new Promise((resolve, reject) => {
      var testResult = true;
      var nodeMsg = '';
      var tabMsg = '';
      var eleMsg = '';
      var message = '';
      state.flowData.nodes.forEach((node) => {//遍历节点
        node.tabs.forEach((tab) => {//遍历tab项
          tab.elements.forEach((ele) => {//遍历参数
            if (ele.required) {
              var reg = null;
              try{
                reg = eval(ele.rexp);
                if (!reg.test(ele.value)) {//校验不通过
                  nodeMsg = "节点【" + node.name + "】";
                  tabMsg = "选项卡【" + tab.tabName + "】";
                  eleMsg = "属性【" + ele.label + "】不合法";
                  message = nodeMsg + tabMsg + eleMsg;
                  testResult = false;
                }
              } catch(e) {
                nodeMsg = "节点【" + node.name + "】";
                tabMsg = "选项卡【" + tab.tabName + "】";
                eleMsg = "属性【" + ele.label + "】校验为非法的正则表达式";
                message = nodeMsg + tabMsg + eleMsg;
                testResult = false;
              }
            }
          });
        });
      });
      resolve({
        testResult: testResult,
        msg: message
      })
    });
  }
}

// mutations
const mutations = {
  //修改流程节点及连线状态为默认状态
  backFlowToDefault(state) {
    var nodes = state.flowData.nodes;
    var links = state.flowData.links;
    nodes.forEach(function (node) {
      node.runStyle = "status-disW";
    });
    links.forEach(function (link) {
      link.runStyle = "";
    });
  },
  updateMenuProjectSelectComponent(state, component) {
    state.flowData.selectComponent = component;
  },
  //更新组件切换状态，用来销毁右侧组件重构
  updateMenuProjectNodeChange(state) {
    state.flowData.nodeChange = !state.flowData.nodeChange;
  },
  //清理当前工程数据
  cleanCurFlowData(state) {
    state.flowData.currentProjectDetail = {};
    state.flowData.nodes = [];
    state.flowData.links = [];
  },
  //更新当前连线节点
  updateFlowCurrentPath(state, payload) {
    state.flowData.currentPath.curPathId = payload.curPathId;
    state.flowData.currentPath.curPathIndex = payload.curPathIndex;
  },
  //更新可视化编辑移动节点的坐标
  updateNodeXYState(state, nodeId) {
    var node = state.flowData.nodes.filter(node => node.id == nodeId)[0];
    var position = state.flowData.currentXY;
    node.left = position.left > 0 ? position.left : 0;
    node.top = position.top > 0 ? position.top : 0;
  },
  //更新当前node节点
  updateCurrentNode(state, payload) {
    state.flowData.currentNodeId.curNodeId = payload.curNodeId;
    state.flowData.currentNodeId.curNodeIndex = payload.curNodeIndex;
  },
  //添加流程线条
  addFlowLinkNode(state, payload) {
    var targetItem = state.flowData.target;
    var pathItem = {//连线信息
      id: payload.outputPortId + "_" + targetItem.targetPortId,
      source: payload.sourceId,
      target: targetItem.targetNodeId,
      inputPortId: targetItem.targetPortId,
      outputPortId: payload.outputPortId,
      d: payload.d,
      runStyle: "",//线条状态pane-link-flow
    };
    state.flowData.links.push(pathItem);//添加连线
    //设置箭头指向的点的样式
    state.flowData.nodes.filter(node => node.id == pathItem.target)[0].inputs.filter(node => node.id == pathItem.inputPortId)[0].isConnected = true;
  },
}

export default {
  state,
  getters,
  actions,
  mutations
}

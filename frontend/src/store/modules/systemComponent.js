import $ from 'jquery';

/**
 * author jiaozhengsheng@163.com
 * @type {{menuComponentsTree: *[], componentsDeals: {defaultEditComponent: {outputs: Array, inputs: Array, creatorId: string, creatorName: string, tabs: {tabName: string, createTime: string, elements: Array, id: number}[], description: string, enabled: boolean, supportPMML: boolean, parentId: number, script: {MAIN: string}, component: boolean, hasReport: boolean, targetAlgorithm: string, createTime: string, allowViewSource: boolean, extra: {engine: string}, name: string, id: number, iconPath: string, minimumInput: string}, basicFieldParamDeals: {TabItem: string, editItem: {visible: boolean, defaultValue: string, toolTip: string, label: string, required: boolean, sequence: number, createTime: string, extra: {}, name: string, options: string, id: number, placeholder: string, elementType: number, value: string, rexp: string}, showEditDialog: boolean, editStatue: string, counter: number}, currentParentItem: string, componentEdit: string, currentView: string, basicMessageSelectClass: string, basicScriptDeals: {curTabItem: string, showEditor: boolean}, treeMenuLoading: boolean, basicInputDeals: {showDialog: boolean, editItem: {createTime: string, cat: string, description: string, model: boolean, id: string, type: string, key: string, canPreview: boolean}, editStatue: string, counter: number}, engine: string, basicOutputDeals: {showDialog: boolean, editItem: {access: string, createTime: string, columns: string, cat: string, description: string, model: boolean, id: string, type: string, key: string, canPreview: boolean}, editStatue: string, counter: number}, editComponent: {outputs: Array, inputs: Array, creatorId: string, creatorName: string, tabs: {tabName: string, createTime: string, elements: Array, id: number}[], description: string, enabled: boolean, supportPMML: boolean, parentId: number, script: {}, component: boolean, hasReport: boolean, targetAlgorithm: string, createTime: string, allowViewSource: boolean, extra: {engine: string}, name: string, id: number, iconPath: string, minimumInput: string}, componentsContextMenuMsg: {top: number, left: number, display: string}, currentCompItem: string}}}
 */
const state = {
  menuComponentsTree: [
    {
      id: 0,
      name: "系统组件",
      enabled: true,
      component: false,
      open: false,
      loading: false,
      children: []
    }
  ],//组件管理与自定义算法列表源
  componentsDeals: {//组件管理中的数据交互
    engine: "python", //编辑器的模式默认为python
    currentView: "", //component_content_tabs
    componentEdit: "add", //组件编辑状态 add/edit
    currentParentItem: "",//操作目标项的父类项
    currentCompItem: "ModuleDescription",//左侧属性菜单项右键时的当前项，可能是分类菜单也可能是组件
    basicMessageSelectClass: "",//目标类  选择目录时使用
    componentsContextMenuMsg: {
      left: 200,
      top: 200,
      display: "none"
    },//树形控件项右键菜单定位设置
    basicInputDeals: {
      showDialog: false,
      editStatue: "add",//编辑状态 add/edit
      counter: 1,
      editItem: {//当前编辑项
        id: "", //0表示为新增
        createTime: "",
        description: "",//描述
        type: "INPUT",//类型
        key: "",//脚本对应名称
        cat: "DATA",//输入类别
        model: false,//输入模型
        canPreview: false//是否能够预览
      }
    },//输入tab页交互
    basicOutputDeals: {
      showDialog: false,
      editStatue: "add",//编辑状态 add/edit
      counter: 1,
      editItem: {//当前编辑项
        id: "", //0表示为新增
        createTime: "",
        description: "",//描述
        type: "OUTPUT",//类型
        key: "",//脚本对应名称
        cat: "DATA",//输入类别
        access: "SAME",//源数据设置
        model: false,//输入模型
        canPreview: false,//是否能够预览
        columns: ""//列字段
      }
    },//输出tab页交互
    basicFieldParamDeals: {
      showEditDialog: false,
      editStatue: "add",//编辑状态 add/edit
      TabItem: "", //当前tab项
      counter: 1,
      editItem: {
        id: 1,
        createTime: "",
        name: "",  //参数名称
        elementType: 0,   //参数类型
        label: "",  //label值
        value: "",  //值
        rexp: "", //正则表达式
        defaultValue: "", //默认值
        placeholder: "",//提示内容
        toolTip: "",//悬浮提示
        options: "",//下拉备填项
        required: false,  //是否必填
        sequence: 1,  //顺序
        visible: true, //是否可用
        extra: {}//额外参数
      }
    },//参数设置交互数据
    basicScriptDeals: {
      curTabItem: "",//当前选中tab项
      showEditor: true
    },//脚本
    defaultEditComponent: {
      "id": 0,
      "createTime": "",
      "creatorId": "",
      "creatorName": "",
      "component": true,
      "enabled": true, //组件是否可用
      "supportPMML": false,//是否支持PMML
      "allowViewSource": false,//是否支持源码预览
      // "paralleled": false, //是否支持并行算法
      "hasReport": false,//是否有输出运行报告
      "name": "", //组件名称
      "targetAlgorithm": "",//服务名称
      "minimumInput": "1",//输入最小个数
      "parentId": 0, //父类id
      "iconPath": "", //图标路径
      "extra": {
        "engine": "PYTHON"
      },
      "script": {
        "MAIN": ""//默认添加主体脚本tab页
      },//脚本对象
      "description": "", //描述
      "inputs": [],
      "outputs": [],
      "tabs": [
        {
          "id": 1,
          "createTime": "",
          "tabName": "字段属性",  //tab页名称
          "elements": []
        }
      ]
    },//添加组件参数默认状态
    editComponent: {
      "id": 0,
      "createTime": "",
      "creatorId": "",
      "creatorName": "",
      "component": true,
      "enabled": true, //组件是否可用
      "supportPMML": false,//是否支持PMML
      "allowViewSource": false,//是否支持源码预览
      // "paralleled": false, //是否支持并行算法
      "hasReport": false,//是否有输出运行报告
      "name": "", //组件名称
      "targetAlgorithm": "",//服务名称
      "minimumInput": "1",//输入最小个数
      "parentId": 0, //父类id
      "iconPath": "", //图标路径
      "extra": {
        "engine": "PYTHON"
      },
      "script": {},//脚本对象
      "description": "", //描述
      "inputs": [],
      "outputs": [],
      "tabs": [
        {
          "id": 1,
          "createTime": "",
          "tabName": "字段属性",  //tab页名称
          "elements": []
        }
      ]
    },//当前编辑组件参数 --默认
    treeMenuLoading: true//系统组件默认显示正在加载状态
  }
}

// getters
const getters = {}

// actions
const actions = {
  //获取组件树的子组件
  loadMenuComponentsChildNode({rootState, commit}, nodeItem) {
    var url = `${rootState.global.httpServer}/api/cat/${nodeItem.id}/childs`;
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
          nodeItem.children = [];
          childrenList.forEach(function (child) {
            if (!child.component) {//判断是分类还是组件
              child["children"] = [];
              child["open"] = false;
              child["loading"] = false;
            }
            nodeItem.children.push(child);
          });
        } else if (response.status == "FAIL") {
          commit("dealRequestError", {
            status: 405,
            responseJSON: {
              message: response.message
            }
          });
        }
        nodeItem.loading = false;//取消加载状态
      },
      error: function (response) {
        commit("dealRequestError", response);
      }
    });
  },
  //获取通用组件树列表，懒加载模式
  getMenuComponentsList({state, rootState, commit}, index) {
    //目标父节点
    var currentParentItem = state.componentsDeals.currentParentItem;//目标父节点
    switch (index) {
      case 0://获取根节点下数据
        currentParentItem = state.menuComponentsTree[0];
        break;
      case 1://获取当前项子节点
        currentParentItem = state.componentsDeals.currentCompItem;
        break;
      case 2://获取目标节点下子节点
        currentParentItem = state.componentsDeals.basicMessageSelectClass;
        break;
      case 3://重载父类下的目录
        currentParentItem = state.componentsDeals.currentParentItem;
        break;
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
              child["open"] = false;
              child["loading"] = false;
            }
            currentParentItem.children.push(child);
          });
        } else if (response.status == "FAIL") {
          commit("dealRequestError", {
            status: 405,
            responseJSON: {
              message: response.message
            }
          });
        }
        currentParentItem.loading = false;//取消加载状态
        //数据加载完成后将加载状态设置为取消加载
        state.componentsDeals.treeMenuLoading = false;
      },
      error: function (response) {
        commit("dealRequestError", response);
      }
    });
  },
}

// mutations
const mutations = {}

export default {
  state,
  getters,
  actions,
  mutations
}

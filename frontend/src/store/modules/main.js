import $ from 'jquery';

const state = {
  templateList: [],//模型列表
  bbsList: [],//bbs列表
  documents: [],//文档列表
  pagination: {
    currentPage: 1,//当前页
    perSize: 5,//每页条数
    totalSize: 0 //数据总条数
  },//分页信息
  showDetail: {
    showFastCreateProjectDialog: false,
    copyTemplateDialog: false
  },
  currentTemplate: ""//当前模板
}

// getters
const getters = {}

// actions
const actions = {
  getBBSList({state, commit, rootState}) {
    var url = rootState.global.httpServer + "/api/banner/bbs/";
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
          var data = response.data;
          state.bbsList = [];
          for (var key in data) {
            state.bbsList.push({name: key, link: data[key]});
          }
        }
      },
      error: function (response) {
        commit("dealRequestError", response);
      }
    });
  },
  getDocumentsList({state, commit, rootState}) {
    var url = rootState.global.httpServer + "/api/banner/documentation/";
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
          var data = response.data;
          state.documents = [];
          for (var key in data) {
            state.documents.push({name: key, link: data[key]});
          }
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
        store.commit("dealRequestError", response);
      }
    });
  },
  //首页获取模板列表
  getTemplateList({state, rootState, commit}, index) {//获取模板列表
    var pagination = state.pagination;
    switch (index) {
      case 0://获取第一页
        pagination.currentPage = 1;
        break;
      case 1://加载更多页
        var totalPage = Math.ceil(pagination.totalSize / pagination.perSize);
        if (totalPage > pagination.currentPage) {
          pagination.currentPage += 1;
        } else {
          return;
        }
        break;
    }
    var url = rootState.global.httpServer + "/api/template/list?pageNumber=" + pagination.currentPage + "&pageSize=" + pagination.perSize;
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
          var items = response.data.content;
          switch (index) {
            case 0:
              state.templateList = items;
              state.pagination.totalSize = response.data.totalElements;
              break;
            case 1:
              items.forEach(function (item) {
                state.templateList.push(item);
              });
              state.pagination.totalSize = response.data.totalElements;
              break;
          }

        } else {
          console.error(response);
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

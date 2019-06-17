import Vue from 'vue'
import Vuex from 'vuex'
import global from './modules/global'
import main from './modules/main'
import datasource from './modules/datasource'
import project from './modules/project'
import systemComponent from './modules/systemComponent'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    global,
    main,
    datasource,
    project,
    systemComponent,
  }
})

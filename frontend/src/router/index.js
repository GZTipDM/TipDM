import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)
//登录导航页
const LoginBanner = resolve => {
  require.ensure(['@/components/LoginBanner.vue'], () => {
    resolve(require('@/components/LoginBanner.vue'))
  })
}
//主页-框架页
const Home = resolve => {
  require.ensure(['@/components/Home.vue'], () => {
    resolve(require('@/components/Home.vue'))
  })
}
//首页
const Main = resolve => {
  require.ensure(['@/components/Main.vue'], () => {
    resolve(require('@/components/Main.vue'))
  })
}
//数据源
const DataSource = resolve => {
  require.ensure(['@/components/DataSource.vue'], () => {
    resolve(require('@/components/DataSource.vue'))
  })
}
//工程管理
const Project = resolve => {
  require.ensure(['@/components/Project.vue'], () => {
    resolve(require('@/components/Project.vue'))
  })
}
//工程管理内容页
const FlowEditorContent = resolve => {
  require.ensure(['@/components/project/FlowEditorContent.vue'], () => {
    resolve(require('@/components/project/FlowEditorContent.vue'))
  })
}
//系统组件
const SystemComponent = resolve => {
  require.ensure(['@/components/SystemComponent.vue'], () => {
    resolve(require('@/components/SystemComponent.vue'))
  })
}

//菜单切换过渡页
const Transition = resolve => {
  require.ensure(['@/components/Transition.vue'], () => {
    resolve(require('@/components/Transition.vue'))
  })
}

const routes = [
  {path: '', redirect: '/login_banner'},
  {path: '/login_banner', name: 'LoginBanner', component: LoginBanner},
  {
    path: '/home', component: Home,
    children: [
      {path: '', redirect: 'main', name: 'Home'},
      {path: 'trans', component: Transition},//过渡页
      {path: 'main', name: 'Main', component: Main},
      {path: 'datasource', name: 'DataSource', component: DataSource},
      {
        path: 'project', component: Project,
        children: [
          {path: '', name: 'Project', component: FlowEditorContent}
        ]
      },
      {path: 'components', name: 'SystemComponent', component: SystemComponent}
    ],
  },
  {path: '/show', name: 'FlowEditorContent', component: FlowEditorContent}

]
const router = new Router({
  mode: 'history',
  routes: routes
})

router.beforeEach((to, from, next) => {
  if (to.matched.length === 0) {//如果未匹配到路由
    from.name ? next({name: from.name}) : next('/');//如果上级也未匹配到路由则跳转登录页面，如果上级能匹配到则转上级路由
  } else {
    next();//如果匹配到正确跳转
  }
});

export default router;

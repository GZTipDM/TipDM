<template>
    <li>
        <a href="javascript:void(0)" @contextmenu="rightClick($event)" :title="item.name">
            <span v-if="!item.component" @click="toggle">
                <i :class="[item.open ? (item.loading?'el-icon-loading':'el-icon-caret-bottom'): 'el-icon-caret-right']"></i>
                <i>{{item.name}}</i>
            </span>
            <span v-else-if="item.component" @click="editComponent">
                <i class="fa" :class="item.iconPath?item.iconPath:'fa-cubes'"></i>
                <i>{{ item.name}}</i>
            </span>
        </a>
        <ul v-show="item.open" v-if="!item.component">
            <SysCompTree v-for="(childItem,index) in item.children" :index="index" :item="childItem" :parentItem="item" :key="childItem.id"></SysCompTree>
        </ul>
    </li>
</template>
<script>
export default {
    name: 'SysCompTree',
    props: ['item','index','parentItem'],
    computed: {
        componentsDeals: function() {
            return this.$store.state.systemComponent.componentsDeals;
        }
    },
    methods: {
        toggle: function() {//文件夹的展开关闭
            var that = this;
            if (!this.item.component) {
                this.item.open = !this.item.open;
                if(this.item.children.length==0) {//如果该节点已经存在children节点，则不再重复获取
                    this.item.loading = true;
                    this.componentsDeals.currentCompItem = this.item;//设置当前操作项
                    this.componentsDeals.currentParentItem = this.parentItem;//设置当前项的父类集合
                    //获取添加子树
                    that.$store.dispatch("getMenuComponentsList",1);
                }
            }
        },
        editComponent: function() {//单击组件，编辑
            //设置状态为编辑状态
            this.componentsDeals.componentEdit = "edit";
            if(!this.item.script.hasOwnProperty("MAIN")) {
                this.item.script["MAIN"] = "";
            }
            //深度复制
            var copyResult = this.deepCopy(this.item);
            //设置当前组件编辑属性
            this.componentsDeals.editComponent = copyResult;
            //设置当前项的目标父类集合
            this.componentsDeals.currentParentItem = this.parentItem;
            //设置基本信息中的目录树选中目录
            this.componentsDeals.basicMessageSelectClass = this.parentItem;
            //切换组件视图
            this.componentsDeals.currentView = "";
            var that = this;
            setTimeout(function() {
                that.componentsDeals.currentView = "SysCompItem";
            },10);
        },
        rightClick: function(event) {
            if (document.all) { window.event.returnValue = false;} else {event.preventDefault()}//阻止默认菜单事件
            //设置当前操作项
            this.componentsDeals.currentCompItem = this.item;
            //设置当前项的父类集合
            this.componentsDeals.currentParentItem = this.parentItem;
            //更新content_menu 坐标位置
            var dom = document.getElementsByClassName("mCom")[0];//获取容器dom
            var centerDomOffset = dom.getBoundingClientRect();
            var top = (event.clientY - centerDomOffset.top);
            if(this.item.component) {//leaf
                top = (top+100)>centerDomOffset.height?(centerDomOffset.height-100):top;
            } else {
                top = (top+183)>centerDomOffset.height?(centerDomOffset.height-183):top;
            }
            this.componentsDeals.componentsContextMenuMsg = {
                left: event.clientX - centerDomOffset.left,
                top: top,
                display:'block'
            }
        },
        deepCopy: function(source) {
            var result;
            (source instanceof Array) ? (result = []) : (typeof(source)==="object")? (source===null?(result = ""):(result={})):(result=source);
            for (var key in source) {
                result[key] = (typeof source[key]==='object') ? this.deepCopy(source[key]) : source[key];
            }
            return result;
        }
    }
}
</script>

<style scoped>
    .on{
        color: #e29012;
    }
</style>

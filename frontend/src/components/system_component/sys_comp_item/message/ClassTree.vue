<template>
    <li>
        <a href="javascript:void(0)"  v-if="!item.component">
            <i :class="[open ? 'el-icon-caret-bottom': 'el-icon-caret-right']" @click="toggle"></i>
            <i :style="selectStyle" @click="selectItem" :id="'child'+item.id">{{ item.name }}</i>
        </a>
        <ul v-show="open" v-if="!item.component">
            <ClassTree v-for="childItem in item.children" :key="childItem.id" :item="childItem"></ClassTree>
        </ul>
    </li>
</template>
<script>
export default {
    name: 'ClassTree',
    props: ['item'],
    data() {
        return {
            open: false,
            selectStyle: {
                color: ""
            }
        }
    },
    computed: {
        componentsDeals: function() {
            return this.$store.state.systemComponent.componentsDeals;
        },
        basicMessageSelectClass: function() {
            return this.componentsDeals.basicMessageSelectClass;
        }
    },
    watch: {
        basicMessageSelectClass: function(val) {
            this.changeStyle(val);
        }
    },
    created: function() {
        this.changeStyle(this.basicMessageSelectClass);
    },
    methods: {
        toggle: function() {//文件夹的展开关闭
            var that = this;
            if (!this.item.component) {
                this.open = !this.open;
                if(this.item.children.length==0) {//如果该节点已经存在children节点，则不再重复获取
                    this.componentsDeals.currentCompItem = this.item;
                    //获取添加子树
                    that.$store.dispatch("getMenuComponentsList",1);
                }
            }
        },
        changeStyle: function(val) {
            try{
                if(this.item.id == val.id) {
                    this.selectStyle.color = "#e29012";
                } else {
                    this.selectStyle.color = "";
                }
            } catch(e) {console.error(e.message)}
        },
        selectItem: function() {//选择目录
            //设置当前树默认选中项，便于子节点加载
            this.componentsDeals.currentCompItem = this.item;
            //设置基本信息选择类
            this.componentsDeals.basicMessageSelectClass = this.item;
            this.componentsDeals.editComponent.parentId = this.item.id;
        }
    }
}
</script>

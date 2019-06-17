<template>
    <li>
        <a href="javascript:void(0)"  v-if="!item.leaf">
            <i :class="[open ? 'el-icon-caret-bottom': 'el-icon-caret-right']" @click="toggle"></i>
            <i :style="selectStyle" @click="selectItem" :id="'child'+item.id">{{ item.name }} </i>
        </a>
        <ul v-show="open" v-if="!item.leaf">
            <SelectTree v-for="childItem in item.children" :item="childItem" :key="childItem.id"></SelectTree>
        </ul>
    </li>
</template>
<script>
export default {
    name: 'SelectTree',
    props: ['item'],
    data() {
        return {
            open: true,
            selectStyle: {
                color: ""
            }
        }
    },
    computed: {
        flowData: function() {
            return this.$store.state.project.flowData;
        },
        rightContentTargetItem: function() {
            return this.flowData.rightContentTargetItem;
        }
    },
    created: function() {
        this.changeStyle(this.rightContentTargetItem);
        //由于要默认展开所有目录，所以要在创建树的时候把所有的目录都加载出来
        if (!this.item.leaf) {
            if(this.item.children.length==0) {//如果该节点已经存在children节点，则不再重复获取
                this.flowData.currentParentItem = this.item;
                //获取添加子树
                this.$store.dispatch("getMenuProjectList",3);
            }
        }

    },
    watch: {
        rightContentTargetItem: function(val) {
            this.changeStyle(val);
        }
    },
    methods: {
        toggle: function() {//文件夹的展开关闭
            var that = this;
            if (!this.item.leaf) {
                this.open = !this.open;
            }
        },
        selectItem: function() {//选择目录
            //设置添加菜单项为父类
            this.flowData.rightContentTargetItem = this.item;
            this.flowData.currentParentItem = this.item;
        },
        changeStyle: function(val) {
            if(this.item.id == val.id) {
                this.selectStyle.color = "#e29012";
            } else {
                this.selectStyle.color = "";
            }
        }
    }
}
</script>

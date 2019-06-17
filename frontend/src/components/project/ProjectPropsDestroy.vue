<!--用来销毁重构选项卡-->
<template>
  <node_template1 v-if="curNode"></node_template1>
  <node_template2 v-else-if="!curNode"></node_template2>
</template>

<script>
  import ProjectProps from "./ProjectProps.vue";

  export default {
    name: 'ProjectPropsDestroy',
    components: {
      node_template1: ProjectProps,
      node_template2: ProjectProps
    },
    computed: {
      curNode: function () {//当组件需要重新渲染的时候进行销毁
        return this.$store.state.project.flowData.nodeChange;
      }
    }
  }
</script>

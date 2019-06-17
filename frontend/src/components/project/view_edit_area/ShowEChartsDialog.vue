<template>
    <el-dialog title="可视化展示" top="10%" :visible.sync="viewEditArea.showEChartsDialog" class="center" width="85%"
               custom-class="show_chart_content_style">
        <div style="width:1050px; height:450px; overflow: auto">
            <template v-if="drawingType=='bar'">
                <tip_bar :curNodeItem="currentNodeItem" :showContent="viewEditArea.showEChartsDialog"></tip_bar>
            </template>
            <template v-else-if="drawingType=='pie'">
                <tip_pie :curNodeItem="currentNodeItem" :showContent="viewEditArea.showEChartsDialog"></tip_pie>
            </template>
            <template v-else-if="drawingType=='line'">
                <tip_line :curNodeItem="currentNodeItem" :showContent="viewEditArea.showEChartsDialog"></tip_line>
            </template>
            <template v-else-if="drawingType=='area'">
                <tip_area :curNodeItem="currentNodeItem" :showContent="viewEditArea.showEChartsDialog"></tip_area>
            </template>
            <template v-else-if="drawingType=='scatter'">
                <tip_scatter :curNodeItem="currentNodeItem" :showContent="viewEditArea.showEChartsDialog"></tip_scatter>
            </template>
            <template v-else-if="drawingType=='wordCloud'">
                <tip_word_cloud :curNodeItem="currentNodeItem" :showContent="viewEditArea.showEChartsDialog"></tip_word_cloud>
            </template>
        </div>
    </el-dialog>
</template>
<style>
    .show_chart_content_style {
        width: 1100px;
        height: 550px;
    }
</style>
<script>
    var TipBar = require("../../../visual/TipBar.vue");
    var TipPie = require("../../../visual/TipPie.vue");
    var TipLine = require("../../../visual/TipLine.vue");
    var TipArea = require("../../../visual/TipArea.vue");
    var TipScatter = require("../../../visual/TipScatter.vue");
    var TipWordCloud = require("../../../visual/TipWordCloud.vue");
   export default {
     name: 'ShowEChartsDialog',
        computed: {
            viewEditArea: function () {
                return this.$store.state.project.viewEditArea;
            },
            flowData: function () {
                return this.$store.state.project.flowData;
            },
            currentNodeItem: function() {
                return this.flowData.currentNodeItem;
            },
            drawingType: function () {
                var curNode = this.flowData.currentNodeItem;
                var result = "";
                if (curNode.tabs) {
                    curNode.tabs.forEach(function (tab) {
                        tab.elements.forEach(function (ele) {
                            if (ele.name == "drawingType") {
                                result = ele.value;
                            }
                        });
                    });
                }
                return result;
            }
        },
        components: {
            tip_bar: TipBar,
            tip_pie: TipPie,
            tip_line: TipLine,
            tip_area: TipArea,
            tip_scatter: TipScatter,
            tip_word_cloud: TipWordCloud
        }
    }
</script>

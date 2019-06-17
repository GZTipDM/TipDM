<template>
  <!--type
  [0：数据源节点的输入搜索下拉框， 1:文本输入框 ，2：数值输入框 ， 3：下拉单选框，
  4：文本域输入框， 5：表结构列表，  6：单选框，  7：多选框 8-10：文本编辑器-pgsql/r/python
  11:条件列表, 12:字段下拉选择框,  13:表结构列表（只读）, 14:输入列表展示, 15:复选框组
  16HIVE数据源输入搜索下拉 17HIVE表结构列表，18添加字段文本输入框 19修改输出字段名称
  20密码框 21通过remoteUrl填充下拉选择 22字段下拉多选框 23可视化组件绘图按钮
  24可视化字段与条件复合选择组件 25修改输出字段类型 26自定义级联选择器 27衍生变量
  -->
  <!--0数据源输入搜索下拉框-->
  <TipSearchSelect :item="item" :index="index" :type="database" v-if="item.elementType==0 "></TipSearchSelect>
  <!--1文本框-->
  <TipInputText :type="text" :item="item" :index="index" v-else-if="item.elementType == 1 "></TipInputText>
  <!--2数值输入框-->
  <TipInputNumber :item="item" :index="index" v-else-if="item.elementType==2 "></TipInputNumber>
  <!--3单选框-->
  <TipSelectedSingle :item="item" :index="index" v-else-if="item.elementType==3 "></TipSelectedSingle>
  <!--4文本域-->
  <TipTextArea :item="item" :index="index" v-else-if="item.elementType==4"></TipTextArea>
  <!--5表结构列表-->
  <TipDataGrid :item="item" :type="database" :index="index" v-else-if="item.elementType==5"></TipDataGrid>
  <!--6单选框-->
  <TipRadio :item="item" :index="index" v-else-if="item.elementType==6"></TipRadio>
  <!--7多选框-->
  <TipCheckBox :item="item" :index="index" v-else-if="item.elementType==7"></TipCheckBox>
  <!--8文本编辑器-pgsql-->
  <TipAceEditor :item="item" :index="index" :models="pgsql" v-else-if="item.elementType==8"></TipAceEditor>
  <!--9文本编辑器-r-->
  <TipAceEditor :item="item" :index="index" :models="r" v-else-if="item.elementType==9"></TipAceEditor>
  <!--10文本编辑器-python-->
  <TipAceEditor :item="item" :index="index" :models="python" v-else-if="item.elementType==10"></TipAceEditor>
  <!--11条件l列表-->
  <TipCondition :nodeItem="nodeItem" :item="item" :index="index" v-else-if="item.elementType==11"></TipCondition>
  <!--12字段下拉选择框-->
  <TipFilterSelectSingle :item="item" :index="index" v-else-if="item.elementType==12"></TipFilterSelectSingle>
  <!--13表结构列表（只读）-->
  <TipShowDataGrid :type="database" :item="item" :index="index"
                   v-else-if="item.elementType==13"></TipShowDataGrid>
  <!--14输入列表展示-->
  <TipShowInputList :nodeItem="nodeItem" :item="item" :index="index"
                    v-else-if="item.elementType==14"></TipShowInputList>
  <!--15 复选框组-->
  <TipCheckBoxGroup :item="item" :index="index" v-else-if="item.elementType==15"></TipCheckBoxGroup>
  <!--16HIVE数据源输入搜索下拉-->
  <TipSearchSelect :item="item" :index="index" :type="hive" v-else-if="item.elementType==16 "></TipSearchSelect>
  <!--17HIVE表结构列表-->
  <TipDataGrid :item="item" :type="hive" :index="index" v-else-if="item.elementType==17"></TipDataGrid>
  <!--18添加字段文本输入框-->
  <TipAddFieldInput :nodeItem="nodeItem" :item="item" :index="index"
                    v-else-if="item.elementType==18"></TipAddFieldInput>
  <!--19修改输出字段名称-->
  <TipUpdateFieldName :nodeItem="nodeItem" :item="item" :index="index"
                      v-else-if="item.elementType==19"></TipUpdateFieldName>
  <!--20密码框-->
  <TipInputText :type="password" :item="item" :index="index" v-else-if="item.elementType==20 "></TipInputText>
  <!--21通过remoteUrl填充下拉选择-->
  <TipFillSelectedByConnection :item="item" :index="index"
                               v-else-if="item.elementType==21 "></TipFillSelectedByConnection>
  <!--22字段下拉多选框-->
  <TipMultipleSelected :nodeItem="nodeItem" :item="item" :index="index"
                       v-else-if="item.elementType==22 "></TipMultipleSelected>
  <!--23可视化组件绘图按钮-->
  <TipDrawingButton :nodeItem="nodeItem" :item="item" :index="index"
                    v-else-if="item.elementType==23 "></TipDrawingButton>
  <!--24可视化字段与条件复合选择组件-->
  <TipFieldConditionSelected :nodeItem="nodeItem" :item="item" :index="index"
                             v-else-if="item.elementType==24 "></TipFieldConditionSelected>
  <!--25修改输出字段类型-->
  <TipUpdateFiledType :nodeItem="nodeItem" :item="item" :index="index"
                      v-else-if="item.elementType==25"></TipUpdateFiledType>
  <!--26自定义级联选择器-->
  <TipDatabaseCascader :nodeItem="nodeItem" :item="item" :index="index"
                       v-else-if="item.elementType==26"></TipDatabaseCascader>
  <!--27衍生变量-->
  <TipDerivedVariable :nodeItem="nodeItem" :item="item" :index="index"
                      v-else-if="item.elementType==27"></TipDerivedVariable>
</template>
<script>
  import TipInputText from "./prop_item/TipInputText.vue"
  import TipInputNumber from "./prop_item/TipInputNumber.vue"
  import TipSelectedSingle from "./prop_item/TipSelectedSingle.vue"
  import TipSpanContent from "./prop_item/TipSpanContent.vue"
  import TipSearchSelect from "./prop_item/TipSearchInput.vue"
  import TipDataGrid from "./prop_item/TipDataGrid.vue"
  import TipShowDataGrid from "./prop_item/TipShowDataGrid.vue"
  import TipTextArea from "./prop_item/TipTextArea.vue"
  import TipRadio from "./prop_item/TipRadio.vue"
  import TipCheckBox from "./prop_item/TipCheckBox.vue"
  import TipCheckBoxGroup from "./prop_item/TipCheckBoxGroup.vue"
  import TipAceEditor from "./prop_item/TipAceEditor.vue"
  import TipCondition from "./prop_item/TipCondition.vue"
  import TipFilterSelectSingle from "./prop_item/TipFilterSelectSingle.vue"
  import TipShowInputList from "./prop_item/TipShowInputList.vue"
  import TipAddFieldInput from "./prop_item/TipAddFieldInput.vue"
  import TipUpdateFieldName from "./prop_item/TipUpdateFieldName.vue"
  import TipFillSelectedByConnection from "./prop_item/TipFillSelectedByConnection.vue"
  import TipMultipleSelected from "./prop_item/TipMultipleSelected.vue"
  import TipDrawingButton from "./prop_item/TipDrawingButton.vue"
  import TipFieldConditionSelected from "./prop_item/TipFieldConditionSelected.vue"
  import TipUpdateFiledType from "./prop_item/TipUpdateFiledType.vue"
  import TipDatabaseCascader from "./prop_item/TipDatabaseCascader.vue"
  import TipDerivedVariable from "./prop_item/TipDerivedVariable.vue"

  export default {
    name: 'PropItem',
    props: ["item", "index", "nodeItem"],
    data: function () {
      return {
        pgsql: "pgsql",
        r: "r",
        python: "python",
        database: "database",
        hive: "hive",
        text: "text",
        password: "password"
      }
    },
    components: {
      TipInputText,
      TipInputNumber,
      TipSelectedSingle,
//      TipSpanContent,
      TipSearchSelect,
      TipDataGrid,
      TipShowDataGrid,
      TipTextArea,
      TipRadio,
      TipCheckBox,
      TipCheckBoxGroup,
      TipAceEditor,
      TipCondition,
      TipFilterSelectSingle,
      TipShowInputList,
      TipAddFieldInput,
      TipUpdateFieldName,
      TipFillSelectedByConnection,
      TipMultipleSelected,
      TipDrawingButton,
      TipFieldConditionSelected,
      TipUpdateFiledType,
      TipDatabaseCascader,
      TipDerivedVariable
    }
  }
</script>
<style scoped>
  .hint_information {
    float: right;
    margin-top: -35px;
    margin-right: 5px;
    height: 13px;
    line-height: 13px;
  }
</style>

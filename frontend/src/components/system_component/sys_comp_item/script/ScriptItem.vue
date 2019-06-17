<template>
  <div class="h100p">
    <editor ref="editor" v-model="scriptObject[curKey]" @init="editorInit();" :lang="mode" theme="chrome"
            height="100%"></editor>
  </div>
</template>
<style>
  .ace_editor {
    width: 100%;
    height: 300px;
  }

  .readonly-highlight {
    background-color: red;
    opacity: 0.2;
    position: absolute;
  }
</style>
<script>
  import ace from 'brace'

  var Range = ace.acequire("./range").Range;
  var editor = null;

  function refresheditor(editor) {
    var session = editor.getSession();
    var ranges = [];
    var starts = [0], ends = [];
    var text = editor.getValue().split("\n");

    ////
    function before(obj, method, wrapper) {
      var orig = obj[method];
      obj[method] = function () {
        var args = Array.prototype.slice.call(arguments);
        return wrapper.call(this, function () {
          return orig.apply(obj, args);
        }, args);
      }
      return obj[method];
    }

    function intersects(range) {
      return editor.getSelectionRange().intersects(range);
    }

    function intersectsRange(newRange) {
      for (var i = 0; i < ranges.length; i++)
        if (newRange.intersects(ranges[i]))
          return true;
      return false;
    }

    function preventReadonly(next, args) {
      for (var i = 0; i < ranges.length; i++) {
        if (intersects(ranges[i])) return;
      }
      next();
    }

    function onEnd(position, lineLength) {
      var row = position["row"], column = position["column"];
      debugger;
      if (row === (lineLength - 1)) {
        return false;
      }
      for (var i = 0; i < ranges.length; i++)
        if (ranges[i].end["row"] == row && ranges[i].end["column"] == column)
          return true;
      return false;
    }

    function outSideRange(position) {
      var row = position["row"], column = position["column"];
      for (var i = 0; i < ranges.length; i++) {
        if (ranges[i].start["row"] < row && ranges[i].end["row"] > row)
          return false;
        if (ranges[i].start["row"] == row && ranges[i].start["column"] < column) {
          if (ranges[i].end["row"] != row || ranges[i].end["column"] > column)
            return false;
        }
        else if (ranges[i].end["row"] == row && ranges[i].end["column"] > column) {
          return false;
        }
      }
      return true;
    }

    ////
    text.forEach(function (line, index) {
      if ((line.indexOf("<editable>") !== -1)) ends.push(index);
      if ((line.indexOf("</editable>") !== -1)) starts.push(index + 1);
    });
    ends.push(text.length);
    for (var i = 0; i < starts.length; i++) {
      ranges.push(new Range(starts[i], 0, ends[i], 0));
    }
    ranges.forEach(function (range) {
      session.addMarker(range, "readonly-highlight");
    });
//        session.setMode("ace/mode/javascript");
    editor.keyBinding.addKeyboardHandler({
      handleKeyboard: function (data, hash, keyString, keyCode, event) {
        if (Math.abs(keyCode) == 13 && onEnd(editor.getCursorPosition(), editor.session.getLength())) {
          return false;
        }
        if (hash === -1 || (keyCode <= 40 && keyCode >= 37)) return false;
        for (var i = 0; i < ranges.length; i++) {
          if (intersects(ranges[i])) {
            return {command: "null", passEvent: false};
          }
        }
      }
    });

    before(editor, 'onPaste', preventReadonly);
    before(editor, 'onCut', preventReadonly);

    for (var i = 0; i < ranges.length; i++) {
      ranges[i].start = session.doc.createAnchor(ranges[i].start);
      ranges[i].end = session.doc.createAnchor(ranges[i].end);
      ranges[i].end.$insertRight = true;
    }

    var old$tryReplace = editor.$tryReplace;
    editor.$tryReplace = function (range, replacement) {
      return intersectsRange(range) ? null : old$tryReplace.apply(this, arguments);
    }
    var session = editor.getSession();
    var oldInsert = session.insert;
    session.insert = function (position, text) {
      return oldInsert.apply(this, [position, outSideRange(position) ? text : ""]);
    }
    var oldRemove = session.remove;
    session.remove = function (range) {
      return intersectsRange(range) ? false : oldRemove.apply(this, arguments);
    }
    var oldMoveText = session.moveText;
    session.moveText = function (fromRange, toPosition, copy) {
      if (intersectsRange(fromRange) || !outSideRange(toPosition)) return fromRange;
      return oldMoveText.apply(this, arguments);
    }
  }

  export default {
    name: 'ScriptItem',
    props: ["curKey", "index", "scriptObject"],
    data: function () {
      return {
        defaultValue: {
          "r": {
            "MAIN": "# <editable>\n" +
            "# 在此处添加算法描述\n" +
            "# </editable>\n" +
            "# conn：             数据库连接\n" +
            "# inputs：           输入数据集合，数据类型：list， 存储组件输入节点对应的数据，\n" +
            "#                    通过输入节点的key获取数据，例如配置的key为“input1”, 那么inputs$input1\n" +
            "#                    即为该节点对应的数据表\n" +
            "# params：           参数集合，数据类型：list， 存储，获取的规则与inputs一致。需要注意的是：\n" +
            "#                    params中参数的值都是字符类型的，需要在代码中进行数据类型转换，比如：\n" +
            "#                    as.integer(params$centers)\n" +
            "# outputs：          存储规则参见inputs\n" +
            "# reportFileName：   算法运行报告文件的存储路径\n" +
            "# 返回值(可选)：     如果函数用于训练模型，则必须返回模型对象\n" +
            "execute <- function(conn, inputs, params, outputs, reportFileName){\n" +
            "#<editable>\n" +
            " \n" +
            "#</editable>\n" +
            "}\n",
            "PREDICT": "# <editable>\n" +
            "# 在此处添加算法描述\n" +
            "# </editable>\n" +
            "# model：            模型\n" +
            "# data：             预测数据集\n" +
            "# 返回值：           DataFrame\n" +
            "doPredict <- function(model, data){\n" +
            "#<editable>\n" +
            " \n" +
            "#</editable>\n" +
            "}\n",
            "EVALUATE": "# <editable>\n" +
            "# 在此处添加算法描述\n" +
            "# </editable>\n" +
            "# conn：             数据库连接\n" +
            "# model：            评估的模型\n" +
            "# inputs:            输入集合\n" +
            "# params:            参数\n" +
            "# outputs:           输出集合\n" +
            "# reportFileName：   评估报告文件的存储路径\n" +
            "evaluate <- function(conn, model, inputs, params, outputs, reportFileName){\n" +
            "#<editable>\n" +
            "  \n" +
            "#</editable>\n" +
            "}\n"
          },
          "python": {
            "MAIN": "# <editable>\n" +
            "# 在此处添加算法描述\n" +
            "# </editable>\n" +
            "# conn：             数据库连接\n" +
            "# inputs：           输入数据集合，数据类型：list， 存储组件输入节点对应的数据，\n" +
            "#                    通过输入节点的key获取数据，例如配置的key为“input1”, 那么inputs$input1\n" +
            "#                    即为该节点对应的数据表\n" +
            "# params：           参数集合，数据类型：list， 存储，获取的规则与inputs一致。需要注意的是：\n" +
            "#                    params中参数的值都是字符类型的，需要在代码中进行数据类型转换，比如：\n" +
            "#                    as.integer(params$centers)\n" +
            "# outputs：          存储规则参见inputs\n" +
            "# reportFileName：   算法运行报告文件的存储路径\n" +
            "# 返回值(可选)：     如果函数用于训练模型，则必须返回模型对象\n" +
            "def execute(conn, inputs, params, outputs, reportFileName):\n" +
            "    #<editable>\n" +
            "    \n" +
            "    #</editable>\n",
            "PREDICT": "# <editable>\n" +
            "# 在此处添加算法描述\n" +
            "# </editable>\n" +
            "# model：            模型\n" +
            "# data：             预测数据集\n" +
            "# 返回值：           DataFrame\n" +
            "def doPredict(model, data):\n" +
            "    #<editable>\n" +
            "    \n" +
            "    #</editable>\n",
            "EVALUATE": "# <editable>\n" +
            "# 在此处添加算法描述\n" +
            "# </editable>\n" +
            "# conn：             数据库连接\n" +
            "# model：            评估的模型\n" +
            "# inputs:            输入集合\n" +
            "# params:            参数\n" +
            "# outputs:           输出集合\n" +
            "# reportFileName：   评估报告文件的存储路径\n" +
            "def evaluate(conn, model, inputs, params, outputs, reportFileName):\n" +
            "    #<editable>\n" +
            "    \n" +
            "    #</editable>\n"
          }
        }
      }
    },
    computed: {
      mode: function () {
        var engine = "r";
        try {
          var lang = this.componentsDeals.editComponent.extra.engine;
          switch (lang) {
            case "R":
            case "SPARK":
              engine = "r"
              break;
            case "PYTHON":
              engine = "python"
              break;
            case "HADOOP":
              engine = "python"
              break;
          }
        } catch (e) {
          console.error(e.message)
        }
        return engine;
      },
      componentsDeals: function () {//编辑状态
        return this.$store.state.systemComponent.componentsDeals;
      }
    },
    watch: {
      mode: function (val) {//新增时检测模式改变
        if (this.componentsDeals.componentEdit == "add") {
          this.setDefaultValue(val);
        }
      }
    },
    components: {
      editor: require('vue2-ace-editor')
    },
    created: function () {
      console.info("created");
      if (this.componentsDeals.componentEdit == "add") {
        this.setDefaultValue(this.mode);
      } else if (this.scriptObject[this.curKey] == "") {
        this.setDefaultValue(this.mode);
      }
    },
    mounted: function () {
      editor = this.$refs.editor.$data.editor;
      refresheditor(editor);
    },
    methods: {
      setDefaultValue: function (mode) {//设置编辑区默认内容
        this.scriptObject[this.curKey] = this.defaultValue[mode][this.curKey];
      },
      editorInit: function () {
        require('brace/mode/r');
        require('brace/mode/python');
        require('brace/theme/chrome');
        require('brace/ext/language_tools');
        require('brace/ext/searchbox')
      }
    }
  }
</script>

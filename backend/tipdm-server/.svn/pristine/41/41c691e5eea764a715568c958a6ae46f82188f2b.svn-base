package com.tipdm.framework.dmserver.core.algo.unparallel.sql;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import com.tipdm.framework.common.utils.StringKit;
import com.tipdm.framework.dmserver.core.algo.unparallel.AbstractAlgorithm;
import com.tipdm.framework.dmserver.exception.AlgorithmException;
import org.slf4j.helpers.MessageFormatter;

import java.util.Map;

/**
 * Created by TipDM on 2017/5/26.
 * E-mail:devp@tipdm.com
 */
public class SQLExecutor extends AbstractAlgorithm {

    @Override
    public void execute() throws AlgorithmException {
//        truncateLog();
        try {
            Map<String, String> params = component.getParameters();
            if(!params.containsKey("SQL")){
                throw new AlgorithmException(MessageFormatter.format("参数缺失，参数名：{},请检查组件配置", "SQL").getMessage());
            }
            String text = params.get("SQL");
            if(StringKit.isBlank(text)){
                throw new AlgorithmException("执行的SQL语句不能为空");
            }
            Template template = Mustache.compiler().escapeHTML(false).compile(text);
            if(!getOutputs().containsKey("output")){
                throw new AlgorithmException(MessageFormatter.format("输出节点{}不存在，请检查组件的输出项配置", "output").getMessage());
            }
            String outputTable = "\"" + component.getCreatorName() + "\".\""+ getOutputs().get("output") + "\"";
            String truncateSQL = "drop table if exists " + outputTable ;
            String querySQL = MessageFormatter.format("create table {} as {}", outputTable, template.execute(getInputs())).getMessage();
            String[] sqls = new String[]{truncateSQL, querySQL};
            tableService.batchExecuteSQL(sqls);
            //将表所有者变更为当前用户
            tableService.changeTableOwner(outputTable, component.getCreatorName());
        }catch (Exception ex){
            throw new AlgorithmException("SQLExecutor运行错误，错误信息：" +ex.getMessage());
        }
    }

}

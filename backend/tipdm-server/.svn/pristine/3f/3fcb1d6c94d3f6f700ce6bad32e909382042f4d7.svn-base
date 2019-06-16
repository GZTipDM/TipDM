package com.tipdm.framework.dmserver.core.algo.unparallel.dataexplore;

import com.tipdm.framework.common.utils.PropertiesUtil;
import com.tipdm.framework.common.utils.RedisUtils;
import com.tipdm.framework.dmserver.core.algo.unparallel.AbstractAlgorithm;
import com.tipdm.framework.dmserver.exception.AlgorithmException;
import com.tipdm.framework.dmserver.utils.Constants;
import facets.overview.DataExplore;

import java.util.LinkedHashMap;
import java.util.Properties;

/**
 * Created by TipDM on 2018/9/21.
 */
public class OverView extends AbstractAlgorithm {

    LinkedHashMap<String, String> dbConfig = PropertiesUtil.getProperties("sysconfig/database.properties");

    @Override
    protected void execute() throws AlgorithmException {

        try {
            String inputTable = getInputs().get("input");

            String url = dbConfig.get("db.url");
            String user = dbConfig.get("db.user");
            String password = dbConfig.get("db.password");

            Properties prop = new Properties();
            prop.put("user", user);
            prop.put("password", password);
            String clientId = component.getClientId().toString();
            DataExplore.analyze(url, prop, inputTable, RedisUtils.get(com.tipdm.framework.dmserver.utils.Constants.REPORT_DIR, String.class) + "/" + clientId + ".html");
            RedisUtils.set("edv_" + clientId, "", 1000L * 60 * 60);
        } catch (Exception ex) {
            logger.error("数据源分析出错, {}", ex.getMessage());
            throw new AlgorithmException("数据源分析出错：" + ex.getMessage());
        }
    }
}

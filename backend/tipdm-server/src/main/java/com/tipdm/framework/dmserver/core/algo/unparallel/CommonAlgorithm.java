package com.tipdm.framework.dmserver.core.algo.unparallel;

import com.tipdm.framework.common.utils.SpringUtils;
import com.tipdm.framework.dmserver.core.algo.unparallel.executor.AlgorithmExecutor;
import com.tipdm.framework.dmserver.core.algo.unparallel.executor.PyExecutor;
import com.tipdm.framework.dmserver.exception.AlgorithmException;
import com.tipdm.framework.dmserver.exception.ConnectionException;
import com.tipdm.framework.dmserver.pyserve.PySession;
import com.tipdm.framework.dmserver.pyserve.PySessionPool;

/**
 * @author tipDM
 * E-mail:devp@tipdm.com
 * @version 创建时间：2016年11月7日 上午10:29:42
 *          单机算法 通用类
 */
@SuppressWarnings("all")
public class CommonAlgorithm extends AbstractAlgorithm {

    private PySessionPool pySessionPool = SpringUtils.getBean("pysessionPool", PySessionPool.class);

    @Override
    protected void execute() throws ConnectionException, AlgorithmException {

        AlgorithmExecutor executor = null;

        PySession session = pySessionPool.getSession();
        executor = new PyExecutor(session, component);
        executor.run();
    }
}

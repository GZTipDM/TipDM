package com.tipdm.framework.dmserver.pyserve;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import edu.cornell.med.icb.io.ResourceFinder;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by TipDM on 2018/1/4.
 * E-mail:devp@tipdm.com
 */
public class PySessionFactory extends BasePooledObjectFactory<PySession> {

    private final static Logger logger = LoggerFactory.getLogger(PySessionFactory.class);
    private XMLConfiguration configuration;
    private int numberOfRServers;
    private static AtomicInteger index = new AtomicInteger(0);

    public PySessionFactory(){
        ResourceFinder resourceFinder = new ResourceFinder(new String[]{".", "sysconfig"});
        URL poolConfigURL = resourceFinder.findResource("PyConnectionPool.xml");
        Assert.notNull(poolConfigURL, "can not found config file: PyConnectionPool.xml");
        try {
            configuration = new XMLConfiguration(poolConfigURL);
        } catch (ConfigurationException e) {

        }
        numberOfRServers = configuration.getMaxIndex("PyConfiguration.PyServer") + 1;
    }

    @Override
    public PySession create() throws Exception {
        PySession pySession = null;
        boolean flag = true;
        int attempts = 3;
        while (flag && attempts > 0) {
            int currentIndex = index.get();
            if (currentIndex >= numberOfRServers) {
                currentIndex = 0;
                index.set(0);
            }

            try {
                String server = "PyConfiguration.PyServer(" + currentIndex + ")";
                String host = configuration.getString(server + "[@host]");
                String sshUser = configuration.getString(server + "[@sshuser]");
                String sshPwd = configuration.getString(server + "[@sshpwd]");
                int sshPort = configuration.getInt(server + "[@sshport]", 22);

                JSch jsch = new JSch();
                Session session = jsch.getSession(sshUser, host, sshPort);
                session.setPassword(sshPwd);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect(1000 * 30);
                pySession = new PySession(session);
                if (null != pySession) {
                    flag = false;
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            } finally {
                index.incrementAndGet();
                attempts--;
            }
        }
        return pySession;
    }

    @Override
    public boolean validateObject(PooledObject<PySession> p) {
        PySession session = p.getObject();
        return session.connected();
    }

    @Override
    public void destroyObject(PooledObject<PySession> p) throws Exception {
        PySession session = p.getObject();
        session.disconnect();
        super.destroyObject(p);
    }

    @Override
    public PooledObject<PySession> wrap(PySession pySession) {
        return new DefaultPooledObject<PySession>(pySession);
    }
}

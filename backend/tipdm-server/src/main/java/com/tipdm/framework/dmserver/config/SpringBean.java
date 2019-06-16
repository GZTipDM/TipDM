package com.tipdm.framework.dmserver.config;

import com.tipdm.framework.common.Constants;
import com.tipdm.framework.common.utils.SpringUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.io.*;
import java.util.Iterator;
import java.util.Properties;

public class SpringBean {
    /**
     * 修改properties文件
     * 重新覆盖注册新bean
     * */
    public static void registerBean(File file) throws Exception {
        BeanDefinitionRegistry bdr = ((BeanDefinitionRegistry) SpringUtils.getConfigurableListableBeanFactory());//获取bean工厂
        InputStream inputStream = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Constants.CHARACTER));//读取文件
        Properties pro = new Properties();
        //redis.properties文件修改时对bean的调整
        if (file.getName().equals("redis.properties")) {
            BeanDefinition dbJF = bdr.getBeanDefinition("jedisFactory");//获取bean
            BeanDefinition dbJPC = bdr.getBeanDefinition("jedisPoolConfig");//获取bean
            MutablePropertyValues mpvJF = dbJF.getPropertyValues();
            MutablePropertyValues mpvJPC = dbJPC.getPropertyValues();

            //读取properties文件
            pro.load(reader);
            Iterator<String> it = pro.stringPropertyNames().iterator();

            while (it.hasNext()) {
                String key = it.next();//取key
                String value = pro.getProperty(key);//取key里面的value,相当于等号后面的值
                String name = key.replace("redis.","");
                if(name.equals("host")){//key与XML里面的属性不一致，要调整
                    name = "hostName";
                }
                if (mpvJF.contains(name)) {
                    mpvJF.addPropertyValue(name, value);//修改存在的key
                }
                if (mpvJPC.contains(name)) {
                    mpvJPC.addPropertyValue(name, value);//修改存在的key
                }
            }
            bdr.registerBeanDefinition("jedisPoolConfig",dbJPC);//重新注册
            bdr.registerBeanDefinition("jedisFactory",dbJF);//重新注册

        } else if (file.getName().equals("bbsBanner.properties") ||
                file.getName().equals("docsBanner.properties") ||
                file.getName().equals("zeppelin.properties") ||
                file.getName().equals("spark.properties")) {
            //没有注册bean不要修改，直接改了文件就生效
        }
        reader.close();//关闭读写
//        applicationContext.refresh();//重新加载
    }

}

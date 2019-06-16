package com.tipdm.framework.common.utils;

/**
 * Created by zhoulong on 2016/12/8.
 * E-mail:zhoulong8513@gmail.com
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class SpringUtils implements BeanFactoryPostProcessor,
        BeanPostProcessor, ApplicationContextAware {

    protected static Logger logger = LoggerFactory.getLogger(SpringUtils.class);

    private static ConfigurableApplicationContext applicationContext = null;

    private static ConfigurableListableBeanFactory beanFactory = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        SpringUtils.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    public static ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(String beanName, Class<T> clazz){
        return applicationContext.getBean(beanName, clazz);
    }
    public static ConfigurableListableBeanFactory getConfigurableListableBeanFactory() {
        return beanFactory;
    }

    @Override
    public void postProcessBeanFactory(
            ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringUtils.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }


    public static void registerBean(String resource) {
        BeanDefinitionRegistry beanDefinitionRegistry = ((BeanDefinitionRegistry)getConfigurableListableBeanFactory());

        XmlBeanDefinitionReader definitionReader = new XmlBeanDefinitionReader(
                beanDefinitionRegistry);

        definitionReader.setResourceLoader(getApplicationContext());
        definitionReader.setEntityResolver(new ResourceEntityResolver(
                getApplicationContext()));

        definitionReader.setValidating(false);
        definitionReader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);

        definitionReader.loadBeanDefinitions(new FileSystemResource(resource));
    }

    public static void unregisterBean(String beanName) {
        if (containsBean(beanName)) {
            BeanDefinitionRegistry beanDefinitionRegistry = ((BeanDefinitionRegistry)getConfigurableListableBeanFactory());
            beanDefinitionRegistry.removeBeanDefinition(beanName);
        }
    }

    public static boolean containsBean(String beanName) {
        return getConfigurableListableBeanFactory().containsBeanDefinition(beanName);
    }
}

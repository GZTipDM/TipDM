package com.tipdm.framework.common.spring;

import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * @author zhoulong E-mail:zhoulong8513@gmail.com
 * @version 创建时间：2016年9月24日 上午9:57:52
 * 类说明
 */
public class ApplicationContextInitializer implements org.springframework.context.ApplicationContextInitializer<XmlWebApplicationContext> {

	@Override
	public void initialize(XmlWebApplicationContext applicationContext) {
		
		applicationContext.setAllowBeanDefinitionOverriding(false);
		
	}

}

/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 
 * @author WindsYan
 * @version $Id: SpringHelper.java, v 0.1 2017年8月8日 下午3:01:20 WindsYan Exp $
 */
@Component
public class SpringHelper implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringHelper.applicationContext = applicationContext;
  }

  @SuppressWarnings("unchecked")
  public static <T> T getBean(String beanName) {
    return (T) SpringHelper.applicationContext.getBean(beanName);
  }
}

package com.lzl.rpc.client.processor;

import com.lzl.core.discovery.DiscoveryService;
import com.lzl.rpc.client.annotation.RpcAutowired;
import com.lzl.rpc.client.config.RpcClientProperties;
import com.lzl.rpc.client.proxy.ClientStubProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class RpcClientProcessor implements
//        BeanFactoryPostProcessor,
        ApplicationContextAware,
        BeanPostProcessor {

    private ClientStubProxyFactory clientStubProxyFactory;

    private DiscoveryService discoveryService;

    private RpcClientProperties properties;

    private ApplicationContext applicationContext;

    public RpcClientProcessor(ClientStubProxyFactory clientStubProxyFactory, DiscoveryService discoveryService, RpcClientProperties properties) {
        this.clientStubProxyFactory = clientStubProxyFactory;
        this.discoveryService = discoveryService;
        this.properties = properties;
    }

//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
//            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
//            String beanClassName = beanDefinition.getBeanClassName();
//            if (beanClassName != null) {
//                Class<?> clazz = ClassUtils.resolveClassName(beanClassName, this.getClass().getClassLoader());
//                ReflectionUtils.doWithFields(clazz, field -> {
//                    RpcAutowired rpcAutowired = AnnotationUtils.getAnnotation(field, RpcAutowired.class);
//                    if (rpcAutowired != null) {
//                        Object bean = applicationContext.getBean(clazz);
//                        field.setAccessible(true);
//                        ReflectionUtils.setField(field, bean, clientStubProxyFactory.getProxy(field.getType(), rpcAutowired.version(), discoveryService, properties));
//                    }
//                });
//            }
//        }
//    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        do {
            for (Field field : clazz.getDeclaredFields()) {
                RpcAutowired annotation = AnnotationUtils.getAnnotation(field, RpcAutowired.class);
                if (annotation != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, bean, clientStubProxyFactory.getProxy(field.getType(), annotation.version(), discoveryService, properties));
                }
            }
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        return bean;
    }
}

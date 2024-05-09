package cn.syx.config.client.config;

import cn.syx.config.client.value.SpringValueProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;

//@Slf4j
public class SyxConfigRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        registerClass(registry, PropertySourcesProcessor.class);
        registerClass(registry, SpringValueProcessor.class);
    }

    private static void registerClass(BeanDefinitionRegistry registry, Class<?> cls) {
        boolean exist = Arrays.stream(registry.getBeanDefinitionNames()).anyMatch(beanName -> cls.getName().equals(beanName));
        if (exist) {
//            log.info("PropertySourcesProcessor is already exist");
            System.out.println(cls.getName() + " is already exist");
            return;
        }

        AbstractBeanDefinition syxPropertyBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(cls).getBeanDefinition();
        registry.registerBeanDefinition(cls.getName(), syxPropertyBeanDefinition);
//        log.info("PropertySourcesProcessor register success");
        System.out.println(cls.getName() + " register success");
    }
}

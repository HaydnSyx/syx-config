package cn.syx.config.client.config;

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
        boolean exist = Arrays.stream(registry.getBeanDefinitionNames()).anyMatch(beanName -> PropertySourcesProcessor.class.getName().equals(beanName));
        if (exist) {
//            log.info("PropertySourcesProcessor is already exist");
            System.out.println("PropertySourcesProcessor is already exist");
            return;
        }

        AbstractBeanDefinition syxPropertyBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(PropertySourcesProcessor.class).getBeanDefinition();
        registry.registerBeanDefinition(PropertySourcesProcessor.class.getName(), syxPropertyBeanDefinition);
//        log.info("PropertySourcesProcessor register success");
        System.out.println("PropertySourcesProcessor register success");
    }
}

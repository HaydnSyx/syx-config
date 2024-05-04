package cn.syx.config.client.config;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.Map;

//@Slf4j
@Data
public class PropertySourcesProcessor implements BeanFactoryPostProcessor, PriorityOrdered, EnvironmentAware {

    private final static String SYX_PROPERTY_SOURCE = "SyxPropertySource";
    private final static String SYX_PROPERTY_SOURCES = "SyxPropertySources";
    private Environment environment;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ConfigurableEnvironment configurableEnvironment = (ConfigurableEnvironment) environment;
        MutablePropertySources propertySources = configurableEnvironment.getPropertySources();

        if (propertySources.contains(SYX_PROPERTY_SOURCES)) {
            return;
        }

        // todo 通过HTTP访问config-server获取配置
        Map<String, String> config = new HashMap<>();
        config.put("syx.a", "a200");
        config.put("syx.b", "b300");
        config.put("syx.c", "c400");
        SyxConfigService configService = new SyxConfigServiceImpl(config);
        SyxPropertySource propertySource = new SyxPropertySource(SYX_PROPERTY_SOURCE, configService);
        CompositePropertySource compositePropertySource = new CompositePropertySource(SYX_PROPERTY_SOURCES);
        compositePropertySource.addPropertySource(propertySource);

        configurableEnvironment.getPropertySources().addFirst(compositePropertySource);
//        log.info("PropertySourcesProcessor postProcessBeanFactory handle success");
        System.out.println("PropertySourcesProcessor postProcessBeanFactory handle success");
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

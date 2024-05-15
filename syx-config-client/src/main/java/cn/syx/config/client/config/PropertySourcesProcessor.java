package cn.syx.config.client.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
public class PropertySourcesProcessor implements BeanFactoryPostProcessor, PriorityOrdered, EnvironmentAware, ApplicationContextAware {

    private final static String SYX_PROPERTY_SOURCE = "SyxPropertySource";
    private final static String SYX_PROPERTY_SOURCES = "SyxPropertySources";
    private Environment environment;
    private ApplicationContext applicationContext;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ConfigurableEnvironment configurableEnvironment = (ConfigurableEnvironment) environment;
        MutablePropertySources propertySources = configurableEnvironment.getPropertySources();

        if (propertySources.contains(SYX_PROPERTY_SOURCES)) {
            return;
        }

        String app = configurableEnvironment.getProperty("syxconfig.app", "app1");
        String env = configurableEnvironment.getProperty("syxconfig.env", "dev");
        String ns = configurableEnvironment.getProperty("syxconfig.ns", "public");
        String configServer = configurableEnvironment.getProperty("syxconfig.configServer", "http://localhost:9129");

        ConfigMeta configMeta = new ConfigMeta(app, env, ns, configServer);

        SyxConfigService configService = SyxConfigService.getDefault(applicationContext, configMeta);
        SyxPropertySource propertySource = new SyxPropertySource(SYX_PROPERTY_SOURCE, configService);
        CompositePropertySource compositePropertySource = new CompositePropertySource(SYX_PROPERTY_SOURCES);
        compositePropertySource.addPropertySource(propertySource);

        configurableEnvironment.getPropertySources().addFirst(compositePropertySource);
        log.info("PropertySourcesProcessor postProcessBeanFactory handle success");
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

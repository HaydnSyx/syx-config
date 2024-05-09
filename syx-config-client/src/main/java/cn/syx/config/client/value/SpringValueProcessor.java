package cn.syx.config.client.value;

import cn.kimmking.utils.FieldUtils;
import cn.syx.config.client.util.PlaceholderHelper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;


@Slf4j
public class SpringValueProcessor implements BeanPostProcessor, BeanFactoryAware, ApplicationListener<EnvironmentChangeEvent> {

    private static final PlaceholderHelper HELPER = PlaceholderHelper.getInstance();

    private static final MultiValueMap<String, SpringValue> VALUE_HOLDER = new LinkedMultiValueMap<>();

    @Setter
    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 第一步
        List<Field> fields = FieldUtils.findAnnotatedField(bean.getClass(), Value.class);
        if (fields.isEmpty()) {
            return bean;
        }

        fields.forEach(e -> {
            log.info("find spring value: {}, field: {}", beanName, e.getName());
            Value value = e.getAnnotation(Value.class);
            Set<String> places = HELPER.extractPlaceholderKeys(value.value());

            places.forEach(p -> {
                log.info("add spring value placeholder: {}", p);
                SpringValue springValue = new SpringValue();
                springValue.setBean(bean);
                springValue.setBeanName(beanName);
                springValue.setField(e);
                springValue.setKey(p);
                springValue.setPlaceholder(value.value());
                VALUE_HOLDER.add(p, springValue);
            });
        });

        return bean;
    }

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        // 第二步
        event.getKeys().forEach(key -> {
            log.info("onApplicationEvent update spring value - key: {}", key);
            List<SpringValue> springValues = VALUE_HOLDER.get(key);

            if (springValues == null || springValues.isEmpty()) {
                return;
            }

            springValues.forEach(value -> {
                log.info("start update spring value - key: {}, bean: {}, field: {}", key, value.getBeanName(), value.getField().getName());
                Object newVal = HELPER.resolvePropertyValue((ConfigurableBeanFactory) beanFactory, value.getBeanName(), value.getPlaceholder());
                Field field = value.getField();
                field.setAccessible(true);
                try {
                    log.info("do update spring value - key: {}, bean: {}, field: {}, newVal: {}", key, value.getBeanName(), value.getField().getName(), newVal);
                    field.set(value.getBean(), newVal);
                } catch (IllegalAccessException e) {
                    log.error("Failed to set field: {}", field, e);
                }
            });
        });
    }
}

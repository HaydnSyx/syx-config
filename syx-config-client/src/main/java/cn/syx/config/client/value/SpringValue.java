package cn.syx.config.client.value;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpringValue {

    private Object bean;
    private String beanName;
    private String key;
    private String placeholder;
    private Field field;
}

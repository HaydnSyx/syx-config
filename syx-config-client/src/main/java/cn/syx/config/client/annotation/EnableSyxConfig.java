package cn.syx.config.client.annotation;

import cn.syx.config.client.config.SyxConfigRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Inherited
@Import(SyxConfigRegistrar.class)
public @interface EnableSyxConfig {

}
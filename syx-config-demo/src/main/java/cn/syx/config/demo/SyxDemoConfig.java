package cn.syx.config.demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "syx")
public class SyxDemoConfig {

    private String a;
    private String b;
    private String c;
    private String d;
}

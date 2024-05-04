package cn.syx.config.demo;

import cn.syx.config.client.annotation.EnableSyxConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


@Slf4j
@EnableSyxConfig
@SpringBootApplication
@EnableConfigurationProperties(SyxDemoConfig.class)
public class SyxConfigDemoApplication {

    @Value("${syx.a}")
    private String a;

    @Autowired
    private SyxDemoConfig syxDemoConfig;

    public static void main(String[] args) {
        SpringApplication.run(SyxConfigDemoApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            log.info("a: {}", a);
            log.info("syxDemoConfig: {}", syxDemoConfig);
        };
    }

}

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@EnableSyxConfig
@SpringBootApplication
@EnableConfigurationProperties(SyxDemoConfig.class)
public class SyxConfigDemoApplication {

    @Value("${syx.a}")
    private String a;
    @Value("${syx.b}")
    private String b;

    @Autowired
    private SyxDemoConfig syxDemoConfig;

    public static void main(String[] args) {
        SpringApplication.run(SyxConfigDemoApplication.class, args);
    }

    @GetMapping("/demo")
    public String demo() {
        return "syx.a = " + a + "\n"
                + "syx.b = " + b + "\n"
                + "demo.a = " + syxDemoConfig.getA() + "\n"
                + "demo.b = " + syxDemoConfig.getB() + "\n";
    }

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            log.info("a: {}", a);
            log.info("syxDemoConfig: {}", syxDemoConfig);
        };
    }
}

package cn.syx.config.client.config;

import cn.syx.config.client.repository.SyxRepository;
import cn.syx.config.client.repository.SyxRepositoryChangeListener;
import cn.syx.config.client.repository.SyxRepositoryImpl;
import org.springframework.context.ApplicationContext;

import java.util.Map;

public interface SyxConfigService extends SyxRepositoryChangeListener {

    static SyxConfigService getDefault(ApplicationContext applicationContext, ConfigMeta meta) {
        SyxRepository repository = new SyxRepositoryImpl(applicationContext, meta);
        Map<String, String> config = repository.getConfig();
        SyxConfigService configService = new SyxConfigServiceImpl(config);
        repository.addListener(configService);
        return configService;
    }

    public String[] getPropertyNames();

    public String getProperty(String name);


}

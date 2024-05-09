package cn.syx.config.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class SyxConfigServiceImpl implements SyxConfigService {

    Map<String, String> config;

    private ApplicationContext applicationContext;

    public SyxConfigServiceImpl(Map<String, String> config, ApplicationContext applicationContext) {
        this.config = config;
        this.applicationContext = applicationContext;
    }

    @Override
    public String[] getPropertyNames() {
        return config.keySet().toArray(new String[0]);
    }

    @Override
    public String getProperty(String name) {
        return config.get(name);
    }

    @Override
    public void onChange(ChangeEvent event) {
        Set<String> keys = calcChangedKeys(this.config, event.config());
        if(keys.isEmpty()) {
            log.info("[SYX_CONFIG] calcChangedKeys return empty, ignore update.");
            return;
        }
        this.config = event.config();
        if(!config.isEmpty()) {
            log.info("[SYX_CONFIG] fire an EnvironmentChangeEvent with keys: {}", keys);
            applicationContext.publishEvent(new EnvironmentChangeEvent(keys));
        }
    }

    private Set<String> calcChangedKeys(Map<String, String> oldConfigs, Map<String, String> newConfigs) {
        if(oldConfigs.isEmpty()) return newConfigs.keySet();
        if(newConfigs.isEmpty()) return oldConfigs.keySet();
        Set<String> news = newConfigs.keySet().stream()
                .filter(key -> !newConfigs.get(key).equals(oldConfigs.get(key)))
                .collect(Collectors.toSet());
        oldConfigs.keySet().stream().filter(key -> !newConfigs.containsKey(key)).forEach(news::add);
        return news;
    }
}

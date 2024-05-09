package cn.syx.config.client.repository;

import cn.kimmking.utils.HttpUtils;
import cn.syx.config.client.config.ConfigMeta;
import cn.syx.config.client.config.Configs;
import com.alibaba.fastjson.TypeReference;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SyxRepositoryImpl implements SyxRepository {

    private final ConfigMeta configMeta;
    private final ApplicationContext applicationContext;
    private Map<String, Long> versionMap = new HashMap<>();
    private Map<String, Map<String, String>> configMap = new HashMap<>();
    private List<SyxRepositoryChangeListener> listeners = new ArrayList<>();

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public SyxRepositoryImpl(ApplicationContext applicationContext, ConfigMeta configMeta) {
        this.configMeta = configMeta;
        this.applicationContext = applicationContext;
        executor.scheduleWithFixedDelay(this::heartbeat, 1000, 5000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void addListener(SyxRepositoryChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public Map<String, String> getConfig() {
        String key = configMeta.genKey();
        if (configMap.containsKey(key)) {
            return configMap.get(key);
        }

        return findAll();
    }

    @NotNull
    private Map<String, String> findAll() {
        String listPath = configMeta.listPath();
        List<Configs> configs = HttpUtils.httpGet(listPath, new TypeReference<List<Configs>>() {
        });

        if (CollectionUtils.isEmpty(configs)) {
            return Collections.emptyMap();
        }

        return configs.stream().collect(
                HashMap::new,
                (m, c) -> m.put(c.getPkey(), c.getPval()),
                (m, u) -> {
                });
    }

    private void heartbeat() {
        String versionPath = configMeta.versionPath();
        Long version = HttpUtils.httpGet(versionPath, Long.class);
        System.out.println("[SYX_CONFIG] heartbeat version=" + version);
        String key = configMeta.genKey();
        Long oldVersion = versionMap.getOrDefault(key, -1L);
        if (version > oldVersion) { // 发生了变化了
            System.out.println("[SYX_CONFIG] current=" + version + ", old=" + oldVersion);
            System.out.println("[SYX_CONFIG] need update new configs.");
            versionMap.put(key, version);
            Map<String, String> newConfigs = findAll();
            configMap.put(key, newConfigs);
            listeners.forEach(l -> l.onChange(new SyxRepositoryChangeListener.ChangeEvent(configMeta, newConfigs)));
//            applicationContext.publishEvent(new EnvironmentChangeEvent(newConfigs.keySet()));
        }

    }
}

package cn.syx.config.client.repository;

import cn.syx.config.client.config.ConfigMeta;

import java.util.Map;

public interface SyxRepositoryChangeListener {

    void onChange(ChangeEvent event);

    record ChangeEvent(ConfigMeta meta, Map<String, String> config) {
    }
}

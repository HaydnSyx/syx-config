package cn.syx.config.client.repository;

import java.util.Map;

public interface SyxRepository {

    void addListener(SyxRepositoryChangeListener listener);

    Map<String, String> getConfig();
}

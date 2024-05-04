package cn.syx.config.client.config;

import java.util.Map;

public class SyxConfigServiceImpl implements SyxConfigService {

    Map<String, String> config;

    public SyxConfigServiceImpl(Map<String, String> config) {
        this.config = config;
    }

    @Override
    public String[] getPropertyNames() {
        return config.keySet().toArray(new String[0]);
    }

    @Override
    public String getProperty(String name) {
        return config.get(name);
    }
}

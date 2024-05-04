package cn.syx.config.client.config;

import org.springframework.core.env.EnumerablePropertySource;

public class SyxPropertySource extends EnumerablePropertySource<SyxConfigService> {

    public SyxPropertySource(String name, SyxConfigService source) {
        super(name, source);
    }

    @Override
    public String[] getPropertyNames() {
        return source.getPropertyNames();
    }

    @Override
    public Object getProperty(String name) {
        return source.getProperty(name);
    }
}

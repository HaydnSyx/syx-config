package cn.syx.config.client.config;

public interface SyxConfigService {

    public String[] getPropertyNames();

    public String getProperty(String name);
}

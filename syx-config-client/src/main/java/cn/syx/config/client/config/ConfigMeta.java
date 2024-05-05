package cn.syx.config.client.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigMeta {

    String app;
    String env;
    String ns;
    String configServer;

    public String genKey() {
        return app + "-" + env + "-" + ns;
    }

    public String listPath() {
        return this.getConfigServer() + "/list?app=" + this.getApp()
                + "&env=" + this.getEnv() + "&ns=" + this.getNs();
    }

    public String versionPath() {
        return this.getConfigServer() + "/version?app=" + this.getApp()
                + "&env=" + this.getEnv() + "&ns=" + this.getNs();
    }
}

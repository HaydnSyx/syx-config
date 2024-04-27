package cn.syx.config.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configs {

    private String app;
    private String env;
    private String ns;
    private String pkey;
    private String pval;

}

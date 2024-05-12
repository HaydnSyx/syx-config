package cn.syx.config.server;

import cn.syx.config.server.dal.ConfigsMapper;
import cn.syx.config.server.model.Configs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class SyxConfigController {

    @Autowired
    private ConfigsMapper mapper;
    @Autowired
    DistributedLocks locks;

    Map<String, Long> VERSIONS = new HashMap<>();

    @GetMapping("/list")
    public List<Configs> list(@RequestParam("app") String app,
                              @RequestParam("env") String env,
                              @RequestParam("ns") String ns) {
        return mapper.list(app, env, ns);
    }

    @PostMapping("/update")
    public List<Configs> update(@RequestParam("app") String app,
                                @RequestParam("env") String env,
                                @RequestParam("ns") String ns,
                                @RequestBody Map<String, String> params) {
        params.forEach((k, v) -> {
            insertOrUpdate(new Configs(app, env, ns, k, v));
        });
        VERSIONS.put(app + "-" + env + "-" + ns, System.currentTimeMillis());
        return mapper.list(app, env, ns);
    }

    private void insertOrUpdate(Configs configs) {
        Configs config = mapper.select(configs);
        if (Objects.isNull(config)) {
            mapper.insert(configs);
        } else {
            mapper.update(configs);
        }
    }

    @GetMapping("/version")
    public long version(@RequestParam("app") String app,
                        @RequestParam("env") String env,
                        @RequestParam("ns") String ns) {
        return VERSIONS.getOrDefault(app + "-" + env + "-" + ns, -1L);
    }

    @GetMapping("/status")
    public boolean status() {
        return locks.getLocked().get();
    }
}

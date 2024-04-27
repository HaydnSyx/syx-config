package cn.syx.config.server.dal;

import cn.syx.config.server.model.Configs;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ConfigsMapper {

    @Select("select * from configs where app = #{app} and env = #{env} and ns = #{ns}")
    List<Configs> list(@Param("app") String app,@Param("env") String env, @Param("ns") String ns);

    @Select("select * from configs where app = #{conf.app} and env = #{conf.env} and ns = #{conf.ns} and pkey = #{conf.pkey}")
    Configs select(@Param("conf") Configs conf);

    @Insert("insert into configs(`app`, `env`, `ns`, `pkey`, `pval`) values (#{conf.app}, #{conf.env}, #{conf.ns}, #{conf.pkey}, #{conf.pval})")
    int insert(@Param("conf") Configs conf);

    @Update("update configs set `pval` = #{conf.pval} where app = #{conf.app} and env = #{conf.env} and ns = #{conf.ns} and pkey = #{conf.pkey}")
    int update(@Param("conf") Configs conf);
}

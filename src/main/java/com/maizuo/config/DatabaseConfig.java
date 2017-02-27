package com.maizuo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author qiyang
 * @className DatabaseConfig
 * @description 数据库配置
 * @email qiyang@maizuo.com
 * @date 2016/9/2 0002 10:59
 */
@Configuration
public class DatabaseConfig {

    /**
     * @return
     * @className DatabaseConfig
     * @method dataSourceTest()
     * @description 测试数据源
     * @author qiyang
     * @email qiyang@maizuo.com
     * @date 2016/9/2 0002 11:03
     */
    @Bean(name = "dsUser")
    @ConfigurationProperties(prefix = "datasource.user")
    @Primary
    public DataSource dataSourceTest() {
        return DataSourceBuilder.create().build();
    }

    /**
     * @param dataSource
     * @return
     * @className DatabaseConfig
     * @method jdbcTemplateTest()
     * @description 测试JdbcTemplate
     * @author qiyang
     * @email qiyang@maizuo.com
     * @date 2016/9/2 0002 11:04
     */
    @Bean(name = "jdbcTemplateUser")
    public JdbcTemplate jdbcTemplateUser(@Qualifier("dsUser") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * @return
     * @className DatabaseConfig
     * @method dataSourceBaochang()
     * @description 包场数据源
     * @author qiyang
     * @email qiyang@maizuo.com
     * @date 2016/9/2 0002 11:04
     */
    @Bean(name = "dsCard")
    @ConfigurationProperties(prefix = "datasource.card")
    public DataSource dataSourceCard() {
        return DataSourceBuilder.create().build();
    }

    /**
     * @param dataSource
     * @return
     * @className DatabaseConfig
     * @method jdbcTemplateBaochang()
     * @description 包场JdbcTemplate
     * @author qiyang
     * @email qiyang@maizuo.com
     * @date 2016/9/2 0002 11:05
     */
    @Bean(name = "jdbcTemplateCard")
    public JdbcTemplate jdbcTemplateCard(@Qualifier("dsCard") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

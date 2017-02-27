package com.maizuo.config;

import com.maizuo.api3.moviebase.client.MovieClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiyang
 * @ClassName: MovieBaseThrift
 * @Description: 基础 RPC服务配置
 * @Email qiyang@maizuo.com
 * @date 2016/8/18 0018
 */
@Configuration
public class RpcConfig {
    /**
     * 影片服务
     *
     * @return
     */
    @Bean(name = "movieClient")
    public MovieClient getOriMovieClient() {
        return new MovieClient();
    }
}

package com.cyb.mongodb.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 若要使用mybatis的分页插件必须在spring中配置
 */
@Configuration
public class MybatisPageConfig {

    @Bean  //让Spring容器进行管理
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor page = new PaginationInterceptor();
        return page;
    }

}

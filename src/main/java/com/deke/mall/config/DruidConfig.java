package com.deke.mall.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
@MapperScan("com.deke.mall.mapper")
public class DruidConfig {

    @Value("${druid.login.username:root}")
    private String userName;

    @Value("${druid.login.password:root}")
    private String password;

    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix="druid.datasource")
    public DataSource druidDataSource(){
        return new DruidDataSource();
    }


    @Bean
    public ServletRegistrationBean druidServlet() {
        log.info("init Druid Servlet Configuration ");
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");
        Map<String, String> initParameters = new HashMap<>();
        // 用户名
        initParameters.put("loginUsername", userName);
        // 密码
        initParameters.put("loginPassword", password);
        // 禁用HTML页面上的“Reset All”功能
        initParameters.put("resetEnable", "false");
        servletRegistrationBean.setInitParameters(initParameters);
        return servletRegistrationBean;
    }


    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico");
        return filterRegistrationBean;
    }

}

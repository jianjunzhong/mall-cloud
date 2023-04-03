package com.jjzhong.mall.cloud.gateway.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.jjzhong.mall.cloud.gateway.service.DynamicRouteRefreshService;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 动态路由配置类
 */
@Slf4j
@Component
@DependsOn("gatewayConfig")
public class DynamicRouteConfig {
    private ConfigService configService;
    @Autowired
    private DynamicRouteRefreshService dynamicRouteRefreshService;

    /**
     * 在 dynamicRouteRefreshService 注入后进行初始化
     */
    @PostConstruct
    public void init() {
        log.info("init gateway route ...");
        try {
            configService = initConfigService();
            if (configService == null) {
                log.error("nacos config service init failed");
                throw new RuntimeException();
            }
            /* 获取 Nacos 中的配置 */
            String config = configService.getConfig(
                    GatewayConfig.NACOS_ROUTE_DATA_ID,
                    GatewayConfig.NACOS_ROUTE_GROUP,
                    GatewayConfig.DEFAULT_TIMEOUT
            );
            log.info("get gateway config: [{}]", config);
            List<RouteDefinition> definitions = JSON.parseArray(config, RouteDefinition.class);

            /* 更新 Gateway 的路由配置 */
            log.info("init gateway config: [{}]", definitions.toString());
            if (!CollectionUtils.isEmpty(definitions))
                dynamicRouteRefreshService.add(definitions);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
        /* 设置监听器 */
        addNacosConfigModifiedListener(GatewayConfig.NACOS_ROUTE_DATA_ID, GatewayConfig.NACOS_ROUTE_GROUP);
    }

    /**
     * 初始化 Nacos 配置服务
     * @return ConfigService
     */
    private ConfigService initConfigService() {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", GatewayConfig.NACOS_SERVER_ADDR);
        properties.setProperty("namespace", GatewayConfig.NACOS_NAMESPACE);
        try {
            return NacosFactory.createConfigService(properties);
        } catch (NacosException e) {
            log.error("nacos config service init error: [{}]", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加配置更改监听器
     * @param dataId Nacos 配置的 dataId
     * @param group Nacos 配置组
     */
    private void addNacosConfigModifiedListener(String dataId, String group) {
        try {
            configService.addListener(dataId, group, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }
                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("config modified, start to update config: [{}]", configInfo);
                    List<RouteDefinition> definitions = JSON.parseArray(configInfo, RouteDefinition.class);
                    dynamicRouteRefreshService.update(definitions);
                    log.info("routed updated: [{}]", definitions.toString());
                }
            });
        } catch (NacosException e) {
            log.info("listener add failed");
            throw new RuntimeException(e);
        }
    }
}

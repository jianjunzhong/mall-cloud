package com.jjzhong.mall.cloud.gateway.service.impl;

import com.jjzhong.mall.cloud.gateway.service.DynamicRouteRefreshService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 动态加载网关路由的实现类
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class DynamicRouteRefreshServiceImpl implements ApplicationEventPublisherAware, DynamicRouteRefreshService {
    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    @Autowired
    private RouteDefinitionLocator routeDefinitionLocator;

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * 增加单个路由
     * @param definition 路由定义
     */
    @Override
    public void add(RouteDefinition definition) {
        try {
            log.info("add route to gateway: [{}]", definition);
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
        } catch (Exception e) {
            log.error("definition add failed: [{}]", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 增加多个路由
     * @param definitions 路由定义列表
     */
    @Override
    public void add(List<RouteDefinition> definitions) {
        try {
            for (RouteDefinition definition : definitions) {
                log.info("add route to gateway: [{}]", definition);
                routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            }
            publisher.publishEvent(new RefreshRoutesEvent(this));
        } catch (Exception e) {
            log.error("definitions add failed: [{}]", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新路由
     * @param definitions 路由列表
     */
    @Override
    public void update(List<RouteDefinition> definitions) {
        try {
            log.info("update routes to gateway: [{}]", definitions);
            /* 获取原有路由 */
            List<RouteDefinition> routeDefinitionsOld = routeDefinitionLocator.getRouteDefinitions().buffer().blockFirst();
            if (!CollectionUtils.isEmpty(routeDefinitionsOld)) {
                /* 删除当前 gateway 中所有的路由配置 */
                routeDefinitionsOld.forEach(routeDefinition -> delete(routeDefinition.getId()));
                /* 设置新的配置 */
                definitions.forEach(routeDefinition -> add(routeDefinition));
            }
            publisher.publishEvent(new RefreshRoutesEvent(this));
        } catch (Exception e) {
            log.error("definitions update failed: [{}]", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除路由
     * @param id 路由 id
     */
    private void delete(String id) {
        try {
            log.info("delete route to gateway: id = {}", id);
            routeDefinitionWriter.delete(Mono.just(id));
            publisher.publishEvent(new RefreshRoutesEvent(this));
        } catch (Exception e) {
            log.error("definition delete failed: [{}]", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

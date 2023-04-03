package com.jjzhong.mall.cloud.gateway.service;

import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.List;

public interface DynamicRouteRefreshService {
    void add(RouteDefinition definition);

    void add(List<RouteDefinition> definitions);

    void update(List<RouteDefinition> definitions);
}

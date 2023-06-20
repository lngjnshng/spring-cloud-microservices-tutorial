package org.rubisemi.micro.gateway.config;

import org.rubisemi.micro.gateway.route.GwRouteLocator;
import org.rubisemi.micro.gateway.service.GwRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class GwRouterConfiguration {

    private final static int LOAD_STATIC = 1;
    private final static int LOAD_DYNAMIC = 2;
    private final static int LOAD_TYPE = LOAD_DYNAMIC;

    @Autowired
    GwRouteService gwRouteService;

    @Bean
    public RouteLocator routeLocator(GwRouteService gwRouteService, RouteLocatorBuilder builder) {
        switch(LOAD_TYPE){
            case LOAD_DYNAMIC:
                return new GwRouteLocator(gwRouteService, builder);
            default:
                return this.loadStaticRoutes(builder);
        }
    }

    private RouteLocator loadStaticRoutes(RouteLocatorBuilder builder){
        return builder.routes()
            .route("FirstRoute", route ->
                route.path("/api/gateway/**")
                    .filters(filter ->  filter.rewritePath("/api/gateway/(?<remaining>.*)", "/api/${remaining}"))
                    .uri("lb://gateway")
            ).route("SecondRoute", route ->
                route.path("/api/v1/order/**")
                    .filters(filter ->  filter.rewritePath("/api/v1/order/(?<remaining>.*)", "/${remaining}"))
                    .uri("lb://order")
            ).build();
    }
}

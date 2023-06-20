package org.rubisemi.micro.gateway.route;

import lombok.extern.slf4j.Slf4j;
import org.rubisemi.micro.gateway.entity.GwRoute;
import org.rubisemi.micro.gateway.service.GwRouteService;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.event.EventListener;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
public class GwRouteLocator implements RouteLocator/*, ApplicationListener<GwRouteRefreshEvent>*/ {

    private final GwRouteService gwRouteService;
    private final RouteLocatorBuilder routeLocatorBuilder;

    public GwRouteLocator(GwRouteService gwRouteService, RouteLocatorBuilder routeLocatorBuilder){
        this.gwRouteService = gwRouteService;
        this.routeLocatorBuilder = routeLocatorBuilder;
        this.loadRoutes();
    }

    private Flux<Route> routes;

    @Override public Flux<Route> getRoutes() {
        return this.routes;
    }

    //@Override
    @EventListener
    public void onApplicationEvent(GwRouteRefreshEvent event) {
        log.info("on refresh route event");
        this.loadRoutes();
    }

    private void loadRoutes(){
        RouteLocatorBuilder.Builder dynamicBuilder = routeLocatorBuilder.routes();
        List<GwRoute> gwRouteList = gwRouteService.getAll();
        for(GwRoute routeData : gwRouteList){
            dynamicBuilder.route(routeData.getId().toString(), route ->
                route.path(routeData.getPath())
                    .filters(filter -> filter.rewritePath(routeData.getRegexp(), routeData.getReplacement()))
                    .uri(routeData.getUri())
            );
        }
        this.routes = dynamicBuilder.build().getRoutes();
    }
}

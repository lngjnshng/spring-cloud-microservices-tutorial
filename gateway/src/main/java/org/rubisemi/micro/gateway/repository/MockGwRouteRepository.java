package org.rubisemi.micro.gateway.repository;

import org.rubisemi.micro.gateway.entity.GwRoute;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MockGwRouteRepository implements IGwRouteRepository{

    private static ConcurrentHashMap<Long, GwRoute> routeMapper;

    static {
        routeMapper = new ConcurrentHashMap<>();
        GwRoute route = new GwRoute();
        route.setId(1L);
        route.setPath("/api/gateway/**");
        route.setUri("lb://gateway");
        route.setRegexp("/api/gateway/(?<remaining>.*)");
        route.setReplacement("/api/${remaining}");
        routeMapper.put(route.getId(), route);
    }

    @Override
    public List<GwRoute> findAll() {
        return routeMapper.values().stream().toList();
    }

    @Override
    public GwRoute save(GwRoute gwRoute) {
        Long max = routeMapper.values().stream().map(route -> route.getId())
            .max( (id1, id2) -> id1.longValue() == id2.longValue() ? 0 : (id1.longValue() > id2.longValue() ? 1 : -1)).get();
        if(max == null) max = 1L;
        gwRoute.setId(max + 1);
        routeMapper.put(gwRoute.getId(), gwRoute);
        return gwRoute;
    }

    @Override public Optional<GwRoute> findById(Long id) {
        return Optional.ofNullable(routeMapper.get(id));
    }

    @Override
    public void deleteById(Long id) {
        if(routeMapper.get(id) != null){
            routeMapper.remove(id);
        }
    }

    @Override
    public GwRoute update(GwRoute gwRoute) {
        if(routeMapper.get(gwRoute.getId()) != null){
            routeMapper.put(gwRoute.getId(), gwRoute);
        }
        return gwRoute;
    }
}

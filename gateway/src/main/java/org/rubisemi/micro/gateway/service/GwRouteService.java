package org.rubisemi.micro.gateway.service;

import org.rubisemi.micro.gateway.entity.GwRoute;
import org.rubisemi.micro.gateway.repository.IGwRouteRepository;
import org.rubisemi.micro.gateway.route.GwRouteRefreshEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GwRouteService {

    @Autowired
    IGwRouteRepository gwRouteRepository;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;


    public List<GwRoute> getAll(){
        return gwRouteRepository.findAll();
    }

    public GwRoute save(GwRoute gwRoute){
        GwRoute saved = gwRouteRepository.save(gwRoute);
        this.notifyRefreshRoutes();
        return saved;
    }

    public GwRoute delete(Long id){
        Optional<GwRoute> find = gwRouteRepository.findById(id);
        if(!find.isPresent()) return null;
        gwRouteRepository.deleteById(id);
        this.notifyRefreshRoutes();
        return find.get();
    }

    public GwRoute update(GwRoute gwRoute){
        GwRoute updated = gwRouteRepository.update(gwRoute);
        this.notifyRefreshRoutes();
        return updated;
    }

    private void notifyRefreshRoutes(){
        this.applicationEventPublisher.publishEvent(new GwRouteRefreshEvent(this));
    }


}

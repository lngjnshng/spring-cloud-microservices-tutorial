package org.rubisemi.micro.gateway.repository;

import org.rubisemi.micro.gateway.entity.GwRoute;

import java.util.List;
import java.util.Optional;

public interface IGwRouteRepository {

    public List<GwRoute> findAll();

    public GwRoute save(GwRoute gwRoute);

    public Optional<GwRoute> findById(Long id);

    public void deleteById(Long id);

    public GwRoute update(GwRoute gwRoute);
}

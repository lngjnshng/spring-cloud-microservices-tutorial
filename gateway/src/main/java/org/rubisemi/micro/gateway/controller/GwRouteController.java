package org.rubisemi.micro.gateway.controller;


import org.rubisemi.micro.gateway.entity.GwRoute;
import org.rubisemi.micro.gateway.service.GwRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/route")
public class GwRouteController {

    @Autowired
    GwRouteService gwRouteService;

    @RequestMapping(value = "", method =  RequestMethod.GET)
    public ResponseEntity<List<GwRoute>> findAll(){
        return new ResponseEntity<>(gwRouteService.getAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "", method =  RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GwRoute> save(@RequestBody GwRoute gwRoute){
        return new ResponseEntity<>(gwRouteService.save(gwRoute), HttpStatus.OK);
    }

    @RequestMapping(value = "", method =  RequestMethod.PUT)
    public ResponseEntity<GwRoute> update(@RequestBody GwRoute gwRoute){
        return new ResponseEntity<>(gwRouteService.update(gwRoute), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method =  RequestMethod.DELETE)
    public ResponseEntity<GwRoute> delete(@PathVariable("id") Long id){
        return new ResponseEntity<>(gwRouteService.delete(id), HttpStatus.OK);
    }
}

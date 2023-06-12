package org.rubisemi.micro.order.client;

import org.rubisemi.micro.invertory.entity.InventoryEntity;
import org.rubisemi.micro.invertory.entity.ReserveRequest;
import org.rubisemi.micro.invertory.entity.ReserveResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "inventory")
public interface InventoryClient {

    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    public InventoryEntity getProductInventory(@PathVariable("productId") Long productId);

    @RequestMapping(value = "/check-n-reserve", method = RequestMethod.POST)
    public ReserveResult checkAndReserve(@RequestBody ReserveRequest request);

    @RequestMapping(value = "/settle-reserve", method = RequestMethod.POST)
    public Boolean settleReserve(@RequestBody ReserveRequest request);

    @RequestMapping(value = "/release-reserve", method = RequestMethod.POST)
    public Boolean rollbackReserve(@RequestBody ReserveRequest request);

}

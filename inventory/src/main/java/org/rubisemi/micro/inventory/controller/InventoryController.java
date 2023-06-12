package org.rubisemi.micro.inventory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.rubisemi.micro.common.AppAuthority;
import org.rubisemi.micro.common.exception.ForbiddenException;
import org.rubisemi.micro.common.exception.ItemNotFoundException;
import org.rubisemi.micro.inventory.service.InventoryService;
import org.rubisemi.micro.invertory.entity.InventoryEntity;
import org.rubisemi.micro.invertory.entity.ReserveRequest;
import org.rubisemi.micro.invertory.entity.ReserveResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "")
@Tag(name = "Inventory Operation API", description = "Create, Update, Delete and Query product inventory")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @Operation(summary = "Get product inventory information",
            description = "Get product inventory information by product id",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Fetch successfully", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Product NOT Found"),
            @ApiResponse(responseCode = "403", description = "Forbidden to access"),
            @ApiResponse(responseCode = "500", description = "Server Error")
    })
    @RequestMapping(value = "/{productId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    @PreAuthorize(AppAuthority.CAN_VIEW_INVENTORY)
    public ResponseEntity<InventoryEntity> get(@PathVariable("productId") long productId, Authentication authentication)
            throws ItemNotFoundException, ForbiddenException
    {
        // Get inventory by product ID, throw NOT_FOUND exception if not exists
        InventoryEntity inventoryEntity = this.inventoryService.getProductInventoryById(productId)
                .orElseThrow(() -> new ItemNotFoundException("Product Not Found"));
        return new ResponseEntity<>(inventoryEntity, HttpStatus.OK);
    }

    @Operation(summary = "Get product inventory information",
            description = "Get product inventory information by product id",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Fetch successfully", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "401", description = "Unauthenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden to access"),
            @ApiResponse(responseCode = "404", description = "Product NOT Found"),
            @ApiResponse(responseCode = "500", description = "Server Error")
    })
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    @PreAuthorize(AppAuthority.CAN_VIEW_INVENTORY)
    public ResponseEntity<List<InventoryEntity>> query(Authentication authentication)
    {
        return new ResponseEntity<>(inventoryService.query(), HttpStatus.OK);
    }

    @Operation(summary = "Check the inventory of order and reserve",
            description = "Check the inventory of order and reserve",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Reserved successfully", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "401", description = "Unauthenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden to access"),
            @ApiResponse(responseCode = "500", description = "Server Error")
    })
    @RequestMapping(value = "/check-n-reserve", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ReserveResult> checkAndReserve(@RequestBody ReserveRequest request, Authentication authentication)
    {
        ReserveResult result = this.inventoryService.checkAndReserve(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/settle-reserve", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Boolean> settleReserve(@RequestBody ReserveRequest request, Authentication authentication)
    {
        Boolean result = this.inventoryService.settleReserve(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/release-reserve", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Boolean> releaseReserve(@RequestBody ReserveRequest request, Authentication authentication)
    {
        Boolean result = this.inventoryService.releaseReserve(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}

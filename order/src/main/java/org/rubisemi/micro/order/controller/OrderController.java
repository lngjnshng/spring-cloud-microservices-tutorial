package org.rubisemi.micro.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.rubisemi.micro.common.AppAuthority;
import org.rubisemi.micro.common.exception.ForbiddenException;
import org.rubisemi.micro.common.exception.ItemNotFoundException;
import org.rubisemi.micro.common.exception.SysErrorException;
import org.rubisemi.micro.order.entity.Order;
import org.rubisemi.micro.order.entity.OrderRequest;
import org.rubisemi.micro.order.exception.InventoryShortageException;
import org.rubisemi.micro.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "")
@Tag(name = "Order Operation API", description = "Create, Update, Delete and Query orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Operation(summary = "Get order",
            description = "Get an order information by order id",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Fetch successfully", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Order NOT Found"),
            @ApiResponse(responseCode = "403", description = "Forbidden to access"),
            @ApiResponse(responseCode = "500", description = "Server Error")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    @PreAuthorize(AppAuthority.CAN_VIEW_ORDER)
    public ResponseEntity<Order> getOrder(@PathVariable("id") long id, Authentication authentication)
            throws ItemNotFoundException, ForbiddenException
    {
        // Print out current username just for study purpose, never do that in production
        log.info("Current logged in user: {}", authentication.getName());
        // Print out current user's authorities
        log.info("Current user roles: {}", this.getAuthorities(authentication));
        // Get order by order ID, throw NOT_FOUND exception if not exists
        Order orderEntity = this.orderService.getOrderById(id)
                .orElseThrow(() -> new ItemNotFoundException("Order Not Found"));
        // Can do your view authorization filter validation according the authentication if necessary
        if(this.canAccess(authentication, orderEntity)) {
            return new ResponseEntity<>(orderEntity, HttpStatus.OK);
        }else{
            throw new ForbiddenException("You don't have authorization to access this order");
        }

    }

    @Operation(summary = "Place an order",
            description = "Place an order with products and amount",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "Place order successfully", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Product NOT Found"),
            @ApiResponse(responseCode = "403", description = "Forbidden to access"),
            @ApiResponse(responseCode = "500", description = "Server Error")
    })
    @RequestMapping(value = "", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest orderRequest, Authentication authentication)
            throws InventoryShortageException, SysErrorException, ItemNotFoundException {
        Order placedOrder = this.orderService.placeOrder(orderRequest);
        return new ResponseEntity<>(placedOrder, HttpStatus.CREATED);
    }

    @Operation(summary = "Query orders",
            description = "Query orders by conditions",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Query order successfully", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "403", description = "Forbidden to access"),
            @ApiResponse(responseCode = "500", description = "Server Error")
    })
    @RequestMapping(value = "", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<Order>> query(@RequestBody OrderRequest orderRequest, Authentication authentication) {
        return new ResponseEntity<>(this.orderService.queryOrder(), HttpStatus.OK);
    }

    /**
     * Get all current user's authorities for print purpose only
     * @param authentication
     * @return
     */
    private String getAuthorities(Authentication authentication){
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
    }

    /**
     * Check whether current user can access a specified order
     * @param auth
     * @param order
     * @return
     */
    private boolean canAccess(Authentication auth, Order order){
        // There are only two roles now CUSTOMER & ADMIN, assuming ADMIN role can view all the orders here
        if(!isCustomer(auth)) return true;
        // Customer can view the orders owned by himself/herself
        if(auth.getName().equals(order.getOwner())) return true;
        return false;
    }

    /**
     * Check whether current user has customer role
     * @param auth
     * @return
     */
    private boolean isCustomer(Authentication auth){
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(AppAuthority::hasRoleCustomer);
    }


}

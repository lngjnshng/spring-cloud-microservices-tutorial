package org.rubisemi.micro.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.rubisemi.common.AppAuthority;
import org.rubisemi.micro.order.entity.OrderEntity;
import org.rubisemi.micro.order.exception.ForbiddenException;
import org.rubisemi.micro.order.exception.OrderNotFoundException;
import org.rubisemi.micro.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<OrderEntity> get(@PathVariable("id") long id, Authentication authentication)
            throws OrderNotFoundException, ForbiddenException
    {
        // Print out current username just for study purpose, never do that in production
        log.info("Current logged in user: {}", authentication.getName());
        // Print out current user's authorities
        log.info("Current user roles: {}", this.getAuthorities(authentication));
        // Get order by order ID, throw NOT_FOUND exception if not exists
        OrderEntity orderEntity = this.orderService.getOrderById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order Not Found"));
        // Can do your view authorization filter validation according the authentication if necessary
        if(this.canAccess(authentication, orderEntity)) {
            return new ResponseEntity<>(orderEntity, HttpStatus.OK);
        }else{
            throw new ForbiddenException("You don't have authorization to access this order");
        }

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
    private boolean canAccess(Authentication auth, OrderEntity order){
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

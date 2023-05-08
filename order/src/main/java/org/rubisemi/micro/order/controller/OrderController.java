package org.rubisemi.micro.order.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.rubisemi.micro.order.entity.OrderEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "")
@Tag(name = "Order Operation API", description = "Create, Update, Delete and Query orders")
public class OrderController {

    @Operation(summary = "Get order", description = "Get an order information by order id")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Fetch successfully", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Order NOT Found"),
            @ApiResponse(responseCode = "500", description = "Server Error")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<OrderEntity> get(@PathVariable("id") long id){
        if(id == 1){
            return new ResponseEntity<>(mock(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private OrderEntity mock(){
        OrderEntity entity = new OrderEntity();
        entity.setId(1);
        entity.setAmount(1D);
        entity.setTotalPrice(102D);
        entity.setCreatedAt(new Date());
        return entity;
    }
}

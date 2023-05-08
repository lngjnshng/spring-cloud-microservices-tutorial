package org.rubisemi.micro.order.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {
    @Schema(description = "Order ID", name = "id", example = "1")
    private long id;
    @Schema(description = "Amount of products", name = "amount", example = "2.0")
    private Double amount;
    @Schema(description = "Total price of the order", name = "totalPrice", example = "2.0")
    private Double totalPrice;
    @Schema(description = "Datetime of the order created", name = "createdAt", example = "2023-07-08 13:12:00")
    private Date createdAt;
}

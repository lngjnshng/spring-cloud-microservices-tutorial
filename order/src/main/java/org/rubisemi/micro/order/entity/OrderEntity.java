package org.rubisemi.micro.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @Schema(description = "The order belong to", name = "owner", example = "jason")
    private String owner;
    @Schema(description = "Datetime of the order created", name = "createdAt", example = "2023-07-08 13:12:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
}

package org.rubisemi.micro.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {

    private Long productId;
    private Double amount;
    private Double price;
}

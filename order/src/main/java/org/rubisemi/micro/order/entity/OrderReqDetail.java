package org.rubisemi.micro.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderReqDetail {
    private Long productId;
    private Double amount;
}

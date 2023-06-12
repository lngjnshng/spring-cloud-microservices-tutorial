package org.rubisemi.micro.invertory.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReserveDetail {
    private Long productId;
    private Double reserveAmount;
}

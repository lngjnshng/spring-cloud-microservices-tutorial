package org.rubisemi.micro.invertory.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReserveResult {

    public static final int SUCCESS = 200;
    public static final int NOT_FOUND = 400;
    public static final int NOT_SUFFICIENT = 406;

    private int code;
    private String message;
}

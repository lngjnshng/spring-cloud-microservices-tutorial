package org.rubisemi.micro.order.exception;

import java.util.function.Supplier;

public class OrderNotFoundException extends Exception  {

    public OrderNotFoundException(String message){
        super(message);
    }
}

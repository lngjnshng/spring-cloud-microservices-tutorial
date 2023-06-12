package org.rubisemi.micro.inventory.exception.handler;

import org.rubisemi.micro.common.entity.ErrorEntity;
import org.rubisemi.micro.common.exception.ForbiddenException;
import org.rubisemi.micro.common.exception.ItemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorEntity> handleForbiddenException(Exception ex){
        ErrorEntity errorEntity = new ErrorEntity();
        errorEntity.setCode(String.format("%s",HttpStatus.FORBIDDEN.value()));
        errorEntity.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorEntity, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorEntity> handleOrderNotFoundException(Exception ex){
        ErrorEntity errorEntity = new ErrorEntity();
        errorEntity.setCode(String.format("%s",HttpStatus.NOT_FOUND.value()));
        errorEntity.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorEntity, HttpStatus.NOT_FOUND);
    }

}

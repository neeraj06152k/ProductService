package dev.neeraj.productservice.controllerAdvice;

import dev.neeraj.productservice.dtos.ErrorDTO;
import dev.neeraj.productservice.exceptions.ProductCreationFailedException;
import dev.neeraj.productservice.exceptions.ProductNotFoundException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;


@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ProductCreationFailedException.class)
    public ResponseEntity<ErrorDTO> handleProductCreationFailedException(ProductCreationFailedException e){
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMsg(e.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleProductCreationFailedException(ProductNotFoundException e){
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMsg(e.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }
}

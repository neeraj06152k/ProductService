package dev.neeraj.productservice.controllerAdvice;

import dev.neeraj.productservice.dtos.ErrorDTO;
import dev.neeraj.productservice.exceptions.ProductCreationFailedException;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.function.EntityResponse;

@Configuration
public class ApplicationConfiguration {

    @ExceptionHandler(ProductCreationFailedException.class)
    public EntityResponse<ErrorDTO> handleProductCreationFailedException(ProductCreationFailedException e){
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMsg(e.getMessage());
        return new         
    }


}

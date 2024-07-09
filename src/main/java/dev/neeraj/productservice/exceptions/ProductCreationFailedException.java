package dev.neeraj.productservice.exceptions;

public class ProductCreationFailedException extends Exception{

    public ProductCreationFailedException(String msg){
        super(msg);
    }
}

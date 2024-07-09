package dev.neeraj.productservice.exceptions;

public class ProductNotFoundException extends Exception{

    public ProductNotFoundException(String msg){
        super(msg);
    }
}

package com.example.withoutjein.Shopping.exception;

public class OutOfStockException extends RuntimeException{

    public OutOfStockException(String message){
        super(message);
    }
}

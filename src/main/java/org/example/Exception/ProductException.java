package org.example.Exception;

public class ProductException extends Exception {
    Integer statusCode;
    public ProductException(String message){

        super(message);

    }

    public ProductException(String message, int status){
        super(message);
        statusCode = status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

}

package org.zew.donations.controller.exception;

public class RevenueAlreadyExistsException extends Exception {
    public RevenueAlreadyExistsException(){
        super(errorMessage());
    }

    public static String errorMessage(){
        return "Revenue already exists" ;
    }
}

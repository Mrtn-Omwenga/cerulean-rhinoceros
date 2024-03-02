package org.zew.donations.controller.exception;

public class RevenueAlreadyExistsException extends Exception {
    public RevenueAlreadyExistsException(){
        super("Revenue already exists");
    }
}

package com.sendwithus.exception;

public class SendWithUsException extends Exception {

    public SendWithUsException(String message) {
        super(message, null);
    }

    public SendWithUsException(String message, Throwable e) {
        super(message, e);
    }
}
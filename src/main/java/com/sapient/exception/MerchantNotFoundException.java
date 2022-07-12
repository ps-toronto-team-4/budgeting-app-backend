package com.sapient.exception;

public class MerchantNotFoundException extends Exception {
    public MerchantNotFoundException() {
        super();
    }

    public MerchantNotFoundException(String message) {
        super(message);
    }
}

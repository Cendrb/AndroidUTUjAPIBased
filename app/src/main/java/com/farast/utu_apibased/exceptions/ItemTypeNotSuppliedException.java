package com.farast.utu_apibased.exceptions;

/**
 * Created by cendr on 14/02/2017.
 */

public class ItemTypeNotSuppliedException extends RuntimeException {
    public ItemTypeNotSuppliedException(Throwable cause) {
        super(cause);
    }

    public ItemTypeNotSuppliedException(String message) {
        super(message);
    }
}

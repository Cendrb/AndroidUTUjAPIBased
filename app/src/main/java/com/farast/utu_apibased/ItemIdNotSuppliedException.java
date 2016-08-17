package com.farast.utu_apibased;

/**
 * Created by cendr_000 on 14.08.2016.
 */
public class ItemIdNotSuppliedException extends RuntimeException {
    public ItemIdNotSuppliedException(Throwable cause) {
        super(cause);
    }

    public ItemIdNotSuppliedException(String message) {
        super(message);
    }
}

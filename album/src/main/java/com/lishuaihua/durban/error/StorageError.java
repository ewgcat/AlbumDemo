package com.lishuaihua.durban.error;


public class StorageError extends Exception {
    
    public StorageError(String message) {
        super(message);
    }

    public StorageError(Throwable cause) {
        super(cause);
    }
}

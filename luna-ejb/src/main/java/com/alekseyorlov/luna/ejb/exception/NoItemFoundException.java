package com.alekseyorlov.luna.ejb.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class NoItemFoundException extends Exception {

    public NoItemFoundException(Throwable cause) {
        super(cause);
    }
    
}

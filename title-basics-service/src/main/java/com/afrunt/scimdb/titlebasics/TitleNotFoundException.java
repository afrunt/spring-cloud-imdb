package com.afrunt.scimdb.titlebasics;

/**
 * @author Andrii Frunt
 */
public class TitleNotFoundException extends RuntimeException {
    public TitleNotFoundException() {
    }

    public TitleNotFoundException(String message) {
        super(message);
    }

    public TitleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TitleNotFoundException(Throwable cause) {
        super(cause);
    }

    public TitleNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

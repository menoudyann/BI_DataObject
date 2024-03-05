package com.ymd;

public class NotEmptyObjectException extends DataObjectImplException {

    public NotEmptyObjectException() {
        super("Oject is not empty.");
    }
}

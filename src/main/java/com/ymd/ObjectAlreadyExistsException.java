package com.ymd;

public class ObjectAlreadyExistsException extends DataObjectImplException {
    public ObjectAlreadyExistsException() {
        super("Object already exists.");
    }
}

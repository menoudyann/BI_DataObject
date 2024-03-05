package com.ymd;

public class ObjectNotFoundException extends DataObjectImplException {
    public ObjectNotFoundException() {
        super("Object not found.");
    }
}

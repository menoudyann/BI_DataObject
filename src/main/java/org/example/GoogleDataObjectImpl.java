package org.example;

import java.io.File;
import java.net.URL;

public class GoogleDataObjectImpl implements IDataObject{

    @Override
    public void uploadObject(byte[] file, String remoteFullPath) {
    }

    @Override
    public File downloadObject(byte[] file, String localFullPath) {
        return null;
    }

    @Override
    public URL publishObject(String remoteFullPath, int expirationTime) {
        return null;
    }

    @Override
    public void removeObject(String remoteFullPath, boolean isRecursive) {

    }
}

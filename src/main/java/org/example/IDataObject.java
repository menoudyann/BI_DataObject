package org.example;

import java.io.File;
import java.net.URL;

public interface IDataObject {
    public void uploadObject(byte[] file, String remoteFullPath);
    public File downloadObject(byte[] file, String localFullPath);
    public URL publishObject(String remoteFullPath, int expirationTime);
    public void removeObject(String remoteFullPath, boolean isRecursive);
}

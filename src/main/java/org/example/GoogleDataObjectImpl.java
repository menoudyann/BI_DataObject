package org.example;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

public class GoogleDataObjectImpl implements IDataObject {

    private Storage storage;

    public GoogleDataObjectImpl(String credentialPathname) {
        Dotenv dotenv = Dotenv.load();

        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(dotenv.get(credentialPathname))).createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
            storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean doesExist(URI remoteFullPath) {
        return false;
    }

    @Override
    public void upload(URI localFullPath, URI remoteFullPath) throws IOException {
        String bucketName = remoteFullPath.getHost();
        String objectName = localFullPath.getPath().substring(localFullPath.getPath().lastIndexOf('/') + 1);

        File file = Paths.get(localFullPath).toFile();
        if (!file.exists()) {
            System.out.println("File not found");
        }

        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.createFrom(blobInfo, Paths.get(localFullPath));
    }

    @Override
    public void download(URI localFullPath, URI remoteFullPath) {

    }

    @Override
    public URL publish(String remoteFullPath, int expirationTime) {
        return null;
    }

    @Override
    public void remove(String remoteFullPath, boolean isRecursive) {

    }
}

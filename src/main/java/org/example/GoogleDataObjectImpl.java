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

    private boolean isBucket(URI remoteFullPath) {
        return remoteFullPath.getPath().equals("/");
    }

    @Override
    public boolean doesExist(URI remoteFullPath) {

        boolean result = false;

        if (isBucket(remoteFullPath)) {
            Bucket bucket = storage.get(remoteFullPath.getHost());
            if (bucket != null) {
                result = bucket.exists();
            }
        } else {
            BlobId blobId = BlobId.of(remoteFullPath.getHost(), remoteFullPath.getPath());
            Blob blob = storage.get(blobId);
            if (blob != null) {
                result = blob.exists();
            }
        }
        return result;
    }

    @Override
    public void upload(URI localFullPath, URI remoteFullPath) throws IOException {

//        // Get bucket name and object name
//        String bucketName = remoteFullPath.getHost();
//        String objectName = localFullPath.getPath().substring(localFullPath.getPath().lastIndexOf('/') + 1);
//
//        // Check if file exists
//        File file = Paths.get(localFullPath).toFile();
//        if (!file.exists()) {
//            System.out.println("File not found");
//        }
//
//        // Upload file
//        BlobId blobId = BlobId.of(bucketName, objectName);
//        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
//        storage.createFrom(blobInfo, Paths.get(localFullPath));
    }

    @Override
    public void download(URI localFullPath, URI remoteFullPath) {

//        // The ID of your GCS bucket
//        String bucketName = remoteFullPath.getHost();
//
//        // The ID of your GCS object
//        // String objectName = "your-object-name";
//
//        // The path to which the file should be downloaded
//        // String destFilePath = "/local/path/to/file.txt";
//
//        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
//
//        Blob blob = storage.get(BlobId.of(bucketName, objectName));
//        blob.downloadTo(Paths.get(destFilePath));
//
//        System.out.println(
//                "Downloaded object "
//                        + objectName
//                        + " from bucket name "
//                        + bucketName
//                        + " to "
//                        + destFilePath);
//    }


    }

    @Override
    public URL publish(String remoteFullPath, int expirationTime) {
        return null;
    }

    @Override
    public void remove(String remoteFullPath, boolean isRecursive) {

    }
}

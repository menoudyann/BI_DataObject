package org.example;

import com.google.api.services.storage.model.GoogleLongrunningListOperationsResponse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws ObjectNotFoundException, ObjectAlreadyExistsException, NotEmptyObjectException, IOException {

        GoogleDataObjectImpl googleDataObject = new GoogleDataObjectImpl("GOOGLE_APPLICATION_CREDENTIALS");

        //googleDataObject.upload(URI.create("file:///Users/yannmenoud/Downloads/test.jpg"), URI.create("gs://java.gogle.cld.education/testRemove.jpg"));

        //System.out.println(googleDataObject.doesExist(URI.create("gs://java.gogle.cld.education/test.png")));

        //googleDataObject.remove(URI.create("gs://java.gogle.cld.education/code.jpg"), false);
        //googleDataObject.download(URI.create("file:///Users/yannmenoud/Downloads/testDownload.png"), URI.create("gs://java.gogle.cld.education/test.png"));
    }
}
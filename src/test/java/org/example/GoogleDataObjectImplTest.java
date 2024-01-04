package org.example;

import junit.framework.TestCase;

import java.io.IOException;
import java.net.URI;

public class GoogleDataObjectImplTest extends TestCase {

    private GoogleDataObjectImpl dataObject;


    public void setUp() throws Exception {
        super.setUp();
        this.dataObject = new GoogleDataObjectImpl("GOOGLE_APPLICATION_CREDENTIALS");
    }

    public void testDoesExist_ExistingBucket_BucketExists() {

        URI bucketUri = URI.create("gs://java.gogle.cld.education/");
        //given
        //The bucket is always available

        //when

        //then
        assertTrue(this.dataObject.doesExist(bucketUri));
    }

    public void testDoesExist_ExistingObject_ObjectExists() throws IOException {
        URI objectUri = URI.create("gs://java.gogle.cld.education/test.png");

        //given
        //The bucket is always available
        this.dataObject.upload(URI.create("file:///Users/yannmenoud/Downloads/test.png"), objectUri);

        //when
        //check the assertion

        //then
        assertTrue(this.dataObject.doesExist(objectUri));
    }

    public void testDoesExist_MissingObject_ObjectNotExists() {
        URI objectUri = URI.create("gs://java.gogle.cld.education/testnexistepas.png");
        //given
        //The bucket is always available
        //The bucket is empty (or does not contain the expected object)

        //when
        //check the assertion

        //then
        assertFalse(this.dataObject.doesExist(objectUri));
    }

}
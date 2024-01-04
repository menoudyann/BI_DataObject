package org.example;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;

import static org.junit.Assert.assertThrows;

public class GoogleDataObjectImplTest extends TestCase {

    private GoogleDataObjectImpl dataObject;

    public void setUp() throws Exception {
        super.setUp();
        this.dataObject = new GoogleDataObjectImpl("GOOGLE_APPLICATION_CREDENTIALS");
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Tests for doesExist method
    // -----------------------------------------------------------------------------------------------------------------
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

    // -----------------------------------------------------------------------------------------------------------------
    // Tests for upload method
    // -----------------------------------------------------------------------------------------------------------------
    public void testUpload_BucketAndLocalFileAreAvailable_NewObjectCreatedOnBucket() throws IOException {
        URI bucketUri = URI.create("gs://java.gogle.cld.education/");
        URI objectUri = URI.create("gs://java.gogle.cld.education/code.jpg");
        URI localFile = URI.create("file:///Users/yannmenoud/Downloads/code.jpg");

        //given
        assertTrue(this.dataObject.doesExist(bucketUri));
        assertFalse(this.dataObject.doesExist(objectUri));

        //when
        this.dataObject.upload(localFile, objectUri);

        //then
        assertTrue(this.dataObject.doesExist(objectUri));
        this.dataObject.remove(objectUri, false);
    }


    // -----------------------------------------------------------------------------------------------------------------
    // Tests for download method
    // -----------------------------------------------------------------------------------------------------------------
    public void testDownload_ObjectAndLocalPathAvailable_ObjectDownloaded() throws ObjectNotFoundException {
        URI objectUri = URI.create("gs://java.gogle.cld.education/test.png");
        URI localFile = URI.create("file:///Users/yannmenoud/Downloads/testDownload.jpg");

        //given
        assertTrue(this.dataObject.doesExist(objectUri));
        assertFalse(Paths.get(localFile).toFile().exists());

        //when
        this.dataObject.download(localFile, objectUri);

        //then
        assertTrue(Paths.get(localFile).toFile().exists());

        Paths.get(localFile).toFile().delete();
    }

    public void testDownload_ObjectMissing_ThrowException() {
        URI objectUri = URI.create("gs://java.gogle.cld.education/dontexists.png");
        URI localFile = URI.create("file:///Users/yannmenoud/Downloads/testDownload.jpg");

        //given
        assertFalse(this.dataObject.doesExist(objectUri));
        assertFalse(Paths.get(localFile).toFile().exists());

        //when
        assertThrows(ObjectNotFoundException.class, () -> this.dataObject.download(localFile, objectUri));

        //then
        //Exception thrown
    }


}
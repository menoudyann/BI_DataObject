package org.example;

import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
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
        this.dataObject.upload(URI.create("file:///Users/yannmenoud/Desktop/CPNV/BI/DataObject/src/test/java/org/example/images/test.png"), objectUri);

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
        URI localFile = URI.create("file:///Users/yannmenoud/Desktop/CPNV/BI/DataObject/src/test/java/org/example/images/code.jpg");

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
        URI localFile = URI.create("file:///Users/yannmenoud/Desktop/CPNV/BI/DataObject/src/test/java/org/example/images/testDownload.jpg");

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
        URI localFile = URI.create("file:///Users/yannmenoud/Desktop/CPNV/BI/DataObject/src/test/java/org/example/images/testDownload.jpg");


        //given
        assertFalse(this.dataObject.doesExist(objectUri));
        assertFalse(Paths.get(localFile).toFile().exists());

        //when
        assertThrows(ObjectNotFoundException.class, () -> this.dataObject.download(localFile, objectUri));

        //then
        //Exception thrown
    }


    // -----------------------------------------------------------------------------------------------------------------
    // Tests for publish method
    // -----------------------------------------------------------------------------------------------------------------
    public void testPublish_ObjectExists_PublicUrlCreated() throws IOException, ObjectNotFoundException {

        URI objectUri = URI.create("gs://java.gogle.cld.education/test.png");
        URI destinationFolder = URI.create("file:///Users/yannmenoud/Desktop/CPNV/BI/DataObject/src/test/java/org/example/images/");
        URI localFile = URI.create("file:///Users/yannmenoud/Desktop/CPNV/BI/DataObject/src/test/java/org/example/images/testPublish.jpg");

        //given
        assertTrue(this.dataObject.doesExist(objectUri));
        assertTrue(Paths.get(destinationFolder).toFile().exists());

        //when
        URL presignedUrl = this.dataObject.publish(objectUri, 10);
        //TODO download file using wget or another method that do not need our project library
        FileUtils.copyURLToFile(presignedUrl, new File(destinationFolder.getPath() + "testPublish.jpg"));

        //then
        assertTrue(Paths.get(localFile).toFile().exists());
    }

    public void testPublish_ObjectMissing_ThrowException() {

        URI objectUri = URI.create("gs://java.gogle.cld.education/nexistepas.png");

        //given
        assertFalse(this.dataObject.doesExist(objectUri));

        //when
        assertThrows(ObjectNotFoundException.class, () -> this.dataObject.publish(objectUri, 10));

        //then
        //Exception thrown
    }


    // -----------------------------------------------------------------------------------------------------------------
    // Tests for remove method
    // -----------------------------------------------------------------------------------------------------------------
    public void testRemove_ObjectPresentNoFolder_ObjectRemoved() throws IOException {

        URI objectUri = URI.create("gs://java.gogle.cld.education/testRemove.jpg");
        URI localFile = URI.create("file:///Users/yannmenoud/Desktop/CPNV/BI/DataObject/src/test/java/org/example/images/team.jpg");
        this.dataObject.upload(localFile, objectUri);

        //given
        assertTrue(this.dataObject.doesExist(objectUri));

        //when
        this.dataObject.remove(objectUri, false);

        //then
        assertFalse(this.dataObject.doesExist(objectUri));
    }

    public void testRemove_ObjectAndFolderPresent_ObjectRemoved() throws IOException {

        URI bucketUri = URI.create("gs://java.gogle.cld.education/");
        URI objectUri = URI.create("gs://java.gogle.cld.education/testRemove.jpg");
        URI localFile = URI.create("file:///Users/yannmenoud/Desktop/CPNV/BI/DataObject/src/test/java/org/example/images/testRemove.jpg");
        URI objectUriWithSubFolder = URI.create("gs://java.gogle.cld.education/testRemove.jpg");

        this.dataObject.upload(localFile, objectUri);

        //given
        //The bucket contains object at the root level as well as in subfolders
        //Sample: mybucket.com/myobject     //myObject is a folder
        //        mybucket.com/myobject/myObjectInSubfolder
        assertTrue(this.dataObject.doesExist(objectUri));
        assertTrue(this.dataObject.doesExist(objectUriWithSubFolder));

        //when
        this.dataObject.remove(objectUri, true);

        //then
        assertFalse(this.dataObject.doesExist(objectUri));
    }
}
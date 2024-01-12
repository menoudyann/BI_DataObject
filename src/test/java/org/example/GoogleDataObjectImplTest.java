package org.example;

import io.github.cdimascio.dotenv.Dotenv;
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
    private Dotenv dotenv;

    public void setUp() throws Exception {
        super.setUp();
        dotenv = Dotenv.load();
        this.dataObject = new GoogleDataObjectImpl("GOOGLE_APPLICATION_CREDENTIALS");
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Tests for doesExist method
    // -----------------------------------------------------------------------------------------------------------------
    public void testDoesExist_ExistingBucket_BucketExists() {
        URI bucketUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI"));
        //given
        //The bucket is always available

        //when

        //then
        assertTrue(this.dataObject.doesExist(bucketUri));
    }

    public void testDoesExist_ExistingObject_ObjectExists() throws IOException {
        URI objectUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI") + "fileToTest.png");

        //given
        //The bucket is always available
        URI localUri = new File("images/test.png").toURI();
        this.dataObject.upload(localUri, objectUri);

        //when
        //check the assertion

        //then

        assertTrue(this.dataObject.doesExist(objectUri));
        this.dataObject.remove(objectUri, false);
    }

    public void testDoesExist_MissingObject_ObjectNotExists() {
        URI objectUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI") + "fileToTest.png");
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
      
        URI bucketUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI"));
        URI objectUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI") + "fileToTest.jpg");
        URI localFile = new File("images/test.png").toURI();

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

    public void testDownload_ObjectAndLocalPathAvailable_ObjectDownloaded() throws ObjectNotFoundException, IOException {
      
        URI objectUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI") + "fileToTest.png");
        URI fileToUpload = new File("images/test.png").toURI();
        URI destinationFile = new File("images/downloaded.png").toURI();
        File file = Paths.get(destinationFile).toFile();

        //given
        this.dataObject.upload(fileToUpload, objectUri);
        assertTrue(this.dataObject.doesExist(objectUri));
        assertFalse(file.exists());

        //when
        this.dataObject.download(destinationFile, objectUri);

        //then
        assertTrue(file.exists());

        file.delete();
        this.dataObject.remove(objectUri, false);
    }

    public void testDownload_ObjectMissing_ThrowException() {
        URI objectUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI") + "fileToTest.png");
        URI localFile = new File("images/dontExist.png").toURI();
        File file = Paths.get(localFile).toFile();

        //given
        assertFalse(this.dataObject.doesExist(objectUri));
        assertFalse(file.exists());

        //when
        assertThrows(ObjectNotFoundException.class, () -> this.dataObject.download(localFile, objectUri));

        //then
        //Exception thrown
    }


    // -----------------------------------------------------------------------------------------------------------------
    // Tests for publish method
    // -----------------------------------------------------------------------------------------------------------------
    public void testPublish_ObjectExists_PublicUrlCreated() throws IOException, ObjectNotFoundException {
        URI objectUriLocal = new File("images/test.png").toURI();
        URI objectUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI") + "fileToTest.png");
        this.dataObject.upload(objectUriLocal, objectUri);

        URI destinationFolder = new File("images/").toURI();
        File destinationFolderFile = Paths.get(destinationFolder).toFile();

        //given
        assertTrue(this.dataObject.doesExist(objectUri));
        assertTrue(destinationFolderFile.exists());

        //when
        URL presignedUrl = this.dataObject.publish(objectUri, 90);
        File downloadedFile = new File(destinationFolder.getPath() + "fileToTest.png");
        FileUtils.copyURLToFile(presignedUrl, downloadedFile);

        //then
        assertTrue(downloadedFile.exists());
        downloadedFile.delete();
        this.dataObject.remove(objectUri, false);
    }

    public void testPublish_ObjectMissing_ThrowException() {

        URI objectUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI") + "fileToTest.png");

        //given
        assertFalse(this.dataObject.doesExist(objectUri));

        //when
        assertThrows(ObjectNotFoundException.class, () -> this.dataObject.publish(objectUri, 90));

        //then
        //Exception thrown
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Tests for remove method
    // -----------------------------------------------------------------------------------------------------------------
    public void testRemove_ObjectPresentNoFolder_ObjectRemoved() throws IOException {

        URI objectUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI") + "fileToTest.png");
        URI localFile = new File("images/test.png").toURI();
        this.dataObject.upload(localFile, objectUri);

        //given
        assertTrue(this.dataObject.doesExist(objectUri));

        //when
        this.dataObject.remove(objectUri, false);

        //then
        assertFalse(this.dataObject.doesExist(objectUri));
    }

    public void testRemove_ObjectAndFolderPresent_ObjectRemoved() throws IOException {

        URI objectUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI") + "fileToTest");
        URI localFile = new File("images/test.png").toURI();
        URI objectUriInSubFolder = URI.create(dotenv.get("GOOGLE_BUCKET_URI") + "test/fileToTest");

        this.dataObject.upload(localFile, objectUri);
        this.dataObject.upload(localFile, objectUriInSubFolder);

        //given
        //The bucket contains object at the root level as well as in subfolders
        //Sample: mybucket.com/myobject     //myObject is a folder
        //        mybucket.com/myobject/myObjectInSubfolder
        assertTrue(this.dataObject.doesExist(objectUri));
        assertTrue(this.dataObject.doesExist(objectUriInSubFolder));

        //when
        this.dataObject.remove(objectUri, true);

        //then
        assertFalse(this.dataObject.doesExist(objectUri));
        assertFalse(this.dataObject.doesExist(objectUriWithSubFolder));
    }
}
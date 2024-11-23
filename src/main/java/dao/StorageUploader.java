package dao;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;

public class StorageUploader {

    private BlobContainerClient containerClient;

    public StorageUploader( ) {
        this.containerClient = new BlobContainerClientBuilder()
                .connectionString("DefaultEndpointsProtocol=https;AccountName=papaziscsc311storage;AccountKey=nc0+nuja2oz29/aipE90NAJ+lPGb3axepOmfg5wlTy2VCOPztc/7ljSn6GCNnyyU8C/xswFlH3fe+AStx0ybNg==;EndpointSuffix=core.windows.net")
                .containerName("media-files")
                .buildClient();
    }


    public void uploadFile(String filePath, String blobName) {
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        blobClient.uploadFromFile(filePath);
    }
    public BlobContainerClient getContainerClient(){
        return containerClient;
    }

    public void listImages() {
        System.out.println("\nListing blobs...");
        // List the blob(s) in the container.
        for (BlobItem blobItem : containerClient.listBlobs()) {
            System.out.println("\t" + blobItem.getName());
        }
        BlobClient blobClient = containerClient.getBlobClient("fsc.png");
    }

    public Image loadImageFromBlob(String blobName){
        //get a reference to the blob
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        //check if the blob exists
        if(!blobClient.exists()){
            System.out.println("blob not found: " + blobName);
            return null;
        }
        try{
            //download the blob as an InputStream
            InputStream blobInputStream = blobClient.openInputStream();
            return new Image(blobInputStream); //create an image from the InputStream
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }





}
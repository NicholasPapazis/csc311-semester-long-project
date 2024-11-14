package dao;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

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
}
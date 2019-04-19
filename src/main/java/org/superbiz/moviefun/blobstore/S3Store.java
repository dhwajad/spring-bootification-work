package org.superbiz.moviefun.blobstore;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class S3Store implements BlobStore {

    AmazonS3Client s3Client;

    String photoStorageBucket;

    public S3Store(AmazonS3Client s3Client, String photoStorageBucket) {
        this.s3Client = s3Client;
        this.photoStorageBucket = photoStorageBucket;
    }

    @Override
    public void put(Blob blob) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(blob.contentType);
        s3Client.putObject(new PutObjectRequest(photoStorageBucket, blob.name, blob.inputStream, objectMetadata));

    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        S3Object s3Object = s3Client.getObject(photoStorageBucket, name);
        Blob blob = new Blob(s3Object.getKey(), s3Object.getObjectContent(), s3Object.getObjectMetadata().getContentType());
        return Optional.ofNullable(blob);
    }

    @Override
    public void deleteAll() {
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(photoStorageBucket);
        s3Client.deleteObjects(deleteObjectsRequest);
    }
}

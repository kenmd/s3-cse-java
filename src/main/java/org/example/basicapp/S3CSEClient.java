package org.example.basicapp;

import java.io.File;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Encryption;
import com.amazonaws.services.s3.AmazonS3EncryptionClientBuilder;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.CryptoConfiguration;
import com.amazonaws.services.s3.model.CryptoMode;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.KMSEncryptionMaterialsProvider;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;

public class S3CSEClient {
    private AmazonS3Encryption s3Encryption;

    public S3CSEClient(String keyAlias) {
        s3Encryption = AmazonS3EncryptionClientBuilder //
                .standard() //
                .withRegion(Regions.AP_NORTHEAST_1) //
                .withCryptoConfiguration( //
                        new CryptoConfiguration(CryptoMode.EncryptionOnly) //
                                .withAwsKmsRegion(Region.getRegion(Regions.AP_NORTHEAST_1)) //
                ).withEncryptionMaterials( //
                        new KMSEncryptionMaterialsProvider(keyAlias) //
                ).build();
    }

    public boolean exists(String s3uriString) {
        var s3uri = new AmazonS3URI(s3uriString);
        return s3Encryption.doesObjectExist(s3uri.getBucket(), s3uri.getKey());
    }

    public PutObjectResult putObject(String s3uriString, File file) {
        var s3uri = new AmazonS3URI(s3uriString);
        return s3Encryption.putObject(s3uri.getBucket(), s3uri.getKey(), file);
    }

    public ObjectMetadata getObject(String s3uriString, File file) {
        var s3uri = new AmazonS3URI(s3uriString);
        var getObjectRequest = new GetObjectRequest(s3uri.getBucket(), s3uri.getKey());
        return s3Encryption.getObject(getObjectRequest, file);
    }

    public String getBasename(String s3uriString) {
        var s3URI = new AmazonS3URI(s3uriString);
        var file = new File(s3URI.getKey());
        return file.getName();
    }
}

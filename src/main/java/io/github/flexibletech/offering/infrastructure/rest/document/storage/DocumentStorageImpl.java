package io.github.flexibletech.offering.infrastructure.rest.document.storage;

import io.github.flexibletech.offering.domain.loanapplication.document.DocumentStorage;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class DocumentStorageImpl implements DocumentStorage {
    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    private static final String DOCUMENT_STORAGE = "document-storage";

    public DocumentStorageImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    @Bulkhead(name = DOCUMENT_STORAGE)
    @CircuitBreaker(name = DOCUMENT_STORAGE)
    public String place(String documentName, byte[] content) {
        var contentStream = new ByteArrayInputStream(content);
        var putObjectArgs = PutObjectArgs.builder()
                .bucket(bucket)
                .object(documentName)
                .stream(contentStream, contentStream.available(), -1)
                .build();

        try {
            minioClient.putObject(putObjectArgs);
        } catch (ErrorResponseException | InsufficientDataException | InternalException
                | InvalidKeyException | InvalidResponseException
                | IOException | NoSuchAlgorithmException
                | ServerException | XmlParserException e) {
            throw new RuntimeException(e);
        }
        return documentName;
    }

}

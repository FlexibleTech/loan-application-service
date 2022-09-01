package io.github.flexibletech.offering.infrastructure.rest;

import io.github.flexibletech.offering.AbstractIntegrationTest;
import io.github.flexibletech.offering.ResourceUtil;
import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.domain.document.DocumentStorage;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class DocumentStorageIT extends AbstractIntegrationTest {
    private static final String ACCESS_KEY = "accessKey";
    private static final String SECRET_KEY = "secretKey";
    private static final String BUCKET = "bucket";

    private static final MinioContainer minioContainer = new MinioContainer(
            new MinioContainer.CredentialsProvider(ACCESS_KEY, SECRET_KEY));

    @Autowired
    private DocumentStorage documentStorage;

    @Autowired
    private MinioClient minioClient;

    @BeforeAll
    static void beforeAll() {
        minioContainer.start();
    }

    @AfterAll
    static void afterAll() {
        minioContainer.stop();
    }

    @Test
    public void shouldPlaceDocument() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.makeBucket(
                MakeBucketArgs.builder()
                        .bucket(BUCKET)
                        .build());

        var content = ResourceUtil.getByteArray("classpath:files/test.pdf");

        var documentId = documentStorage.place(TestValues.FILE_NAME, content);

        Assertions.assertEquals(documentId, TestValues.FILE_NAME);

        InputStream foundContent = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(BUCKET)
                        .object(TestValues.FILE_NAME)
                        .build());
        Assertions.assertArrayEquals(IOUtils.toByteArray(foundContent), content);
    }

}

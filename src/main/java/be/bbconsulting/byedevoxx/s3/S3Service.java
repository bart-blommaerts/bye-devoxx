package be.bbconsulting.byedevoxx.s3;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.ByteBuffer;
import java.util.Random;

@Service
public class S3Service {
    public static void upload(S3Client s3Client, String key) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket("bye-devoxx")
                .key(key)
                .build();
        s3Client.putObject(objectRequest, RequestBody.fromByteBuffer(getRandomByteBuffer(10_000)));
    }

    private static ByteBuffer getRandomByteBuffer(int size) {
        byte[] b = new byte[size];
        new Random().nextBytes(b);
        return ByteBuffer.wrap(b);
    }
}

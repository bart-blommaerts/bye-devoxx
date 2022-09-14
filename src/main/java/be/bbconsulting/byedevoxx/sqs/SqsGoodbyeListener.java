package be.bbconsulting.byedevoxx.sqs;

import be.bbconsulting.byedevoxx.s3.S3Service;
import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.jashmore.sqs.argument.payload.Payload;
import com.jashmore.sqs.spring.container.basic.QueueListener;
import com.jashmore.sqs.spring.decorator.visibilityextender.AutoVisibilityExtender;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@XRayEnabled
@Component
public class SqsGoodbyeListener {
    private static final Region region = Region.EU_WEST_3;

    S3Service s3Service;
    S3Client s3Client;


    ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();

    public SqsGoodbyeListener(S3Service s3Service) {
        this.s3Service = s3Service;

        s3Client = S3Client.builder()
                .credentialsProvider(credentialsProvider)
                .region(region)
                .build();
    }

    @QueueListener(value = "https://sqs.eu-west-3.amazonaws.com/836964591189/devoxx-sqs", messageVisibilityTimeoutInSeconds = 30)
    @AutoVisibilityExtender(visibilityTimeoutInSeconds = 30, maximumDurationInSeconds = 90, bufferTimeInSeconds = 15)
    public void onGoodbyeEvent(@Payload String hello) {
        System.out.println("hello:" + hello);

        s3Service.upload(s3Client, hello);
    }
}

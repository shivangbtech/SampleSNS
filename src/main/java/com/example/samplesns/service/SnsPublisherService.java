package com.example.samplesns.service;

import java.net.URI;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

@Service
public class SnsPublisherService {

  private final SnsClient snsClient;
  private final String topicArn = "arn:aws:sns:us-east-1:000000000000:my-topic";

  public SnsPublisherService() {
    this.snsClient = SnsClient.builder()
        .endpointOverride(URI.create("http://localstack:4566"))
        .region(Region.US_EAST_1)
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create("test", "test")
        ))
        .build();
  }

  public void publishMessage(String message) {
    PublishRequest request = PublishRequest.builder()
        .topicArn(topicArn)
        .message(message)
        .build();
    snsClient.publish(request);
  }
}
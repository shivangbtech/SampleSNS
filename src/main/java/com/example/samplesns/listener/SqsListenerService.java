package com.example.samplesns.listener;

import java.net.URI;
import java.util.List;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

@Service
public class SqsListenerService {

  private final SqsClient sqsClient;
  private final String queueUrl;

  public SqsListenerService(@Value("${spring.cloud.aws.localstack-endpoint}") String endpoint) {
    this.sqsClient = SqsClient.builder()
        .region(Region.US_EAST_1)
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create("test", "test")))
        .endpointOverride(URI.create(endpoint))
        .build();

    this.queueUrl = sqsClient.getQueueUrl(GetQueueUrlRequest.builder()
        .queueName("my-queue").build()).queueUrl();

    // start polling
    pollMessages();
  }

  private void pollMessages() {
    Executors.newSingleThreadExecutor().submit(() -> {
      while (true) {
        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
            .queueUrl(queueUrl)
            .waitTimeSeconds(10)
            .maxNumberOfMessages(5)
            .build();

        List<Message> messages = sqsClient.receiveMessage(request).messages();
        for (Message message : messages) {
          System.out.println("Received: " + message.body());

          // delete after processing
          sqsClient.deleteMessage(DeleteMessageRequest.builder()
              .queueUrl(queueUrl)
              .receiptHandle(message.receiptHandle())
              .build());
        }
      }
    });
  }
}

#!/bin/bash

awslocal sns create-topic --name my-topic
awslocal sqs create-queue --queue-name my-queue
awslocal sns subscribe \
  --topic-arn arn:aws:sns:us-east-1:000000000000:my-topic \
  --protocol sqs \
  --notification-endpoint arn:aws:sqs:us-east-1:000000000000:my-queue



#!/bin/bash
set -e

ENDPOINT_URL=http://localstack:4566

echo "⏳ Waiting for LocalStack..."

until aws --endpoint-url=$ENDPOINT_URL sqs list-queues > /dev/null 2>&1; do
  echo "🕐 Still waiting..."
  sleep 2
done

echo "✅ LocalStack is up"

echo "🚀 Creating SQS queue..."
aws --endpoint-url=$ENDPOINT_URL sqs create-queue --queue-name my-queue

echo "📢 Creating SNS topic..."
aws --endpoint-url=$ENDPOINT_URL sns create-topic --name my-topic

echo "✅ AWS init complete"

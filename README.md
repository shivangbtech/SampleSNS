# AWS SNS (Simple Notification Service) and SQS (Simple Queue Service) with Spring-Boot
A Spring Boot application that demonstrates integration with AWS SNS (Simple Notification Service) and SQS (Simple Queue Service), running locally using LocalStack for development and testing.

## 🛠 Features
- Publish messages to SNS topics
- Automatically subscribe SQS queues to SNS topics
- Consume messages from SQS using async listener
- Fully containerized with Docker
- Local AWS services powered by LocalStack
- init-aws.sh script auto-creates required queues and topics at startup

## 📦 Tech Stack
- Java 8
- Spring Boot 2.x
- AWS SDK v2
- Docker & Docker Compose
- LocalStack
- Maven

## 🚀 Getting Started
### 1. Clone the Repository
```bash
git clone https://github.com/shivangbtech/SampleSNS.git
cd SampleSNS
```   
### 2. Build the application:

```bash
mvn clean package -DskipTests
```

### 3. Run the Application Locally
   Ensure Docker is running, then start the app with:

```bash
docker-compose down -v       # Clean existing volumes and containers

docker-compose up -d --build # Start the services
```

This will:
- Start LocalStack with SQS and SNS services
- Execute init-aws.sh to create my-queue and my-topic
- Build and run the Spring Boot app once LocalStack is ready

## 📜 AWS Initialization Script
The init-aws.sh script will:
- Wait for LocalStack to be fully available
- Create an SQS queue named my-queue
- Create an SNS topic named my-topic
- (Optional) Subscribe the queue to the topic
```bash
#!/bin/bash
set -e
ENDPOINT_URL=http://localstack:4566

until aws --endpoint-url=$ENDPOINT_URL sqs list-queues > /dev/null 2>&1; do
echo "🕐 Waiting for LocalStack..."
sleep 2
done

echo "✅ LocalStack is up"
aws --endpoint-url=$ENDPOINT_URL sqs create-queue --queue-name my-queue
aws --endpoint-url=$ENDPOINT_URL sns create-topic --name my-topic
echo "✅ AWS setup complete"
```

### 📩 Publish Messages
Send a message via curl or Postman:
```bash
curl -X POST http://localhost:8080/api/publish \
-H "Content-Type: application/json" \
-d '{"message": "Hello from SNS!"}'
```
The message will be logged by the SQS listener in your app.

### 🧪 Project Workflow
- init-aws.sh runs in LocalStack ready hook to create AWS resources.
- Spring app starts, subscribes my-queue to my-topic.
- REST endpoint /publish sends messages to SNS, which are delivered to SQS and consumed.

## 📦 Repository Structure
```bash
├── Dockerfile # Spring Boot application build
├── docker-compose.yml # Multi-container setup
├── init-aws.sh # Script to create SNS topic & SQS queue
├── src/
│ └── main/java/com/example/samplesns/
│ ├── SampleSnsApplication.java
│ ├── controller/ # REST controller to publish message
│ └── listener/ # SQS listener
└── README.md```
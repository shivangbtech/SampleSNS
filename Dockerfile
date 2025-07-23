FROM openjdk:8-jdk-alpine
WORKDIR /app

# Build arguments
ARG VERSION=0.0.0-alpha.0
ARG SERVICE_JAR=SampleSNS-${VERSION}.jar

# Set environment variables
ENV VERSION=${VERSION}
ENV SERVICE_JAR=${SERVICE_JAR}
#ENV JAVA_TOOL_OPTIONS="-Dcom.amazonaws.sdk.disableEc2Metadata=true"

# Copy JAR
COPY target/${SERVICE_JAR} app.jar

# Run the application
ENTRYPOINT ["sh", "-c", "exec java $JAVA_TOOL_OPTIONS -jar app.jar"]

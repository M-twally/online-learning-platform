## Use Maven image as base for building the application
#FROM maven:3.8.4-openjdk-11-slim AS build
#
## Set the working directory in the container
#WORKDIR /app
#
## Copy the pom.xml file into the container
#COPY pom.xml .
#
## Download dependencies specified in pom.xml
#RUN mvn dependency:go-offline
#
## Copy the entire project source
#COPY . .
#
## Build the application
#RUN mvn package -DskipTests
#
## Use a lightweight base image for the runtime
#FROM tomcat:9.0-jdk11-openjdk-slim
#
## Set the working directory in the container
#WORKDIR /usr/local/tomcat/webapps
#
## Copy the built WAR file from the previous stage to the Tomcat webapps directory
#COPY --from=build /app/target/*.war .
#
## Expose the port your application runs on (Tomcat default is 8080)
#EXPOSE 8080
#
## Start Tomcat when the container starts
#CMD ["catalina.sh", "run"]
#
#
# Use Maven image as base for building the application
FROM maven:3.8.4-openjdk-11-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file into the container
COPY pom.xml .

# Download dependencies specified in pom.xml
RUN mvn dependency:go-offline

# Copy the entire project source
COPY . .

# Build the application
RUN mvn package -DskipTests

# Use WildFly base image for the runtime
FROM jboss/wildfly:22.0.1.Final

# Set the working directory in the container
WORKDIR /opt/jboss/wildfly/standalone/deployments

# Copy the built WAR file from the previous stage to the WildFly deployments directory
COPY --from=build /app/target/*.war .

# Expose the port your application runs on (WildFly default is 8080)
EXPOSE 8080

# Start WildFly when the container starts
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]

# Lottery Service

The following is a spring-boot project which creates, updates and fetches lottery tickets based on some conditions
## Installation

1. Checkout the git project.
2. Install Java 8 and Maven for the project
2. Go into the distance-calculator directory
    cd distance-calculator
2. Run the command
    ```maven
    mvn clean
    ```   
   This will help to build the classes
2. Then Run the following command to build the jar file
    ```maven
    mvn install
     ```
 2. The following command will create a jar file after running the test cases.
   The jar file will be created inside the target folder as
    ```
    lottery-0.0.1-SNAPSHOT.jar
    ```
2. Run the jar file using the following command
    ```
    java -jar main-1.0-SNAPSHOT.jar
    ```   
2. Once you run the jar the output can be accessed using the following local url:
    ```
    http://localhost:8080/api/v1/ticket/
    ```   

# pets-ui-mpa (Multi Page Application with JSP)

TODO: ADD TESTS

** Migrated to Public Repository after removing credentials. **

This Spring Web MVC is implemented to execute view layer for the PETS app. Most of the business logic
are implemented in pets-service API, so this app provides only the user interface with minimal processing.
It takes requests from the users, and passes it on to pets-service for execution and displays the response.

This does not have Spring Boot starter, so the generated war does not have embedded container.
Hence the app needs to be loaded to outside container (Tomcat/Jetty) to run.
AWS uses Tomcat, GCP uses Jetty.

To run the App:
(1) From IDE, use Jetty plugin for Eclipse.
(2) Create WAR file (mvn clean package OR mvn clean package -P development) and run from container (Tomcat/Jetty)

When running either way, two environment variables need to be set - spring.profiles.active and LOG_CONFIG.
These variables are used in application.properties and logback.xml. Maven Profiles is used in pom.xml when
creating war files.

The App can be run locally or AWS (with -P development WAR file) or GCP. 
To run in AWS, upload WAR file, set the two environment variables, and that's it.
The .ebextensions folder in src/main/webapp contains configurations for viewing logs in the AWS UI.
To run in GCP App Engine, use the GCP plugin for easy way, the environment variables are set in the appengine-web.xml

Used Eclipse instead of IntelliJ because IntelliJ community edition does not support JSP/JS 

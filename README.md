# Location Simulator


## How to run the application
**Step 1:** Update the ```DIRECTION_API_KEY``` variable in ```application.properties``` with your own API key.

**Step 2:** Run the application

For Mac:
```
./gradlew bootRun
```
For Windows:
```
gradlew.bat bootRun
```

**Note:** The application uses Java version 16.

## Demo
Latitute and Longitude of Sikar: 27.6094,75.1398

Latitude and Longitude of Jaipur: 26.9124,75.7873

Once the application is running, we can visit the URL ```http://localhost:8080/getpath/from/27.6094,75.1398/to/26.9124,75.7873/step/50``` and it will give the coordinates of points between Sikar and Jaipur separated by 50m.

### API response
![alt text](https://github.com/Akash01010/location-simulator/blob/master/api_response.JPG?raw=true)

The repository contains python code that can draw the path using the coordinates generated by API.
Run the application using command
```
gradlew.bat bootRun | awk -F'Coordinate:' '/Coordinate:/{print $2}'
```

and put the output in ```points.csv``` file.

After that run
```
python main.py
```
and it will plot the graph using the points.


### Plot of points returned by API vs Google Maps path
![alt text](https://github.com/Akash01010/location-simulator/blob/master/sikar_to_jaipur.JPG?raw=true)

### Plot of points using https://www.mapcustomizer.com
Start: 12.93175,77.62872

End: 12.92662,77.63696  

Step: 50m

URL: http://localhost:8080/getpath/from/12.93175,77.62872/to/12.92662,77.63696/step/50

![alt text](https://github.com/Akash01010/location-simulator/blob/master/a_to_b.JPG?raw=true)

## Op Info Endpoints

### Health
```
http://localhost:8080/actuator/health
```

### Swagger
Swagger UI: http://localhost:8080/swagger-ui.html

Default Path for OpenAPI discriptions in json http://localhost:8080/v3/api-docs/

To get in yaml format: http://localhost:8080/v3/api-docs.yaml

### Seluth
Seluth generates applicationName, traceId, spanId for each request and add it to headers. It is also printed in logs as shown below
```
2021-10-02 16:03:58.010  INFO **[location-simulator,0640e579462f0792,0640e579462f0792]** 18688 --- [nio-8080-exec-6] c.s.l.controller.LocationController      : Inside Hello
```
These logs are stored in memory by Seluth and can be exported to Zipkin Server, ELK for further analysis.

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.4/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.4/gradle-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.5.4/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)


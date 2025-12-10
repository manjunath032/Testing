# Spring Boot WSDL Web Service Project

This is a Spring Boot application for implementing WSDL-based web services (SOAP services).

## Project Structure

```
SpringWsdl/
├── pom.xml                          # Maven configuration with Spring Boot and Web Services dependencies
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/example/
│   │   │       ├── SpringWsdlApplication.java      # Main Spring Boot application class
│   │   │       ├── config/
│   │   │       │   └── WebServiceConfig.java       # WSDL configuration
│   │   │       └── endpoint/
│   │   │           └── SampleWebServiceEndpoint.java # Sample SOAP endpoint
│   │   └── resources/
│   │       ├── application.properties              # Application configuration
│   │       └── xsd/
│   │           └── sample.xsd                      # XSD schema definition
│   └── test/
│       └── java/                                    # Unit tests
└── README.md                         # This file
```

## Key Features

- ✅ Spring Boot 3.2.0 with Java 21
- ✅ Spring Web Services (WSDL/SOAP) support
- ✅ WSDL auto-generation from XSD schema
- ✅ XML binding with JAXB (Jakarta XML Binding)
- ✅ SOAP endpoint configuration
- ✅ Logging configuration
- ✅ Maven build configuration

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- Spring Boot 3.2.0

## Building the Project

```bash
cd F:\Projects\SpringWsdl
mvn clean install
```

## Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## WSDL Endpoint

Once the application is running, you can access the WSDL at:
```
http://localhost:8080/ws/sampleWebService.wsdl
```

## Sample SOAP Request

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                   xmlns:sam="http://www.example.org/sample">
    <soapenv:Header/>
    <soapenv:Body>
        <sam:SampleRequest>
            <sam:name>John Doe</sam:name>
        </sam:SampleRequest>
    </soapenv:Body>
</soapenv:Envelope>
```

## Sample SOAP Response

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                   xmlns:sam="http://www.example.org/sample">
    <soapenv:Body>
        <sam:SampleResponse>
            <sam:message>Hello, John Doe!</sam:message>
            <sam:status>SUCCESS</sam:status>
        </sam:SampleResponse>
    </soapenv:Body>
</soapenv:Envelope>
```

## Testing the Web Service

You can test the web service using tools like:
- SoapUI
- Postman
- cURL with XML payload
- REST clients with SOAP capabilities

## Configuration Files

### pom.xml
Contains all dependencies including:
- `spring-boot-starter-web-services` - Spring WSDL/SOAP support
- `wsdl4j` - WSDL parsing
- `jakarta.xml.bind` - XML binding
- `jakarta.soap-api` - SOAP protocol support

### application.properties
Configure application settings:
- `server.port` - Server port (default: 8080)
- `logging.level.*` - Logging levels for different components

### sample.xsd
Defines the XML schema for your SOAP messages. Modify this to define your own request/response structures.

## Creating Custom SOAP Endpoints

1. Define your XSD schema in `src/main/resources/xsd/`
2. Create endpoint classes in `src/main/java/org/example/endpoint/`
3. Annotate with `@Endpoint` and use `@PayloadRoot`, `@RequestPayload`, `@ResponsePayload`
4. Rebuild and restart the application

## Troubleshooting

- **404 on WSDL endpoint**: Ensure the application is running and XSD file exists
- **XML parsing errors**: Validate your XSD schema syntax
- **SOAP faults**: Check application logs for detailed error messages
- **Port already in use**: Change `server.port` in application.properties

## Next Steps

1. Customize the XSD schema for your business requirements
2. Implement additional SOAP endpoints
3. Add business logic and database integration
4. Configure security (WS-Security)
5. Add WSDL documentation
6. Deploy to production environment

## Useful Links

- [Spring Web Services Documentation](https://spring.io/projects/spring-ws)
- [SOAP Standards](https://www.w3.org/TR/soap/)
- [WSDL Specification](https://www.w3.org/TR/wsdl/)
- [JAXB Documentation](https://eclipse-ee4j.github.io/jaxb-ri/)

---
**Project Created**: December 10, 2025
**Version**: 1.0.0-SNAPSHOT


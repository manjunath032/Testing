package org.example.endpoint;

import org.example.sample.SampleRequest;
import org.example.sample.SampleResponse;
import org.example.sample.LoginRequest;
import org.example.sample.LoginResponse;
import org.example.sample.VerifyAccessRequest;
import org.example.sample.VerifyAccessResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sample Web Service Endpoint
 */
@Endpoint
public class SampleWebServiceEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(SampleWebServiceEndpoint.class);
    private static final String NAMESPACE_URI = "http://www.example.org/sample";

    /**
     * Sample method to handle SOAP requests using JAXB-generated types
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SampleRequest")
    @ResponsePayload
    public SampleResponse handleRequest(@RequestPayload SampleRequest request) {
        logger.info("Received request: {}", request);

        SampleResponse response = new SampleResponse();
        response.setMessage("Hello, " + request.getName() + "!");
        response.setStatus("SUCCESS");

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "LoginRequest")
    @ResponsePayload
    public LoginResponse handleLogin(@RequestPayload LoginRequest request) {
        logger.info("Login request for user: {}", request.getUsername());

        LoginResponse response = new LoginResponse();

        // Simple fixed authentication logic for testing/demo purposes
        if ("user".equals(request.getUsername()) && "pass".equals(request.getPassword())) {
            response.setToken("dummy-token");
            response.setStatus("SUCCESS");
            response.setMessage("Logged in");
        } else {
            response.setStatus("FAILURE");
            response.setMessage("Invalid credentials");
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "VerifyAccessRequest")
    @ResponsePayload
    public VerifyAccessResponse handleVerifyAccess(@RequestPayload VerifyAccessRequest request) {
        logger.info("VerifyAccess request for resource: {} with token: {}", request.getResource(), request.getToken());

        VerifyAccessResponse response = new VerifyAccessResponse();

        // Simple token/resource check for testing/demo purposes
        if ("dummy-token".equals(request.getToken()) && "allowedResource".equals(request.getResource())) {
            response.setAllowed(true);
            response.setReason(null);
        } else if (!"dummy-token".equals(request.getToken())) {
            response.setAllowed(false);
            response.setReason("Invalid token");
        } else {
            response.setAllowed(false);
            response.setReason("Access denied for resource");
        }

        return response;
    }
}

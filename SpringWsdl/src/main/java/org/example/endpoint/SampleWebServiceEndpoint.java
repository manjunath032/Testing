package org.example.endpoint;

import org.example.sample.SampleRequest;
import org.example.sample.SampleResponse;
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
}

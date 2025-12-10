package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;

import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.payload;

@SpringBootTest
public class SoapEndpointIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    private MockWebServiceClient client;

    @BeforeEach
    public void setUp() {
        client = MockWebServiceClient.createClient(applicationContext);
    }

    @Test
    public void testSoapEndpointReturnsHello() {
        String request = "<sam:SampleRequest xmlns:sam=\"http://www.example.org/sample\">" +
                "<sam:name>Test User</sam:name>" +
                "</sam:SampleRequest>";

        String expectedResponse = "<sam:SampleResponse xmlns:sam=\"http://www.example.org/sample\">" +
                "<sam:message>Hello, Test User!</sam:message>" +
                "<sam:status>SUCCESS</sam:status>" +
                "</sam:SampleResponse>";

        client.sendRequest(withPayload(new StringSource(request)))
                .andExpect(payload(new StringSource(expectedResponse)));
    }
}


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

    @Test
    public void testLoginSuccess() {
        String request = "<sam:LoginRequest xmlns:sam=\"http://www.example.org/sample\">" +
                "<sam:username>user</sam:username>" +
                "<sam:password>pass</sam:password>" +
                "</sam:LoginRequest>";

        String expectedResponse = "<sam:LoginResponse xmlns:sam=\"http://www.example.org/sample\">" +
                "<sam:token>dummy-token</sam:token>" +
                "<sam:status>SUCCESS</sam:status>" +
                "<sam:message>Logged in</sam:message>" +
                "</sam:LoginResponse>";

        client.sendRequest(withPayload(new StringSource(request)))
                .andExpect(payload(new StringSource(expectedResponse)));
    }

    @Test
    public void testVerifyAccessAllowed() {
        String request = "<sam:VerifyAccessRequest xmlns:sam=\"http://www.example.org/sample\">" +
                "<sam:token>dummy-token</sam:token>" +
                "<sam:resource>allowedResource</sam:resource>" +
                "</sam:VerifyAccessRequest>";

        String expectedResponse = "<sam:VerifyAccessResponse xmlns:sam=\"http://www.example.org/sample\">" +
                "<sam:allowed>true</sam:allowed>" +
                "</sam:VerifyAccessResponse>";

        client.sendRequest(withPayload(new StringSource(request)))
                .andExpect(payload(new StringSource(expectedResponse)));
    }
}

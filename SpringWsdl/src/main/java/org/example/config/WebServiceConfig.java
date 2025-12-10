package org.example.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/**
 * Configuration for WSDL and Web Services
 */
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    /**
     * Register the message dispatcher servlet for SOAP requests
     */
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
            ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    /**
     * Serve the static WSDL from classpath (wsdl/sample.wsdl)
     */
    @Bean(name = "sampleWebService")
    public SimpleWsdl11Definition sampleWsdl11Definition() {
        return new SimpleWsdl11Definition(new ClassPathResource("wsdl/sample.wsdl"));
    }

    /**
     * Define the XSD schema
     */
    @Bean
    public XsdSchema countriesSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/sample.xsd"));
    }
}

package com.citrus.employee;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.consol.citrus.dsl.endpoint.CitrusEndpoints;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.xml.namespace.NamespaceContextBuilder;

@Configuration
public class EndPointConfig {

	 @Bean
	  public HttpClient employeeClient() { 
		  return CitrusEndpoints.http() 
				   	.client()
				   	.requestUrl("http://localhost:8081") 
				    .build(); 
		  }
	  
	  @Bean
	    public NamespaceContextBuilder namespaceContextBuilder() {
	        final NamespaceContextBuilder namespaceContextBuilder = new NamespaceContextBuilder();
	        namespaceContextBuilder.setNamespaceMappings(Collections.singletonMap("xh", "http://www.w3.org/1999/xhtml"));
	        return namespaceContextBuilder;
	    }
	  
	 }

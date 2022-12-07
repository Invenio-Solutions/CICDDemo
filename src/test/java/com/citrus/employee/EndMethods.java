package com.citrus.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Test;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;

public class EndMethods extends TestNGCitrusSpringSupport {

	 @Autowired
	 private HttpClient employeeClient;
	 
	 @Test
	 @CitrusTest
	    public void testGet() {
	        $(http().client(employeeClient).send().get("/v1/fetch-employees"));

	        $(http().client(employeeClient).receive().response(HttpStatus.OK));
	    }
	 
		/*
		 * @Test
		 * 
		 * @CitrusTest public void testPost() { variable("todoName",
		 * "citrus:concat('todo_', citrus:randomNumber(4))");
		 * variable("todoDescription", "Description: ${todoName}");
		 * 
		 * $(http() .client(employeeClient) .send() .post("/v1/add-employee") .message()
		 * .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
		 * .body("title=${todoName}&description=${todoDescription}"));
		 * 
		 * $( http() .client(employeeClient) .receive() .response(HttpStatus.FOUND)); }
		 */
}

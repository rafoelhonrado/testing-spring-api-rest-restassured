package com.jbenterprise.rest_assured.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import io.restassured.http.ContentType;
import static org.hamcrest.Matchers.lessThan;

public class BasicTest {
	
	@Test
	@Tag("Basic")
    @DisplayName("GET products - /api/v1/product/")
    public void getProducts() {
		 System.out.println("START - Running BasicSuite getProducts - " + Thread.currentThread().getId());
    	 given()
    		.contentType(ContentType.JSON)//Headers
    	.when()
    		.get("http://localhost:8081/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_OK)
    		.body("status", equalTo(true));
    		//.log()//Generate logs
    		//.all();
    	 System.out.println("END - Running BasicSuite getProducts - " + Thread.currentThread().getId());
    }
	
	@Test
	@Tag("Basic")
    @DisplayName("GET products by name - /api/v1/product/")
    public void getProductsByName() {
		 System.out.println("START - Running BasicSuite getProductsByName - " + Thread.currentThread().getId());
    	 given()
    	 	.queryParam("name", "phonex")
    		.contentType(ContentType.JSON)//Headers
    	.when()
    		.get("http://localhost:8081/api/v1/product/")//Uri
    	.then()
    		.statusCode(HttpStatus.SC_OK)
    		.body("status", equalTo(true))
    		.time(lessThan(5000L));
    	 
    		//.log()//Generate logs
    		//.all();
    	 System.out.println("END - Running BasicSuite getProductsByName - " + Thread.currentThread().getId());
    }
}

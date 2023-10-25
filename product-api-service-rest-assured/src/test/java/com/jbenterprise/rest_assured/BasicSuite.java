package com.jbenterprise.rest_assured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.restassured.http.ContentType;

public class BasicSuite {
	
	@Test
    @DisplayName("GET products - /api/v1/product/")
    public void getProducts() {
    	 given()
    		.contentType(ContentType.JSON)//Headers
    	.when()
    		.get("http://localhost:8081/api/v1/product/")//Uri
    	.then()
    		.statusCode(HttpStatus.SC_OK)
    		.body("status", equalTo(true))
    		.log()//Generate logs
    		.all();
    }
	
	@Test
    @DisplayName("GET products by name - /api/v1/product/")
    public void getProductsByName() {
    	 given()
    	 	.queryParam("name", "phonex")
    		.contentType(ContentType.JSON)//Headers
    	.when()
    		.get("http://localhost:8081/api/v1/product/")//Uri
    	.then()
    		.statusCode(HttpStatus.SC_OK)
    		.body("status", equalTo(true))
    		.log()//Generate logs
    		.all();
    }
}

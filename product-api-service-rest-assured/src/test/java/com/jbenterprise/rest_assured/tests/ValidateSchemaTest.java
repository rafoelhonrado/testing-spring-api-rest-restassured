package com.jbenterprise.rest_assured.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import java.io.File;

import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jbenterprise.rest_assured.entity.ProductRequest;
import com.jbenterprise.rest_assured.utils.Utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;


public class ValidateSchemaTest {

	private static RequestSpecification requestSpec;

	@BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8081";
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("User-Agent", Utils.USER_AGENT);
        builder.addHeader("Authorization","Bearer aGFzaGRzZnNkZnNkZnNkZnNk");
        requestSpec = builder.build();
    }   
    
    @Test
    @DisplayName("Validate Schema in classpath- /api/v1/product/")
    public void validateSchema1() {
    	 System.out.println("START - Running IntermediateSuite createNewProduct - " + Thread.currentThread().getId());
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	given()
    		.spec(requestSpec)
    		.header("Content-Type",ContentType.JSON)
    		.body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_CREATED)
    		.body("status", equalTo(true))
    		.body("message", equalTo("El producto fue creado con éxito!"))
    		.body("sku", CoreMatchers.not(equalTo("")))
    		.body(matchesJsonSchemaInClasspath("valid_create_schema.json"));
    		//.log()
    		//.all();
    	System.out.println("END - Running IntermediateSuite createNewProduct - " + Thread.currentThread().getId());
    }
    
    @Test
    @DisplayName("Validate Schema from file- /api/v1/product/")
    public void validateSchema2() {
    	 System.out.println("START - Running IntermediateSuite createNewProduct - " + Thread.currentThread().getId());
    	 File schemaFile = new File("src/test/resources/product-schema.json");
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	given()
    		.spec(requestSpec)
    		.header("Content-Type",ContentType.JSON)
    		.body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_CREATED)
    		.body("status", equalTo(true))
    		.body("message", equalTo("El producto fue creado con éxito!"))
    		.body("sku", CoreMatchers.not(equalTo("")))
    		.body(matchesJsonSchema(schemaFile));
    		//.log()
    		//.all();
    	System.out.println("END - Running IntermediateSuite createNewProduct - " + Thread.currentThread().getId());
    }
}

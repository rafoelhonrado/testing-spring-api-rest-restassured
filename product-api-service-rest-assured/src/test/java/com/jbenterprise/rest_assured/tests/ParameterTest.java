package com.jbenterprise.rest_assured.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.text.IsEmptyString.emptyString;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.hamcrest.text.IsEmptyString;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.jbenterprise.rest_assured.entity.ProductRequest;
import com.jbenterprise.rest_assured.utils.Utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ParameterTest {
	private static String sku="";
	private static RequestSpecification requestSpec;
	
	@BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8081";
        ProductRequest productRequest = Utils.generateNewProductRequest();
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("User-Agent", Utils.USER_AGENT);
        builder.setContentType(ContentType.JSON);
        builder.addHeader("Authorization","Bearer aGFzaGRzZnNkZnNkZnNkZnNk");
        requestSpec = builder.build();
        
    	sku = given()
        		.spec(requestSpec)
        		.body(productRequest)
        	.when()
        		.post("/api/v1/product/")
        	.then()
        		.statusCode(HttpStatus.SC_CREATED)
        		.body("status", equalTo(true))
        		.body("message", equalTo("El producto fue creado con éxito!"))
        		.body("sku", CoreMatchers.not(equalTo("")))
        		.body("sku", CoreMatchers.not(emptyString()))
        		.extract()
        		.jsonPath().getString("sku");
    }   
	
	@ParameterizedTest
	@CsvFileSource(resources = "/datos_crear.csv", numLinesToSkip = 1)
    @DisplayName("Parametrizado - Crear nuevo producto usando /api/v1/product/")
    public void createNewProduct(String name,String description, String price, String message) {
		float fPrice = Float.parseFloat(price);
    	ProductRequest productRequest = Utils.generateNewProductRequest(name,description,fPrice);
    	given()
    		.log().all()
    			.spec(requestSpec)
    		 .body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_CREATED)
    		.body("status", equalTo(true))
    		.body("message", equalTo(message))
    		.body("sku", CoreMatchers.not(equalTo("")))
    		.body("sku", CoreMatchers.not(emptyString()))
    		//.body("sku", equalTo(""))
    		.log()
    		.all();
    }
	
	@ParameterizedTest
	@CsvFileSource(resources = "/datos_actualizar.csv", numLinesToSkip = 1)
    @DisplayName("Parametrizado - Actualizar producto usando /api/v1/product/")
    public void updateProduct(String name,String description, String price, String message) {
		float fPrice = Float.parseFloat(price);
    	ProductRequest productRequest = Utils.generateNewProductRequest();    	
    	
    	productRequest.setName(name);
    	productRequest.setDescription(description);
    	productRequest.setPrice(fPrice);
    	
    	given()
		.pathParam("sku", sku)
    	.contentType(ContentType.JSON)
    	.body(productRequest)
    .when()
    	.put("/api/v1/product/{sku}/")
    .then()
    	.statusCode(HttpStatus.SC_OK)
    	.body("status", equalTo(true))
    	.body("message", equalTo(message))
    	.log()
    	.all();
    }
}

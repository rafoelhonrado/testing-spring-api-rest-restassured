package com.jbenterprise.rest_assured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
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
import io.restassured.http.ContentType;

public class FailureSuite {
	private static String sku="";
	
	@BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8081";
        ProductRequest productRequest = Utils.generateNewProductRequest();
    	sku = given()
        		.headers("User-Agent",Utils.USER_AGENT, "Content-Type", ContentType.JSON)
        		.body(productRequest)
        	.when()
        		.post("/api/v1/product/")
        	.then()
        		.statusCode(HttpStatus.SC_CREATED)
        		.body("status", equalTo(true))
        		.body("message", equalTo("El producto fue creado con éxito!"))
        		.body("sku", CoreMatchers.not(equalTo("")))
        		.extract()
        		.jsonPath().getString("sku");
    }   
	
	@ParameterizedTest
	@CsvFileSource(resources = "/datos.csv", numLinesToSkip = 1)
    @DisplayName("Fallo - Crear nuevo producto usando /api/v1/product/")
    public void createNewProduct(String name,String description, String price, String message) {
		float fPrice = Float.parseFloat(price);
    	ProductRequest productRequest = Utils.generateNewProductRequest(name,description,fPrice);
    	given()
    		 .header("User-Agent", Utils.USER_AGENT)
    		 .header("Content-Type",ContentType.JSON)
    		 .body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_BAD_REQUEST)
    		.body("status", equalTo(false))
    		.body("message", equalTo(message))
    		.body("sku", equalTo(""))
    		.log()
    		.all();
    }
	
	@Test
    @DisplayName("Fallo - Actualizar producto usando /api/v1/product/")
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
    	.statusCode(HttpStatus.SC_BAD_REQUEST)
    	.body("status", equalTo(false))
    	.body("message", equalTo(message))
    	.log()
    	.all();
    }
	
	@Disabled
	@Test
    @DisplayName("Fallo - Actualizar precio  usando /api/v1/product/")
    public void updatePrice() {
    	ProductRequest productRequest = Utils.generateNewProductRequest();    	
    	productRequest.setName("No Modificar");
    	productRequest.setDescription("No Modificar");
    	productRequest.setPrice(0);
    	
    	given()
		.pathParam("sku", sku)
    	.contentType(ContentType.JSON)
    	.body(productRequest)
    .when()
    	.put("/api/v1/product/{sku}/")
    .then()
    	.statusCode(HttpStatus.SC_BAD_REQUEST)
    	.body("status", equalTo(false))
    	.body("message", equalTo(""))
    	.log()
    	.all();
    }
	
    @Test
    @DisplayName("Fallo - Eliminar producto usando /api/v1/product/")
    public void deleteProduct() {
    	String skuFalso="00000";
    	given()
        	.contentType(ContentType.JSON)
        .when()
        	.delete(String.format("/api/v1/product/%1$s/",skuFalso))
        .then()
	    	.statusCode(HttpStatus.SC_NOT_FOUND)
	    	.body("status", equalTo(false))
	    	.body("message", equalTo("El producto no fue encontrado"))
	    	.log()
	    	.all();  	
    }
    
	@Test
    @DisplayName("Fallo - Recuperar producto por Sku usando /api/v1/product/")
    public void getProductBySku() {
		String skuFalso="00000";
    	 given()
    		.contentType(ContentType.JSON)//Headers
    	.when()
    		.get(String.format("/api/v1/product/%1$s/",skuFalso))//Uri
    	.then()
    		.statusCode(HttpStatus.SC_OK)
    		.body("status", equalTo(false))
    		.body("message", equalTo("El producto no fue encontrado"))
    		.log()//Generate logs
    		.all();
    }
}

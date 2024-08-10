package com.jbenterprise.rest_assured.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.jbenterprise.rest_assured.entity.ProductRequest;
import com.jbenterprise.rest_assured.utils.Utils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;


public class IntermediateTest {
	
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
    @DisplayName("Create new product using POST - /api/v1/product/")
    public void createNewProduct() {
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
    		.body("sku", CoreMatchers.not(equalTo("")));
    		//.log()
    		//.all();
    	System.out.println("END - Running IntermediateSuite createNewProduct - " + Thread.currentThread().getId());
    }
    
    @Test
    @DisplayName("Update product using PUT - /api/v1/product/")
    public void updateProduct() {
    	 System.out.println("START - Running IntermediateSuite updateProduct - " + Thread.currentThread().getId());
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	String sku = given()
    		.headers("User-Agent",Utils.USER_AGENT, "Content-Type", ContentType.JSON,"Authorization","Bearer aGFzaGRzZnNkZnNkZnNkZnNk")
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

    	productRequest.setName("Name Updated");
    	productRequest.setDescription("Description Updated");
    	productRequest.setPrice(1900);

    	given()
    		.pathParam("sku", sku)
        	.contentType(ContentType.JSON)
        	.body(productRequest)
        .when()
        	.put("/api/v1/product/{sku}/")
        .then()
	    	.statusCode(HttpStatus.SC_OK)
	    	.body("status", equalTo(true))
	    	.body("message", equalTo("El producto fue actualizado con éxito"));
        	//.log()
        	//.all();
    	System.out.println("END - Running IntermediateSuite updateProduct - " + Thread.currentThread().getId());
    }
    
    @Test
    @DisplayName("Update product price using PATCH - /api/v1/product/")
    public void updateProductPrice() {
    	 System.out.println("START - Running IntermediateSuite updateProductPrice - " + Thread.currentThread().getId());
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	String sku = given()
    			.spec(requestSpec)
    		.headers( "Content-Type", ContentType.JSON)
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
    	
    	productRequest.setName("LORE_IPSUM");
    	productRequest.setDescription("LORE_IPSUM");
    	productRequest.setPrice(2500);

    	given()
    		.contentType(ContentType.JSON)
    		.body(productRequest)
    	.when()
        	.patch(String.format("/api/v1/product/%1$s/",sku))
        .then()
	    	.statusCode(HttpStatus.SC_OK)
	    	.body("status", equalTo(true))
	    	.body("message", equalTo("El precio del producto fue actualizado con éxito"));
	    	//.log()
	    	//.all();
    	System.out.println("END - Running IntermediateSuite updateProductPrice - " + Thread.currentThread().getId());
    }
    
    @Test
    @DisplayName("Delete product using DELETE - /api/v1/product/")
    public void deleteProduct() {
    	 System.out.println("START - Running IntermediateSuite deleteProduct - " + Thread.currentThread().getId());
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	String sku = given()
    		.contentType(ContentType.JSON)
    		.spec(requestSpec)
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

    	given()
        	.contentType(ContentType.JSON)
        	.body(productRequest)
        .when()
        	.delete(String.format("/api/v1/product/%1$s/",sku))
        .then()
	    	.statusCode(HttpStatus.SC_OK)
	    	.body("status", equalTo(true))
	    	.body("message", equalTo("El producto fue eliminado con éxito"));
	    	//.log()
	    	//.all();
    	System.out.println("END - Running IntermediateSuite deleteProduct - " + Thread.currentThread().getId());
    }
    
}

package com.jbenterprise.rest_assured;

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
import io.restassured.http.ContentType;

public class IntermediateSuite {

	@BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8081";
    }   
    
    @Test
    //@Disabled
    @DisplayName("Create new product using POST - /api/v1/product/")
    public void createNewProduct() {    	
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	given()
    		 .header("User-Agent", Utils.USER_AGENT)
    		 .header("Content-Type",ContentType.JSON)
    		 .body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_CREATED)
    		.body("status", equalTo(true))
    		.body("message", equalTo("Product was created sucessfully!"))
    		.body("sku", CoreMatchers.not(equalTo("")))
    		.log()
    		.all();
    }
    
    @Test
    @DisplayName("Update product using PUT - /api/v1/product/")
    public void updateProduct() {
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	String sku = given()
    		.headers("User-Agent",Utils.USER_AGENT, "Content-Type", ContentType.JSON)
    		.body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_CREATED)
    		.body("status", equalTo(true))
    		.body("message", equalTo("Product was created sucessfully!"))
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
	    	.body("message", equalTo("Product was updated sucessfully!"))
        	.log()
        	.all();
    }
    
    @Test
    //@Disabled
    @DisplayName("Update product price using PATCH - /api/v1/product/")
    public void updateProductPrice() {
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	String sku = given()
    		.headers("User-Agent",Utils.USER_AGENT, "Content-Type", ContentType.JSON)
    		.body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_CREATED)
    		.body("status", equalTo(true))
    		.body("message", equalTo("Product was created sucessfully!"))
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
	    	.body("message", equalTo("Product price was updated sucessfully!"))
	    	.log()
	    	.all();
    }
    
    @Test
    //@Disabled
    @DisplayName("Delete product using DELETE - /api/v1/product/")
    public void deleteProduct() {
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	String sku = given()
    		.contentType(ContentType.JSON)
    		.body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
			.statusCode(HttpStatus.SC_CREATED)
			.body("status", equalTo(true))
			.body("message", equalTo("Product was created sucessfully!"))
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
	    	.body("message", equalTo("Product was deleted sucessfully!"))
	    	.log()
	    	.all();  	
    }
    
}

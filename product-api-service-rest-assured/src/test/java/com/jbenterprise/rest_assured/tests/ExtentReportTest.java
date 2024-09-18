package com.jbenterprise.rest_assured.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.jbenterprise.rest_assured.entity.Product;
import com.jbenterprise.rest_assured.entity.ProductRequest;
import com.jbenterprise.rest_assured.entity.ProductResponse;
import com.jbenterprise.rest_assured.utils.ExtentReportManager;
import com.jbenterprise.rest_assured.utils.Utils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ExtentReportTest {
	private static RequestSpecification createSpec;
	private static RequestSpecification genericSpec;
    private static ExtentReports extent;
    private static ExtentTest test;
    
	@BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8081";
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("User-Agent", Utils.USER_AGENT);
        builder.addHeader("Authorization","Bearer aGFzaGRzZnNkZnNkZnNkZnNk");
        builder.addHeader("Content-Type", "application/json");
        createSpec = builder.build();
        
        builder = new RequestSpecBuilder();
        builder.addHeader("User-Agent", Utils.USER_AGENT);
        builder.addHeader("Content-Type", "application/json");
        genericSpec = builder.build();
        
        extent = ExtentReportManager.getInstance(); // Iniciar el reporte
    }
	
	
	@Test
    @DisplayName("Actualizar un producto existente con exito")
    public void verifyProductUsingExtract() {
        test = extent.createTest("Actualizar un producto existente con exito")
                .assignCategory("Exito")
                .assignAuthor("YourName");
		try {
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	String sku = given()
    		.spec(createSpec)
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
    	
    	productRequest.setName("Samsung S20");
    	productRequest.setDescription("Telefono de la marca Samsung");
    	productRequest.setPrice(2500);
    	
		given()
			.spec(genericSpec)
			.pathParam("sku", sku)
			.body(productRequest)
		.when()
			.put("/api/v1/product/{sku}/")
		.then()
			.statusCode(HttpStatus.SC_OK)
			.body("status", equalTo(true))
		.body("message", equalTo("El producto fue actualizado con éxito"));
		
    	 ProductResponse response = given()
    			 .spec(genericSpec)
    			 .pathParam("sku", sku)
    	.when()
    		.get("/api/v1/product/{sku}/")
    	.then()
    		.statusCode(HttpStatus.SC_OK)
    		.extract()
    		.as(ProductResponse.class);
    	 
         Assertions.assertTrue(response.getStatus());
         assertThat(response.getProducts().size(), is(greaterThan(0)));
         Product product = response.getProducts().get(0);
         
         Assertions.assertEquals(productRequest.getName(), product.getName());
         Assertions.assertEquals(productRequest.getDescription(), product.getDescription());
         Assertions.assertEquals(productRequest.getPrice(), product.getPrice());
         test.pass("El producto fue actualizado con exito.");
		} catch(Exception ex) {
			test.fail("El producto no fue actualizado con exito debido a: " + ex.getMessage());
		}
         
    }
	
    @AfterAll
    public static void tearDown() {
        extent.flush(); // Cerrar y generar el reporte
    }
}

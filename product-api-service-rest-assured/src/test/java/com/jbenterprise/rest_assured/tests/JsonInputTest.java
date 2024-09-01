package com.jbenterprise.rest_assured.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.text.IsEmptyString.emptyString;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.stream.Stream;

import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbenterprise.rest_assured.entity.Item;
import com.jbenterprise.rest_assured.entity.Order;
import com.jbenterprise.rest_assured.entity.ProductRequest;
import com.jbenterprise.rest_assured.utils.Utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.io.BufferedReader;
import java.io.File;

public class JsonInputTest {
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
	
    static Stream<Arguments> provideOrderDataFromURL() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String fileUrl = System.getProperty("json.file.url");
        System.out.println(fileUrl);
        if (fileUrl == null) {
            throw new IllegalArgumentException("System property 'json.file.url' is not set.");
        }
        URL url = new URL(fileUrl);        
        List<Order> ordenes = mapper.readValue(
            url,
            new TypeReference<List<Order>>() {}
        );

        return ordenes.stream().map(orden ->
            Arguments.of(orden.getOrden(), orden.getFecha(), orden.getTotal(),orden.getDetalle())
        );
    }
    
    static Stream<Arguments> provideOrderData() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = JsonInputTest.class.getResourceAsStream("/extra.json")) {
        	List<Order> ordenes  = mapper.readValue(inputStream, new TypeReference<List<Order>>() {});            
            return ordenes.stream().map(orden ->
            Arguments.of(orden.getOrden(), orden.getFecha(), orden.getTotal(),orden.getDetalle())
        );
        }
    }
	
	@ParameterizedTest
	@MethodSource("provideOrderData")
    @DisplayName("Parametrizado usando JSON")
	@Tag("JSON")
    public void createNewProduct(String orden,String fecha,float total, List<Item> detalle) {
    	ProductRequest productRequest = Utils.generateNewProductRequest("Iphone15","Description",1500);
    	given()
    		.log().all()
    			.spec(requestSpec)
    		 .body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_CREATED)
    		.body("status", equalTo(true))
    		.log()
    		.all();
    }
}

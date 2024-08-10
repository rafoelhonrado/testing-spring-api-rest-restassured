package com.jbenterprise.rest_assured.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.text.IsEmptyString.emptyString;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvFileSource;
import com.jbenterprise.rest_assured.entity.ProductRequest;
import com.jbenterprise.rest_assured.utils.Utils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParameterAdvTest {
	private static String sku="";
	private static RequestSpecification requestSpec;
	private static String testToRun="actualizar";
	
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
	@Disabled
	@CsvFileSource(resources = "/datos.csv", numLinesToSkip = 1)
    @DisplayName("Parametrizado usando assumeTrue - Crear nuevo producto usando /api/v1/product/")
    public void createNewProduct(String test,String name,String description, String price, String message) {
		assumeTrue("crear".equals(test),"Solo ejecuta test cases para el crear");
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
    		.log()
    		.all();
    }
	
    static class FilteredCsvArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    getClass().getResourceAsStream("/datos.csv")))) {
                List<Arguments> argumentsList = reader.lines()
                        .skip(1) 
                        .map(line -> line.split(","))
                         .filter(fields -> testToRun.equals(fields[0])) 
                        .map(fields -> Arguments.of(fields[1], fields[2], Float.parseFloat(fields[3]), fields[4]))
                        .collect(Collectors.toList());

                return argumentsList.stream();
            }
        }
    }
	
	@ParameterizedTest
	@ArgumentsSource(FilteredCsvArgumentsProvider.class)
    @DisplayName("Parametrizado - Actualizar producto usando /api/v1/product/")
    public void updateProduct(String name,String description, float price, String message) {
		testToRun="actualizar";
    	ProductRequest productRequest = Utils.generateNewProductRequest();    	
    	productRequest.setName(name);
    	productRequest.setDescription(description);
    	productRequest.setPrice(price);
    	
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

package com.jbenterprise.rest_assured.entity;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
	private List<Product> products;
	private Boolean status;
	private String message;
}

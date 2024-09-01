package com.jbenterprise.rest_assured.entity;

import java.util.List;

import lombok.Data;

@Data
public class Order {
	private String orden;
	private String fecha;
	private float total;
	private List<Item> detalle;
}

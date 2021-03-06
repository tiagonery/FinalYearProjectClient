/**
 * 
 */
package com.tiago.finalyearproject.model;


import java.io.Serializable;

/**
 * @author Tiago
 *
 */
public class Address implements Serializable{


	private String postCode;
	private String city;
	private String street;
	private String county;
	private String country;
	private int number;
	private String complement;

	public Address(String postCode, String city, String street, String county, String country, int number, String complement) {
		super();
		this.postCode = postCode;
		this.city = city;
		this.street = street;
		this.county = county;
		this.country = country;
		this.number = number;
		this.complement = complement;
	}
}

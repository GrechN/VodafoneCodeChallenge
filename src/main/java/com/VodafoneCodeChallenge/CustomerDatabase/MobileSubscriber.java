package com.VodafoneCodeChallenge.CustomerDatabase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MobileSubscriber {
	
	@Id
	@GeneratedValue
	private int id;
	// Unique ID for this Mobile Number.
	@Column (unique = true)
	private String msisdn;
	// The mobile number in E164 format.
	private int customer_id_owner;
	// The ID referencing the owner of this mobile number.
	private int customer_id_user;
	// The ID referencing the user of this mobile number.
	private MobileServiceType service_type;
	// An enum defining the type of service.
	private long service_start_date;
	//The time mobile number was created, encoded in Unix Epoch in Milliseconds (UTC).
	
	public MobileSubscriber() {
		super();
		this.service_start_date = System.currentTimeMillis() / 1000l;
	}

	public MobileSubscriber(String msisdn, int customer_id_owner, int customer_id_user, MobileServiceType service_type) {
		super();
		this.msisdn = msisdn;
		this.customer_id_owner = customer_id_owner;
		this.customer_id_user = customer_id_user;
		this.service_type = service_type;	
		this.service_start_date = System.currentTimeMillis() / 1000l;
	}

	public int getId() {
		return id;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public int getCustomer_id_owner() {
		return customer_id_owner;
	}

	public int getCustomer_id_user() {
		return customer_id_user;
	}

	public MobileServiceType getService_type() {
		return service_type;
	}

	public long getService_start_date() {
		return service_start_date;
	}
	
	public void updateServiceType(MobileSubscriber subscriber) {
		this.service_type = subscriber.getService_type();
	}

}

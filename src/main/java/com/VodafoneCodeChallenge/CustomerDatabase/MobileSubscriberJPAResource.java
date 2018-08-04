package com.VodafoneCodeChallenge.CustomerDatabase;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.VodafoneCodeChallenge.CustomerDatabase.Exception.BadRequestException;
import com.VodafoneCodeChallenge.CustomerDatabase.Exception.MsisdnExistsException;
import com.VodafoneCodeChallenge.CustomerDatabase.Exception.SubscriberNotFoundException;

@RestController
public class MobileSubscriberJPAResource {
	
	@Autowired
	private MobileSubscriberRepository subscriberRepository;
	
	// Return all mobile numbers
	@GetMapping("/")
	public List<MobileSubscriber> getAllMobileNumbers() {
		return subscriberRepository.findAll();
	}
	
	// Add a mobile number
	@PostMapping("/")
	public MobileSubscriber addMobileNumber(@RequestBody MobileSubscriber subscriber) {
		
		if (subscriber.getMsisdn() == null || subscriber.getService_type() == null) {
			throw new BadRequestException("Required data missing");
		} 
		
		Optional<MobileSubscriber> savedSubscriber = subscriberRepository.findByMsisdn(subscriber.getMsisdn());
		
		if (!savedSubscriber.isPresent()) {
			return subscriberRepository.save(subscriber);
		} else {
			throw new MsisdnExistsException("Msisdn already exists!");
		}
	}
	
	// Get a specific mobile number
	@GetMapping("/{id}")
	public MobileSubscriber getMobileNumberById(@PathVariable int id) {
		return findSubscriber(id);
	}
	
	// Update a mobile number
	@PutMapping("/{id}")
	public MobileSubscriber updateMobileNumber(@PathVariable int id, @RequestBody MobileSubscriber updatedSubscriber) {
		
		MobileSubscriber subscriber = findSubscriber(id);
		subscriber.updateServiceType(updatedSubscriber);
		subscriberRepository.save(subscriber);

		return subscriber;
	}
	
	// Delete a mobile number by ID
	@DeleteMapping("/{id}")
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void deleteMobileNumber(@PathVariable int id) {
		findSubscriber(id);
		subscriberRepository.deleteById(id);
	}
	
	// Search for mobile number by id
	private MobileSubscriber findSubscriber(int id) {
		Optional<MobileSubscriber> mobileSubscriber = subscriberRepository.findById(id);
		
		if(!mobileSubscriber.isPresent()) {
			throw new SubscriberNotFoundException("Subscriber with ID " + id + " not found!");
		}
		
		return mobileSubscriber.get();
	}

}

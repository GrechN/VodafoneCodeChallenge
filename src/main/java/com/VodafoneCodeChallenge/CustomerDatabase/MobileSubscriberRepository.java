package com.VodafoneCodeChallenge.CustomerDatabase;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MobileSubscriberRepository extends JpaRepository<MobileSubscriber, Integer> {
	
	public Optional<MobileSubscriber> findByMsisdn(String msisdn);
}

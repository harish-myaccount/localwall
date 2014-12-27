package com.geoc.app.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.geoc.app.model.Greeting;

@Repository
public class GreetingRepository {

	static final Logger logger = LoggerFactory
			.getLogger(GreetingRepository.class);
	@Autowired
	MongoTemplate mongoTemplate;

	public void logAllPersons(Greeting greeting) {
		mongoTemplate.save(greeting);
	};
	
	

}

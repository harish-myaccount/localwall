package com.geoc.app.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.geoc.app.model.Topic;

public interface TopicRepository extends MongoRepository<Topic, BigInteger> {
	
	List<Topic> findByOwner(String emailc);

}

package com.geoc.app.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.geoc.app.model.Message;

public interface GCMessageRepository{
	public Map<String, List<Message>> findByToGroupByFrom(String to); 

}
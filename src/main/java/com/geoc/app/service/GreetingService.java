package com.geoc.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geoc.app.model.Greeting;
import com.geoc.app.repository.GreetingRepository;

@Service
public class GreetingService {
	@Autowired
	private GreetingRepository greetingrepo;
	
	public void saveGreeting(Greeting greet){
		greetingrepo.logAllPersons(greet);
	}

}

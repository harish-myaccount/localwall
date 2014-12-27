package com.geoc.app.controller;


import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geoc.app.model.Greeting;
import com.geoc.app.service.GreetingService;

@RestController
public class GreetingController {
	
	@Autowired
	private GreetingService greetservice;

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
    	Greeting greet = new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    	greetservice.saveGreeting(greet);
        return greet;
    }
}
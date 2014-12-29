package com.geoc.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.geoc.app.model.ConnectedUser;
import com.geoc.app.model.Message;
import com.geoc.app.service.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageController {
	

	@Autowired
	private MessageService msgService;
	
	@RequestMapping(value="/inbox",method = RequestMethod.POST )
	@ResponseBody
	public Map<String, List<Message>> getInboxOf(@RequestBody ConnectedUser user){
		return msgService.getInbox(user.getEmail());
	} 
	

}

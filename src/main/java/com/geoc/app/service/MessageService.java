package com.geoc.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geoc.app.model.Conversation;
import com.geoc.app.model.Message;
import com.geoc.app.model.Topic;
import com.geoc.app.repository.GCMessageRepository;
import com.geoc.app.repository.UserRepository;
import com.geoc.app.util.CryptoUtil;

@Service
public class MessageService {
	
	@Autowired
	private GCMessageRepository msgrepo;
	
	@Autowired
	private UserRepository usrRepo;
	
	public Map<String,List<Message>> getInbox(String mail){
		//TODO this needs to be changed
		return msgrepo.findByToGroupByFrom(CryptoUtil.mask(mail));
	}

	public Conversation postToAnotherUser(Message msg) {
		if(msg.getPic()==null){
			msg.setPic(usrRepo.findOne(msg.getFrom()).getImage());
		}
		return msgrepo.addTopicInConversations(msg);
	}

	public List<Topic> getOutbox(String email) {
	return msgrepo.getTopicsParticipated(CryptoUtil.mask(email));	
	}

}

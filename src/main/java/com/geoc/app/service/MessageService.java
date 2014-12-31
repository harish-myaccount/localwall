package com.geoc.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geoc.app.model.Conversation;
import com.geoc.app.model.Message;
import com.geoc.app.model.Topic;
import com.geoc.app.repository.GCTopicRepository;
import com.geoc.app.repository.TopicRepository;
import com.geoc.app.repository.UserRepository;
import com.geoc.app.util.CryptoUtil;

@Service
public class MessageService {
	
	@Autowired
	private GCTopicRepository msgrepo;
	
	@Autowired
	private UserRepository usrRepo;
	
	@Autowired
	private TopicRepository topicrepo;
	
	public List<Topic> getInbox(String mail){
		return topicrepo.findByOwner(CryptoUtil.mask(mail));
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

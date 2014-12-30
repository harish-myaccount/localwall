package com.geoc.app.repository;

import java.util.List;
import java.util.Map;

import com.geoc.app.model.Conversation;
import com.geoc.app.model.Message;
import com.geoc.app.model.Topic;

public interface GCMessageRepository{
	public Map<String, List<Message>> findByToGroupByFrom(String to); 
	
	
	public Conversation addTopicInConversations(Message msg);


	public List<Topic> getTopicsParticipated(String maskedEmail);

}
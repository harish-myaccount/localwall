package com.geoc.app.model;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="g_conversations")
public class Topic {
	
	@Id
	private BigInteger  id;
	
	@Indexed
	private String title;
	private String owner;
	private String[] participants;
	
	private List<Conversation> messages;

	public Topic(String topic,String owner) {
		this.title=topic;
		this.owner=owner;
	}

	public Topic() {
		// TODO Auto-generated constructor stub
	}
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String[] getParticipants() {
		return participants;
	}

	public void setParticipants(String[] participants) {
		this.participants = participants;
	}

	public List<Conversation> getMessages() {
		return messages;
	}

	public void setMessages(List<Conversation> messages) {
		this.messages = messages;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	

}

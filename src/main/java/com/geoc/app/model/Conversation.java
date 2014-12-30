package com.geoc.app.model;

import java.util.Date;

public class Conversation {
	
	private String author;
	private String text;
	private Date timestamp;
	public Conversation(String text2, String from, Date at) {
		this.text=text2;
		this.author = from;
		this.timestamp=at;
	}
	public Conversation() {
		// TODO Auto-generated constructor stub
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}

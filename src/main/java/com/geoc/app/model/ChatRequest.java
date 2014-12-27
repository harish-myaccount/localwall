package com.geoc.app.model;

public class ChatRequest {
	private String email;
	private String answer;
	private Boolean self;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public Boolean getSelf() {
		return self;
	}
	public void setSelf(Boolean self) {
		this.self = self;
	}

}

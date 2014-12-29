package com.geoc.app.model;


import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="g_conversations")
public class Message {

  private String from;
  private String to;
  private Date at;
  public Message(String message, Date at) {
	  this.setAt(at);
	  this.message=message;
}
  public Message(){
	  
  }

public String getFrom() {
	return from;
}

public void setFrom(String from) {
	this.from = from;
}

public String getTo() {
	return to;
}

public void setTo(String to) {
	this.to = to;
}

private String message;
  
  private Integer  id;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Integer  getId() {
    return id;
  }

  public void setId(Integer  id) {
    this.id = id;
  }
public Date getAt() {
	return at;
}
public void setAt(Date at) {
	this.at = at;
}
}

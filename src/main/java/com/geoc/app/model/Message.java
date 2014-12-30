package com.geoc.app.model;


import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

public class Message {

  private String from;
  private String to;
  private String topic;
  private Date at;
  private String text;
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
public String getTopic() {
	return topic;
}
public void setTopic(String topic) {
	this.topic = topic;
}
public String getText() {
	return text;
}
public void setText(String text) {
	this.text = text;
}
public String getPic() {
	return pic;
}
public void setPic(String pic) {
	this.pic = pic;
}

private String pic;
}

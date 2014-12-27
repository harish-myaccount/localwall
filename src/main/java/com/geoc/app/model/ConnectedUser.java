package com.geoc.app.model;

import java.util.Map;

import javax.json.JsonValue;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection="g_users")
public class ConnectedUser implements JsonValue {
	
	private Boolean isChatting;

	@Id
	private String email;
	
	@Transient
	private Map<String,Double> coOrd;
	
	public Map<String, Double> getCoOrd() {
		return coOrd;
	}
	public void setCoOrd(Map<String, Double> coOrd) {
		this.coOrd = coOrd;
	}
	
	private String image;
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}

	@JsonIgnore
	@GeoSpatialIndexed
	private Point location;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public Boolean getIsChatting() {
		return isChatting;
	}
	public void setIsChatting(Boolean isChatting) {
		this.isChatting = isChatting;
	}
	public Point getLocation() {
		return location;
	}
	public void setLocation(Point location) {
		this.location = location;
	}
	private String nickName;
	
	private String tagline;
	
	private String code;

	public ValueType getValueType() {		
		return ValueType.OBJECT;
	}
	public String getTagline() {
		return tagline;
	}
	public void setTagline(String tagline) {
		this.tagline = tagline;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

}

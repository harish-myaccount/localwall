package com.geoc.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geoc.app.model.Message;
import com.geoc.app.repository.GCMessageRepository;
import com.geoc.app.util.CryptoUtil;

@Service
public class MessageService {
	
	@Autowired
	private GCMessageRepository msgrepo;
	
	public Map<String,List<Message>> getInbox(String mail){
		return msgrepo.findByToGroupByFrom(CryptoUtil.mask(mail));
	}

}

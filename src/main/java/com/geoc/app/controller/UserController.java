package com.geoc.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.geoc.app.model.ChatRequest;
import com.geoc.app.model.ConnectedUser;
import com.geoc.app.service.MailDispatcher;
import com.geoc.app.service.UserService;
import com.geoc.app.util.CryptoUtil;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService usrsevice;

	@Autowired
	public UserController(MailDispatcher automail) {
		this.mailService = automail;
	}
	
	final MailDispatcher  mailService;
	
	
	
	@RequestMapping(value="/nearby",method = RequestMethod.POST )
	@ResponseBody
	public GeoResults<ConnectedUser> getUsers(@RequestBody ConnectedUser user){
		
		String emailc = CryptoUtil.mask(user.getEmail());
		ConnectedUser existing = usrsevice.getUser(emailc);
		if(existing==null){
			existing=user;
			existing.setEmail(emailc);
		}
		if(existing.getCode()==null){
			final String code = CryptoUtil.nextString();
			final String to = user.getEmail();
			new Thread(new Runnable() {
				
				public void run() {
					mailService.sendMail("LocalWall", to, "Secret Code", "Your secret code is "+ code);					
				}
			}).start();
			existing.setCode(code);
		}
		existing.setLocation(new Point(user.getCoOrd().get("longitude"), user.getCoOrd().get("latitude")));
		usrsevice.saveUser(existing);
		GeoResults<ConnectedUser> grs = usrsevice.getUsersNearBy(existing);
		for(GeoResult<ConnectedUser> gr : grs){
			if(gr.getContent().getEmail().equalsIgnoreCase(emailc))
				gr.getContent().setEmail(user.getEmail());
		}
	 return grs;	
	}
	

	@RequestMapping(value="/question/add",method = RequestMethod.POST )
	@ResponseBody
	public ConnectedUser updateUser(@RequestBody ConnectedUser user){
		return usrsevice.addQuestion(user);
	}
	
	@RequestMapping(value="/authenticate",method = RequestMethod.POST )
	@ResponseBody
	public boolean authenticateUser(@RequestBody ConnectedUser user){
		return usrsevice.isAllowed(user.getEmail(), user.getCode());
	}
}

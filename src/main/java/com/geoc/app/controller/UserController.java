package com.geoc.app.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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
		final String email = user.getEmail();
		String emailc = CryptoUtil.mask(user.getEmail());
		ConnectedUser existing = usrsevice.getUser(emailc);
		if(existing==null){
			existing=user;
			existing.setEmail(emailc);
		}
		if(existing.getCode()==null){
			final String code = CryptoUtil.nextString();
			new Thread(new Runnable() {
				
				public void run() {
					try {
						mailService.sendMail( email, "Your secret code is "+ code + ".You can change it here http://"+InetAddress.getLocalHost().getHostName()+":8080/users/changecode?e="+email+"&p="+code);
					} catch (UnknownHostException e) {
					
					}					
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
	
	@RequestMapping(value="/changecode",method =RequestMethod.GET)
	@ResponseBody
	public String changePassword (@RequestParam(value="e") String email,@RequestParam(value="p") String pass){
		if(usrsevice.isAllowed(email, pass))
		return "<form method='post'><input type='text' name='newpassword'/><button type='submit'>Set Password</button></form>";
	return "This link is dead";
	}
	
	@RequestMapping(value="/changecode",method =RequestMethod.POST)
	public ModelAndView setCode(@RequestParam(value="e") String email,@RequestParam(value="p") String pass,@RequestParam(value="newpassword") String newcode ){
		if(usrsevice.isAllowed(email, pass)){
			ConnectedUser existing = usrsevice.getUser(CryptoUtil.mask(email));
			existing.setCode(newcode);
			usrsevice.saveUser(existing);
		}
			return new ModelAndView(new RedirectView("/", true));
	
	}
	
}

package com.geoc.app.controller;

import java.net.UnknownHostException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.geoc.app.MailConfig;
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
					mailService.sendMail( email, "Your secret code is "+ code + ".You can change it here "+MailConfig.prefixDomain()+"users/changecode?e="+email+"&p="+code);					
				}
			}).start();
			existing.setCode(code);
		}
		if (existing.getImage() == null) {
			String[] urls = { "http://goo.gl/J7SKmj", "http://goo.gl/SvjslJ" };
			existing.setImage(CryptoUtil.mash(email)  + ".jpg?d="
					+ urls[((int) (Math.random() * 10)) % 2]);
		}
		existing.setLocation(new Point(user.getCoOrd().get("longitude"), user.getCoOrd().get("latitude")));
		usrsevice.saveUser(existing);
		GeoResults<ConnectedUser> grs = usrsevice.getUsersNearBy(existing);
		for(GeoResult<ConnectedUser> gr : grs){
			gr.getContent().setCode(null);
			if(gr.getContent().getEmail().equalsIgnoreCase(emailc))
				gr.getContent().setEmail(user.getEmail());
		}
	 return grs;	
	}
	

	@RequestMapping(value="/question/add",method = RequestMethod.POST )
	@ResponseBody
	@Secured("ROLE_USER")
	public ConnectedUser updateUser(@RequestBody ConnectedUser user){
		return usrsevice.addQuestion(user);
	}
	
	@RequestMapping(value="/authenticate",method = RequestMethod.POST )
	@ResponseBody
	public boolean authenticateUser(@RequestBody ConnectedUser user){
		 if(usrsevice.isAllowed(user.getEmail(), user.getCode())){
			 ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			 authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			 UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(),"token",authorities );
			 SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			 return true;
		 }
		 return false;
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

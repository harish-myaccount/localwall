package com.geoc.app.service;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.stereotype.Service;

import com.geoc.app.model.ConnectedUser;
import com.geoc.app.repository.UserRepository;
import com.geoc.app.util.CryptoUtil;

@Service
public class UserService {
	@Autowired
	UserRepository repo;
	

	public void saveUser(ConnectedUser user) {
		repo.save(user);
	}

	public GeoResults<ConnectedUser> getUsersNearBy(ConnectedUser user) {
		
		return repo.findByTaglineNotNull(user.getLocation(), new Distance(1,
				Metrics.KILOMETERS));
	}

	public ConnectedUser addQuestion(ConnectedUser user) {
		String emailc = CryptoUtil.mask(user.getEmail());
		String email = user.getEmail();
		ConnectedUser updatedUser = repo.findOne(emailc);
		
		updatedUser.setTagline(user.getTagline());

		repo.save(updatedUser);
		updatedUser.setEmail(email);
		return updatedUser;
	}

	public ConnectedUser getUser(String email) {
		return repo.findOne(email);
	}

	public boolean isAllowed(String email, String secret) {
		ConnectedUser existing = repo.findOne(CryptoUtil.mask(email));
		if (existing == null || existing.getCode()==null)
			return false;
		else if (existing.getCode().equalsIgnoreCase(secret))
			return true;
		return false;
	}

}

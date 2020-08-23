package in.nareshit.guru.service;

import in.nareshit.guru.model.User;

public interface IUserService {
	
	Integer saveUser(User user);
	public User findByUsername(String username);

}

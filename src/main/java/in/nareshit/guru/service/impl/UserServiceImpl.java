package in.nareshit.guru.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import in.nareshit.guru.model.User;
import in.nareshit.guru.repo.UserRepository;
import in.nareshit.guru.service.IUserService;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

	@Autowired
	private UserRepository repo;

	@Autowired
	private BCryptPasswordEncoder pwdEncoder;

	@Override
	//@Transactional
	public Integer saveUser(User user) {
		//Encode password
		user.setPassword(pwdEncoder.encode(user.getPassword()));

		return repo.save(user).getId();
	}

	// get user by username.
	//@Transactional(readOnly = true)
	public User findByUsername(String username) {
		Optional<User> user = repo.findByUsername(username);
		if (user.isPresent())
			return user.get();
		return null;
	}
	@Override
	//@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = findByUsername(username);
		if (user == null)
			throw new UsernameNotFoundException(
					new StringBuffer()
					.append("User name ")
					.append(username)
					.append(" not found!")
					.toString());
		List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role))
				.collect(Collectors.toList());
		return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
	}

}

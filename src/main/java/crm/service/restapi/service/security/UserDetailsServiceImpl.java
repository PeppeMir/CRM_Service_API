package crm.service.restapi.service.security;

import java.util.Optional;

import crm.service.restapi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import crm.service.restapi.model.User;
import crm.service.restapi.model.security.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

		final Optional<User> usersOptional = userService.find(username);

		final User user = usersOptional.orElseThrow(() 
				-> new UsernameNotFoundException("User not found"));
		
		return new UserDetailsImpl(user);
	}
}

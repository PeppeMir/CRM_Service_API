package crm.service.restapi.service.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import crm.service.restapi.model.User;
import crm.service.restapi.model.security.UserDetailsImpl;
import crm.service.restapi.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository usersRepository;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

		final Optional<User> usersOptional = usersRepository.findByEmail(username);

		final User user = usersOptional.orElseThrow(() 
				-> new UsernameNotFoundException("User not found"));
		
		return new UserDetailsImpl(user);
	}
}

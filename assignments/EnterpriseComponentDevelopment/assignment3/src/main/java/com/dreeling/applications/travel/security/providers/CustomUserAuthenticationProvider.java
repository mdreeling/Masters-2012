package com.dreeling.applications.travel.security.providers;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.dreeling.applications.travel.domain.User;
import com.dreeling.applications.travel.service.CustomUserDetailsService;

/**
 * A custom authentication provider which accesses a database via JPA to
 * validate the user.
 * 
 * <P>
 * {@link CustomUserDetailsService} is used to return a {@link User} domain
 * object.
 * 
 * @author Michael Dreeling
 * @version 1.0
 */
public class CustomUserAuthenticationProvider implements AuthenticationProvider {

	/** The user details service. */
	CustomUserDetailsService userDetailsService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.AuthenticationProvider#
	 * authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		System.out.println("0. CustomUserAuthenticationProvider.authenticate");

		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
		String username = String.valueOf(auth.getPrincipal());
		String password = String.valueOf(auth.getCredentials());

		System.out.println("username:" + username);

		if (!supports(authentication.getClass())) {
			return null;
		}

		User userDetails = (User) userDetailsService.loadUserByUsername((String) authentication.getPrincipal());

		// return null if no user details are found
		if (userDetails == null) {
			return null;
		}

		// 2. Check the passwords match.
		if (!userDetails.getPassword().equals(password)) {
			System.out.println("0.1 - CustomUserAuthenticationProvider - BAD PASSWORD");
			throw new BadCredentialsException("Custom Auth Bad Credentials");
		}

		System.out.println("CustomUserAuthenticationProvider - userDetails = " + userDetails.getUsername());

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null,
				userDetails.getAuthorities());
		token.setDetails(userDetails);

		return token;
	}

	/**
	 * Gets the user details service.
	 * 
	 * @return the userDetailsService
	 */
	public CustomUserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	/**
	 * Sets the user details service.
	 * 
	 * @param userDetailsService
	 *            the userDetailsService to set
	 */
	public void setUserDetailsService(CustomUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.authentication.AuthenticationProvider#supports
	 * (java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return true;
	}
}

package com.dreeling.applications.travel.security.providers;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.dreeling.applications.travel.domain.User;
import com.dreeling.applications.travel.service.CustomUserDetailsService;

public class CustomUserAuthenticationProvider implements AuthenticationProvider {

	CustomUserDetailsService userDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {

		System.out.println("CustomUserAuthenticationProvider - In here!!!!");
		// quickly return if for some reason the authentication manager uses
		// this provider for non-facebook
		if (!supports(authentication.getClass())) {
			return null;
		}

		User userDetails = (User) userDetailsService
				.loadUserByUsername((String) authentication.getPrincipal());

		// return null if no user details are found
		if (userDetails == null) {
			return null;
		}

		// log.debug 'User details found: ' + userDetails

		// create facebook token, which includes UID
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
		token.setDetails(userDetails);
		return null;
	}

	/**
	 * @return the userDetailsService
	 */
	public CustomUserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	/**
	 * @param userDetailsService
	 *            the userDetailsService to set
	 */
	public void setUserDetailsService(
			CustomUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return true;
	}
}

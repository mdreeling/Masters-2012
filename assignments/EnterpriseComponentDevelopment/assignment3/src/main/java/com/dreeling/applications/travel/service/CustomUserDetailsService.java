package com.dreeling.applications.travel.service;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.dreeling.applications.travel.dao.JPACustomUserDetailsService;
import com.dreeling.applications.travel.domain.User;
import com.dreeling.applications.travel.service.CustomerSvc;

public class CustomUserDetailsService implements UserDetailsService, CustomerSvc {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.dreeling.applications.travel.service.sd#loadUserByUsername(java.lang
	 * .String)
	 */
	/* (non-Javadoc)
	 * @see com.dreeling.applications.travel.service.CustomerSvc#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		System.out.println("Persistence!!!!!!!!!!!! ");

		JPACustomUserDetailsService svc = new JPACustomUserDetailsService();

		User u = svc.findUserByUsername(username);

		System.out.println("User is " + u.getName() + " " + u.getName());

		return new User();
	}

}

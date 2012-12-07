/*
 *
 */
package com.dreeling.applications.travel.service;

import javax.persistence.EntityManagerFactory;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.dreeling.applications.travel.dao.JPACustomUserDetailsService;
import com.dreeling.applications.travel.domain.User;
import com.dreeling.applications.travel.security.providers.CustomUserAuthenticationProvider;

/**
 * A custom implementation of Springs UserDetailsService.
 * 
 * <P>
 * loadUserByUsername is used to return a Spring {@link UserDetails} object
 * which can be operated on by the Custom Authenticator
 * {@link CustomUserAuthenticationProvider}
 * 
 * @author Michael Dreeling
 * @version 1.0
 */
public class CustomUserDetailsService implements UserDetailsService {

	/**
	 * The entity manager (injected by the container so that i can pass it to
	 * the JPACustomUserDetailsService. I found that i had issues injecting the
	 * object directly into the JPACustomUserDetailsService class itself.
	 */
	EntityManagerFactory emf;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.dreeling.applications.travel.service.CustomerSvc#loadUserByUsername
	 * (java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		System.out.println("1. CustomUserDetailsService.loadUserByUsername");

		JPACustomUserDetailsService svc = new JPACustomUserDetailsService(emf.createEntityManager());

		User u = svc.findUserByUsername(username);

		System.out.println("4. Returning " + u);

		return u;
	}

	/**
	 * Gets the emf.
	 *
	 * @return the emf
	 */
	public EntityManagerFactory getEmf() {
		return emf;
	}

	/**
	 * Sets the emf.
	 *
	 * @param emf
	 *            the emf to set
	 */
	public void setEmf(EntityManagerFactory emf) {
		this.emf = emf;
	}

}

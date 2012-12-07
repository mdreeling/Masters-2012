/*
 *
 */
package com.dreeling.applications.travel.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreeling.applications.travel.domain.Role;
import com.dreeling.applications.travel.domain.User;
import com.dreeling.applications.travel.service.CustomUserDetailsService;

/**
 * A JPA-based implementation of the Custom User Details Service. Delegates to a
 * JPA entity manager to issue data access calls against the backing repository.
 * The EntityManager reference is provided by the managing container (Spring)
 * automatically.
 *
 * <P>
 * {@link CustomUserDetailsService} is used to return a {@link User} domain
 * object.
 *
 * @author Michael Dreeling
 * @version 1.0
 */
@Service
@Repository
public class JPACustomUserDetailsService {

	/** The em. */
	protected EntityManager em;

	/**
	 * Instantiates a new jPA custom user details service.
	 */
	public JPACustomUserDetailsService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new jPA custom user details service.
	 *
	 * @param m
	 *            the m
	 */
	public JPACustomUserDetailsService(EntityManager m) {
		// TODO Auto-generated constructor stub
		em = m;
	}

	/**
	 * Sets the entity manager.
	 *
	 * @param em
	 *            the new entity manager
	 */
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	/**
	 * Find user by username.
	 *
	 * @param username
	 *            the username as per the username attribute of {@link User}.
	 * @return An {@link User} instance containing the user and their associated
	 *         {@link Role}'s.
	 * @throws UsernameNotFoundException
	 *             If the user could not be located.
	 */
	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public User findUserByUsername(String username) throws UsernameNotFoundException {
		User s = null;

		System.out.println("2. JPACustomUserDetailsService - finding by username... em = " + em);

		if (username != null) {
			List results = em.createQuery("select b from User b where b.username = :username")
					.setParameter("username", username).getResultList();

			if (results.size() > 0) {
				s = (User) results.get(0);

				System.out.println("3. JPACustomUserDetailsService User = " + s);

				for (Object element : s.getRoles()) {
					Role object = (Role) element;

					if (object != null) {
						System.out.println("3. JPACustomUserDetailsService - Role = " + object.getAuthority());
					}
				}

			} else {
				throw new UsernameNotFoundException("Unable to find Person by username", username);
			}
			return s;

		} else {
			return null;
		}
	}

	/**
	 * Gets the users.
	 *
	 * @return the users
	 */
	@SuppressWarnings("unchecked")
	public List<User> getUsers() {
		System.out.println("Running...");
		return em.createQuery("from User").getResultList();
	}
}
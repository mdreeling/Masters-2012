package com.dreeling.applications.travel.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreeling.applications.travel.domain.User;

/**
 * A JPA-based implementation of the Booking Service. Delegates to a JPA entity
 * manager to issue data access calls against the backing repository. The
 * EntityManager reference is provided by the managing container (Spring)
 * automatically.
 */
@Service("jpaUserService")
@Repository
public class JPACustomUserDetailsService {

	private EntityManager em;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public User findUserByUsername(String username) {

		try {
			System.out
					.println("JPACustomUserDetailsService - finding by username... em = "
							+ em);

			if (username != null) {
				User s = (User) em
						.createQuery(
								"select b from Customer b where b.username = :username")
						.setParameter("username", username).getSingleResult();

				System.out.println("User = " + s);

				return s;

			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
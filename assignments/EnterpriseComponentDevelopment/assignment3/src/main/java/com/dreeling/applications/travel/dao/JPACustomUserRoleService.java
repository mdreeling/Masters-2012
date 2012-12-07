/*
 *
 */
package com.dreeling.applications.travel.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.dreeling.applications.travel.domain.Role;

/**
 * A JPA-based implementation of the Booking Service. Delegates to a JPA entity
 * manager to issue data access calls against the backing repository. The
 * EntityManager reference is provided by the managing container (Spring)
 * automatically.
 */
@Service
@Repository
public class JPACustomUserRoleService {

	/** The em. */
	protected EntityManager em;

	/**
	 * Instantiates a new jPA custom user role service.
	 */
	public JPACustomUserRoleService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new jPA custom user role service.
	 * 
	 * @param m
	 *            the m
	 */
	public JPACustomUserRoleService(EntityManager m) {
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
	 * Gets the roles.
	 * 
	 * @return the roles
	 */
	@SuppressWarnings("unchecked")
	public List<Role> getRoles() {
		System.out.println("Running UserAuthority...");
		return em.createQuery("from Role").getResultList();
	}
}
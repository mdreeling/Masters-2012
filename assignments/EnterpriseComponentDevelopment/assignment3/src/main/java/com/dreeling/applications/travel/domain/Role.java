package com.dreeling.applications.travel.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

/**
 * A role for the user (either normal or admin).
 * 
 * <P>
 * This class has been extended to cater for Custom Authentication. It now
 * implements {@link org.springframework.security.core.GrantedAuthority} and is
 * linked to the additional domain object {@link User} via a JPA mapping.
 * 
 * <P>
 * The method getAuthority from {@link GrantedAuthority} is overridden so as to
 * allow the Stirng value of the role to be returned.
 * 
 * @author Michael Dreeling
 * @version 1.0
 */
@Entity
@Table(name = "UserAuthority")
public class Role implements Serializable, GrantedAuthority {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	private long id;

	/** The user. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user", nullable = true)
	private User user;

	/** The authority. */
	private String authority;

	// @Override
	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.security.core.GrantedAuthority#getAuthority()
	 */
	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return authority;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the authority.
	 *
	 * @param authority
	 *            the authority to set
	 */
	public void setAuthority(String authority) {
		this.authority = authority;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Sets the user.
	 *
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
}

package com.dreeling.applications.travel.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "UserAuthority")
public class Role implements Serializable, GrantedAuthority {
	private static final long serialVersionUID = 1L;
	private long id;
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn(name = "user", nullable = true)
	private User user;
	private String authority;

	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return authority;
	}

	/**
	 * @return the id
	 */
	@Id
	public long getId() {
		return id;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param authority
	 *            the authority to set
	 */
	public void setAuthority(String authority) {
		this.authority = authority;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
}

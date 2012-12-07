package com.dreeling.applications.travel;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.dreeling.applications.travel.dao.JPACustomUserDetailsService;
import com.dreeling.applications.travel.domain.User;

@ContextConfiguration
public class UserDetailsServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	protected JPACustomUserDetailsService usvc;
	@PersistenceContext
	private EntityManager entityManager;

	@Test
	public void readUsers() {
		List<User> users = usvc.getUsers();

		for (Object element : users) {
			User user = (User) element;
			System.out.println("Username = " + user.getUsername() + " : Roles = " + user.getRoles());
		}
	}
}

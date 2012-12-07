package com.dreeling.applications.travel;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.dreeling.applications.travel.dao.JPACustomUserRoleService;
import com.dreeling.applications.travel.domain.Role;

@ContextConfiguration
public class UserRoleServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	protected JPACustomUserRoleService usvc;
	@PersistenceContext
	private EntityManager entityManager;

	@Test
	public void readUsers() {
		List<Role> users = usvc.getRoles();

		for (Object element : users) {
			Role role = (Role) element;
			System.out.println("Role = " + role.getAuthority());
		}
	}
}

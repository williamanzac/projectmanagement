package nz.co.fit.projectmanagement.server.core;

import java.security.Principal;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import nz.co.fit.projectmanagement.server.auth.CustomAuthUser;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

public class ServiceUtilities {

	@Context
	private SecurityContext securityContext;
	private final SessionFactory sessionFactory;

	public ServiceUtilities(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public UserModel getCurrentUser() {
		if (securityContext == null) {
			return null;
		}
		final Principal userPrincipal = securityContext.getUserPrincipal();
		System.out.println("principal name:" + userPrincipal.getName());
		final CustomAuthUser authUser = (CustomAuthUser) userPrincipal;
		final Long userId = authUser.getUserId();
		System.out.println("user id:" + userId);
		if (userId == null) {
			return null;
		}

		final Session session = sessionFactory.getCurrentSession();
		final UserModel currentUser = session.find(UserModel.class, userId);
		return currentUser;
	}
}

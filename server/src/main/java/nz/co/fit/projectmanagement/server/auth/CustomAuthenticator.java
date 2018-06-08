package nz.co.fit.projectmanagement.server.auth;

import java.util.Optional;

import javax.inject.Inject;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.hibernate.UnitOfWork;
import nz.co.fit.projectmanagement.server.dao.UserDAO;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

public class CustomAuthenticator implements Authenticator<CustomCredentials, CustomAuthUser> {

	private final UserDAO userDAO;

	@Inject
	public CustomAuthenticator(final UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	@UnitOfWork
	public Optional<CustomAuthUser> authenticate(final CustomCredentials credentials) throws AuthenticationException {
		if (credentials == null) {
			return Optional.of(new CustomAuthUser());
		}
		final String username = credentials.getUsername();
		final String password = credentials.getPassword();
		final UserModel user = userDAO.findByUsername(username);
		if (user == null) {
			return Optional.of(new CustomAuthUser());
		}
		// check password
		final CustomAuthUser authUser = new CustomAuthUser();
		authUser.setName(user.getEmail());
		authUser.setUserId(user.getId());
		// authUser.setName("foo");
		// authUser.setUserId(1l);
		return Optional.of(authUser);
	}

}

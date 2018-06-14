package nz.co.fit.projectmanagement.server.auth;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Optional;

import javax.inject.Inject;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.hibernate.UnitOfWork;
import nz.co.fit.projectmanagement.server.dao.DAOException;
import nz.co.fit.projectmanagement.server.dao.TokenDAO;
import nz.co.fit.projectmanagement.server.dao.UserDAO;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

public class CustomAuthenticator implements Authenticator<CustomCredentials, CustomAuthUser> {

	private final UserDAO userDAO;
	private final TokenDAO tokenDAO;

	@Inject
	public CustomAuthenticator(final UserDAO userDAO, final TokenDAO tokenDAO) {
		this.userDAO = userDAO;
		this.tokenDAO = tokenDAO;
	}

	@Override
	@UnitOfWork
	public Optional<CustomAuthUser> authenticate(final CustomCredentials credentials) throws AuthenticationException {
		if (credentials == null) {
			throw new AuthenticationException("No credentials provided.");
		}

		// get user for the specified token
		if (isNotBlank(credentials.getToken())) {
			final String token = credentials.getToken();
			final Long userId = tokenDAO.findUserIdForToken(token);
			if (userId == null) {
				throw new AuthenticationException("Invalid token provided.");
			}

			try {
				final UserModel user = userDAO.read(userId);
				if (user == null) {
					throw new AuthenticationException("Invalid token provided.");
				}

				final CustomAuthUser authUser = new CustomAuthUser();
				authUser.setName(user.getEmail());
				authUser.setUserId(user.getId());
				return Optional.of(authUser);
			} catch (final DAOException e) {
				throw new AuthenticationException(e);
			}
		}
		throw new AuthenticationException("Invalid credentials provided.");
	}
}

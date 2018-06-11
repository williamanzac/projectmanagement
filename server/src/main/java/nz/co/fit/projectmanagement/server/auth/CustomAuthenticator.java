package nz.co.fit.projectmanagement.server.auth;

import static nz.co.fit.projectmanagement.server.auth.CustomAuthUser.EMPTY;
import static nz.co.fit.projectmanagement.server.core.PasswordUtilities.decodeSaltAndPassword;
import static nz.co.fit.projectmanagement.server.core.PasswordUtilities.getHash;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.inject.Inject;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

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
			return Optional.of(EMPTY);
		}
		final String username = credentials.getUsername();
		final String password = credentials.getPassword();
		final UserModel user = userDAO.findByUsername(username);
		if (user == null) {
			return Optional.of(EMPTY);
		}

		final String existingPassword = user.getPassword();
		if (!authenticate(password, existingPassword)) {
			// passwords do not match
			return Optional.of(EMPTY);
		}

		final CustomAuthUser authUser = new CustomAuthUser();
		authUser.setName(user.getEmail());
		authUser.setUserId(user.getId());
		return Optional.of(authUser);
	}

	boolean authenticate(final String suppliedPassword, final String storedPassword) {
		final HexBinaryAdapter adapter = new HexBinaryAdapter();

		final String[] saltDetails = decodeSaltAndPassword(storedPassword);
		final String salt = saltDetails[0];
		final String storedHash = saltDetails[1];
		try {
			final byte[] calculatedHash = getHash(suppliedPassword, adapter.unmarshal(salt));
			final String calculatedPasswordHash = adapter.marshal(calculatedHash);
			// logger.debug("comparing ... " + calculatedPasswordHash + " and " + storedHash);
			if (storedHash.equals(calculatedPasswordHash)) {
				return true;
			}
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}
}

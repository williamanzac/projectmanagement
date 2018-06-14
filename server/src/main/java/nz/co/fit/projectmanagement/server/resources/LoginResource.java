package nz.co.fit.projectmanagement.server.resources;

import static nz.co.fit.projectmanagement.server.core.PasswordUtilities.decodeSaltAndPassword;
import static nz.co.fit.projectmanagement.server.core.PasswordUtilities.getHash;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import io.dropwizard.hibernate.UnitOfWork;
import nz.co.fit.projectmanagement.server.api.Token;
import nz.co.fit.projectmanagement.server.core.TokenService;
import nz.co.fit.projectmanagement.server.core.UserService;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginResource {

	private final UserService userService;
	private final TokenService tokenService;

	@Inject
	public LoginResource(final UserService userService, final TokenService tokenService) {
		this.userService = userService;
		this.tokenService = tokenService;
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UnitOfWork
	public Token loginFromForm(final @FormParam("username") String username,
			final @FormParam("password") String password) throws ResourceException {
		return login(username, password);
	}

	@POST
	@UnitOfWork
	@Consumes(MediaType.APPLICATION_JSON)
	public Token login(final @FormParam("username") String username, final @FormParam("password") String password)
			throws ResourceException {
		final UserModel user = userService.findByUsername(username);
		if (user == null) {
			throw new ResourceException("Not a valid username or password");
		}

		final String existingPassword = user.getPassword();
		if (!authenticate(password, existingPassword)) {
			// passwords do not match
			throw new ResourceException("Not a valid username or password");
		}
		final String tokenForUser = tokenService.findOrCreateToken(user.getId());
		final String encoded = Base64.getEncoder().encodeToString(tokenForUser.getBytes());
		final Token token = new Token();
		token.setToken(encoded);
		return token;
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

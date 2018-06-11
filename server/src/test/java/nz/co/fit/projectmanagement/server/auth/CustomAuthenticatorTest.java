package nz.co.fit.projectmanagement.server.auth;

import static nz.co.fit.projectmanagement.server.core.PasswordUtilities.encode;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.dropwizard.testing.junit.DAOTestRule;
import nz.co.fit.projectmanagement.server.dao.UserDAO;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

public class CustomAuthenticatorTest {

	@Rule
	public DAOTestRule database = DAOTestRule.newBuilder().addEntityClass(UserModel.class).build();

	private UserDAO userDAO;

	@Before
	public void setup() {
		userDAO = new UserDAO(database.getSessionFactory());
	}

	@Test
	public void verifyPassword() throws Exception {
		final String suppliedPassword = "password";
		final String storedPassword = encode("password");
		final CustomAuthenticator authenticator = new CustomAuthenticator(userDAO);
		final boolean actual = authenticator.authenticate(suppliedPassword, storedPassword);
		assertThat(actual, equalTo(true));
	}
}

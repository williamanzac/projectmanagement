package nz.co.fit.projectmanagement.server.resources;

import static nz.co.fit.projectmanagement.server.core.PasswordUtilities.encode;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import nz.co.fit.projectmanagement.server.core.TokenService;
import nz.co.fit.projectmanagement.server.core.UserService;

public class LoginResourceTest {
	private static final String PASSWORD = "password";

	private UserService userService;
	private TokenService tokenService;
	private LoginResource loginResource;

	@Before
	public void setup() {
		userService = mock(UserService.class);
		tokenService = mock(TokenService.class);
		loginResource = new LoginResource(userService, tokenService);
	}

	@Test
	public void verifyPassword() throws Exception {
		final String suppliedPassword = PASSWORD;
		final String storedPassword = encode(PASSWORD);
		final boolean actual = loginResource.authenticate(suppliedPassword, storedPassword);
		assertThat(actual, equalTo(true));
	}
}

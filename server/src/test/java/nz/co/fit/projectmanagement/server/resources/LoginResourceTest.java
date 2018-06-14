package nz.co.fit.projectmanagement.server.resources;

import static io.dropwizard.testing.junit.DAOTestRule.newBuilder;
import static nz.co.fit.projectmanagement.server.core.PasswordUtilities.encode;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.dropwizard.testing.junit.DAOTestRule;
import nz.co.fit.projectmanagement.server.core.HistoryService;
import nz.co.fit.projectmanagement.server.core.TokenService;
import nz.co.fit.projectmanagement.server.core.UserService;
import nz.co.fit.projectmanagement.server.dao.HistoryDAO;
import nz.co.fit.projectmanagement.server.dao.TokenDAO;
import nz.co.fit.projectmanagement.server.dao.UserDAO;
import nz.co.fit.projectmanagement.server.dao.entities.HistoryModel;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

public class LoginResourceTest {
	@Rule
	public DAOTestRule database = newBuilder().addEntityClass(UserModel.class).addEntityClass(HistoryModel.class)
			.build();

	private UserDAO userDAO;
	private TokenDAO tokenDAO;
	private HistoryDAO historyDAO;
	private UserService userService;
	private TokenService tokenService;
	private HistoryService historyService;
	private LoginResource loginResource;

	@Before
	public void setup() {
		userDAO = new UserDAO(database.getSessionFactory());
		tokenDAO = new TokenDAO();
		historyDAO = new HistoryDAO(database.getSessionFactory());
		historyService = new HistoryService(historyDAO);
		userService = new UserService(userDAO, historyService);
		tokenService = new TokenService(tokenDAO);
		loginResource = new LoginResource(userService, tokenService);
	}

	@Test
	public void verifyPassword() throws Exception {
		final String suppliedPassword = "password";
		final String storedPassword = encode("password");
		final boolean actual = loginResource.authenticate(suppliedPassword, storedPassword);
		assertThat(actual, equalTo(true));
	}
}

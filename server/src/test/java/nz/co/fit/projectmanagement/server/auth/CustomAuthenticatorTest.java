package nz.co.fit.projectmanagement.server.auth;

import static io.dropwizard.testing.junit.DAOTestRule.newBuilder;

import org.junit.Before;
import org.junit.Rule;

import io.dropwizard.testing.junit.DAOTestRule;
import nz.co.fit.projectmanagement.server.dao.TokenDAO;
import nz.co.fit.projectmanagement.server.dao.UserDAO;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

public class CustomAuthenticatorTest {

	@Rule
	public DAOTestRule database = newBuilder().addEntityClass(UserModel.class).build();

	private UserDAO userDAO;
	private TokenDAO tokenDAO;

	@Before
	public void setup() {
		userDAO = new UserDAO(database.getSessionFactory());
		tokenDAO = new TokenDAO();
	}
}

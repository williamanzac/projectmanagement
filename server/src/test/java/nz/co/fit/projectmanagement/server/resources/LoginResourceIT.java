package nz.co.fit.projectmanagement.server.resources;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static java.lang.String.format;
import static javax.ws.rs.client.Entity.form;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static nz.co.fit.projectmanagement.server.dao.AuthType.PASSWORD;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hibernate.context.internal.ManagedSessionContext.bind;
import static org.hibernate.context.internal.ManagedSessionContext.unbind;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.servlet.ServletContainer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import io.dropwizard.testing.junit.DropwizardAppRule;
import nz.co.fit.projectmanagement.server.ServerApplication;
import nz.co.fit.projectmanagement.server.ServerConfiguration;
import nz.co.fit.projectmanagement.server.api.Token;
import nz.co.fit.projectmanagement.server.core.UserService;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

public class LoginResourceIT {

	private static final String USER_USERNAME = "foo@bar.com";
	private static final String USER_PASSWORD = "password";
	private static final String USER_NAME = "Foo";

	private static final String PARAM_USERNAME = "username";
	private static final String PARAM_PASSWORD = "password";

	private static final String TEST_CONFIG_FILE = resourceFilePath("test-config.yml");

	// startup the server application
	@ClassRule
	public static final DropwizardAppRule<ServerConfiguration> RULE = new DropwizardAppRule<>(ServerApplication.class,
			TEST_CONFIG_FILE);

	// create a user, so that we can verify the login
	@BeforeClass
	public static void setupClass() throws Exception {
		final ServiceLocator serviceLocator = ((ServletContainer) RULE.getEnvironment().getJerseyServletContainer())
				.getApplicationHandler().getServiceLocator();
		final UserService userService = serviceLocator.createAndInitialize(UserService.class);
		final List<SessionFactory> allServices = serviceLocator.getAllServices(SessionFactory.class);
		final SessionFactory sessionFactory = allServices.get(0);

		try (final Session session = sessionFactory.openSession();) {
			bind(session);
			final Transaction transaction = session.beginTransaction();
			try {
				final UserModel userModel = new UserModel();
				userModel.setEmail(USER_USERNAME);
				userModel.setPassword(USER_PASSWORD);
				userModel.setAuthType(PASSWORD);
				userModel.setName(USER_NAME);
				userService.create(userModel);

				transaction.commit();
			} catch (final Exception e) {
				transaction.rollback();
			}
		} finally {
			unbind(sessionFactory);
		}
	}

	@Test
	public void loginHandlerReturnsTokenAfterPost() {
		final Client client = RULE.client();

		final Form entity = new Form();
		entity.param(PARAM_USERNAME, USER_USERNAME);
		entity.param(PARAM_PASSWORD, USER_PASSWORD);

		final String loginUrl = format("http://localhost:%d/login", RULE.getLocalPort());
		final Response response = client.target(loginUrl).request().accept(APPLICATION_JSON_TYPE).post(form(entity));

		assertThat(response.getStatus(), equalTo(200));
		final Token actualToken = response.readEntity(Token.class);
		assertThat(actualToken.getToken(), notNullValue());
	}
}

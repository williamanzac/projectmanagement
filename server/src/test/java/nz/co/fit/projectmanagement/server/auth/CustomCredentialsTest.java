package nz.co.fit.projectmanagement.server.auth;

import static java.util.Base64.getEncoder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CustomCredentialsTest {

	@Test
	public void getCredentials() {
		final String plainText = "username:password";
		final String encoded = getEncoder().encodeToString(plainText.getBytes());
		final String authString = "BASIC " + encoded;
		final CustomCredentials actual = CustomCredentials.getCredentials(authString);
		assertThat(actual.getUsername(), equalTo("username"));
		assertThat(actual.getPassword(), equalTo("password"));
	}
}

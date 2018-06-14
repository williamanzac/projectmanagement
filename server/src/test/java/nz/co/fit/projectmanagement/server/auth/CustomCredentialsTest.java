package nz.co.fit.projectmanagement.server.auth;

import static java.util.Base64.getEncoder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import javax.ws.rs.core.Cookie;

import org.junit.Test;

public class CustomCredentialsTest {

	@Test
	public void getCredentialsBasic() {
		final String plainText = "username:password";
		final String encoded = getEncoder().encodeToString(plainText.getBytes());
		final String authString = "BASIC " + encoded;
		final CustomCredentials actual = CustomCredentials.getCredentials(authString, null);
		assertThat(actual.getUsername(), equalTo("username"));
		assertThat(actual.getPassword(), equalTo("password"));
	}

	@Test
	public void getCredentialsBearer() {
		final String plainText = UUID.randomUUID().toString();
		final String encoded = getEncoder().encodeToString(plainText.getBytes());
		final String authString = "BEARER " + encoded;
		final CustomCredentials actual = CustomCredentials.getCredentials(authString, null);
		assertThat(actual.getToken(), equalTo(plainText));
	}

	@Test
	public void getCredentialsCookie() {
		final String plainText = UUID.randomUUID().toString();
		final String encoded = getEncoder().encodeToString(plainText.getBytes());
		final CustomCredentials actual = CustomCredentials.getCredentials(null, new Cookie("auth-token", encoded));
		assertThat(actual.getToken(), equalTo(plainText));
	}
}

package nz.co.fit.projectmanagement.server.auth;

import static java.util.Base64.getDecoder;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.StringTokenizer;

import javax.ws.rs.core.Cookie;

public class CustomCredentials {

	private String username;
	private String password;
	private String token;

	public CustomCredentials() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public static CustomCredentials getCredentials(final String authString, final Cookie rawToken) {
		// if the cookie token is defined then use that first
		if (rawToken != null) {
			final String encoded = rawToken.getValue();
			final String decoded = new String(getDecoder().decode(encoded));
			final CustomCredentials credentials = parseBearer(decoded);
			return credentials;
		}
		if (isBlank(authString)) {
			return null;
		}

		final StringTokenizer tokenizer = new StringTokenizer(authString, " ");
		final String type = tokenizer.nextToken(); // BASIC or BEARER

		switch (type) {
		case "BEARER":
			final String encoded = tokenizer.nextToken();
			final String decoded = new String(getDecoder().decode(encoded));
			final CustomCredentials credentials = parseBearer(decoded);
			return credentials;
		case "BASIC":
		default:
			return null;
		}
	}

	static CustomCredentials parseBasic(final String decoded) {
		final StringTokenizer tokenizer = new StringTokenizer(decoded, ":");
		if (tokenizer.countTokens() < 2) {
			return null; // or empty
		}
		final CustomCredentials customCredentials = new CustomCredentials();
		customCredentials.username = tokenizer.nextToken();
		customCredentials.password = tokenizer.nextToken();
		return customCredentials;
	}

	static CustomCredentials parseBearer(final String decoded) {
		final CustomCredentials customCredentials = new CustomCredentials();
		customCredentials.token = decoded;
		return customCredentials;
	}

	public String getToken() {
		return token;
	}

	public void setToken(final String token) {
		this.token = token;
	}
}

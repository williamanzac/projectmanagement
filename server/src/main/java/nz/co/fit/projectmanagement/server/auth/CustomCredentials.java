package nz.co.fit.projectmanagement.server.auth;

import static java.util.Base64.getDecoder;

import java.util.StringTokenizer;

public class CustomCredentials {

	private String username;
	private String password;

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

	public static CustomCredentials getCredentials(final String authString) {
		// original "BASIC <Base64String>"
		final StringTokenizer tokenizer = new StringTokenizer(authString, " ");
		// skip first "BASIC"
		tokenizer.nextToken();
		final String encoded = tokenizer.nextToken();
		final String decoded = new String(getDecoder().decode(encoded));
		final CustomCredentials credentials = parse(decoded);
		return credentials;
	}

	public static CustomCredentials parse(final String decoded) {
		final StringTokenizer tokenizer = new StringTokenizer(decoded, ":");
		if (tokenizer.countTokens() < 2) {
			return null; // or empty
		}
		final CustomCredentials customCredentials = new CustomCredentials();
		customCredentials.username = tokenizer.nextToken();
		customCredentials.password = tokenizer.nextToken();
		return customCredentials;
	}
}

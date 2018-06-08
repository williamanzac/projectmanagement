package nz.co.fit.projectmanagement.server.auth;

import org.apache.commons.lang3.StringUtils;

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
		final CustomCredentials credentials = new CustomCredentials();
		final String[] split = StringUtils.split(authString, " ");
		if (split.length == 2) {
			if ("BASIC".equals(split[0])) {
				credentials.setUsername(split[1]);
			}
		}
		return credentials;
	}
}

package nz.co.fit.projectmanagement.server.auth;

import java.security.Principal;

public class CustomAuthUser implements Principal {

	private String name;
	private Long userId;

	public CustomAuthUser() {
	}

	@Override
	public String getName() {
		return name;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(final Long userId) {
		this.userId = userId;
	}

	public void setName(final String name) {
		this.name = name;
	}

}

package nz.co.fit.projectmanagement.server.auth;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

public class CustomSecurityContext implements SecurityContext {
	private final SecurityContext securityContext;
	private final CustomAuthUser principal;

	public CustomSecurityContext(final SecurityContext securityContext, final CustomAuthUser principal) {
		this.securityContext = securityContext;
		this.principal = principal;
	}

	@Override
	public Principal getUserPrincipal() {
		return principal;
	}

	@Override
	public boolean isUserInRole(final String role) {
		return true;
	}

	@Override
	public boolean isSecure() {
		return securityContext.isSecure();
	}

	@Override
	public String getAuthenticationScheme() {
		return "CUSTOM_TOKEN";
	}

}

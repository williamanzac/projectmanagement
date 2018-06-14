package nz.co.fit.projectmanagement.server.auth;

import static javax.ws.rs.Priorities.AUTHENTICATION;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static nz.co.fit.projectmanagement.server.auth.CustomCredentials.getCredentials;
import static org.eclipse.jetty.http.HttpHeader.AUTHORIZATION;

import java.io.IOException;
import java.util.Optional;

import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.SecurityContext;

import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthenticationException;

@PreMatching
@Priority(AUTHENTICATION)
public class CustomAuthFilter extends AuthFilter<CustomCredentials, CustomAuthUser> {

	private static final String AUTH_TOKEN = "auth_token";
	private final CustomAuthenticator authenticator;

	public CustomAuthFilter(final CustomAuthenticator authenticator) {
		this.authenticator = authenticator;
	}

	@Override
	public void filter(final ContainerRequestContext requestContext) throws IOException {
		final String authString = requestContext.getHeaderString(AUTHORIZATION.asString());
		final Cookie rawToken = requestContext.getCookies().get(AUTH_TOKEN);
		final CustomCredentials credentials = getCredentials(authString, rawToken);

		final Optional<CustomAuthUser> authenticatedUser;
		try {
			authenticatedUser = authenticator.authenticate(credentials);
		} catch (final AuthenticationException e) {
			throw new WebApplicationException("Unable to validate credentials", UNAUTHORIZED);
		}

		if (authenticatedUser.isPresent()) {
			final SecurityContext securityContext = new CustomSecurityContext(requestContext.getSecurityContext(),
					authenticatedUser.get());
			requestContext.setSecurityContext(securityContext);
		}
		throw new WebApplicationException("Credentials not valid", UNAUTHORIZED);
	}
}

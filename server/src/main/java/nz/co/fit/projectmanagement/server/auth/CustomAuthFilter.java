package nz.co.fit.projectmanagement.server.auth;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static nz.co.fit.projectmanagement.server.auth.CustomCredentials.getCredentials;
import static org.eclipse.jetty.http.HttpHeader.AUTHORIZATION;

import java.io.IOException;
import java.util.Optional;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.SecurityContext;

import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthenticationException;

@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class CustomAuthFilter extends AuthFilter<CustomCredentials, CustomAuthUser> {

	private final CustomAuthenticator authenticator;

	public CustomAuthFilter(final CustomAuthenticator authenticator) {
		this.authenticator = authenticator;
	}

	@Override
	public void filter(final ContainerRequestContext requestContext) throws IOException {
		final String authString = requestContext.getHeaderString(AUTHORIZATION.asString());
		final CustomCredentials credentials = getCredentials(authString);
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
		} else {
			throw new WebApplicationException("Credentials not valid", UNAUTHORIZED);
		}
	}
}

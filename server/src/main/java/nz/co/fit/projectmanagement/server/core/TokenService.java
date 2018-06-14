package nz.co.fit.projectmanagement.server.core;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.TokenDAO;

@Service
public class TokenService {

	private final TokenDAO tokenDAO;

	@Inject
	public TokenService(final TokenDAO tokenDAO) {
		this.tokenDAO = tokenDAO;
	}

	public Long findUserIdForToken(final String token) {
		return tokenDAO.findUserIdForToken(token);
	}

	public String findTokenForUser(final Long userId) {
		return tokenDAO.findTokenForUser(userId);
	}

	public String findOrCreateToken(final Long userId) {
		return tokenDAO.findOrCreateToken(userId);
	}

	public String createToken(final Long userId) {
		return tokenDAO.createToken(userId);
	}
}

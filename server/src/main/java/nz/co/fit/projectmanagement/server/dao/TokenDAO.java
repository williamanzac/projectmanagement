package nz.co.fit.projectmanagement.server.dao;

import static com.google.common.cache.CacheBuilder.newBuilder;
import static java.util.UUID.randomUUID;
import static java.util.concurrent.TimeUnit.HOURS;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Map.Entry;
import java.util.UUID;

import org.jvnet.hk2.annotations.Service;

import com.google.common.cache.Cache;

@Service
public class TokenDAO {

	private static final Cache<String, Long> userIdByToken = newBuilder().maximumSize(100).expireAfterWrite(1, HOURS)
			.build();

	public Long findUserIdForToken(final String token) {
		return userIdByToken.getIfPresent(token);
	}

	public String findTokenForUser(final Long userId) {
		final String token = userIdByToken.asMap().entrySet().stream().filter(e -> e.getValue() == userId)
				.map(Entry::getKey).findFirst().orElse(null);
		return token;
	}

	public String findOrCreateToken(final Long userId) {
		final String token = findTokenForUser(userId);
		if (isNotBlank(token)) {
			return token;
		}
		return createToken(userId);
	}

	public String createToken(final Long userId) {
		final UUID token = randomUUID();
		final String strToken = token.toString();
		userIdByToken.put(strToken, userId);
		return strToken;
	}
}

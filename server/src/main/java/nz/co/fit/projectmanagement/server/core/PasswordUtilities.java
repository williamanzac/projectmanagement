package nz.co.fit.projectmanagement.server.core;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class PasswordUtilities {
	private static final String ENCODE_PREFIX = "HP1";

	public static String encode(final String plainText) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		if (isEncoded(plainText)) {
			return plainText;
		}

		// Salt generation 64 bits long
		final byte[] bSalt = getSalt();

		// Digest computation
		final byte[] bDigest = getHash(plainText, bSalt);

		final HexBinaryAdapter adapter = new HexBinaryAdapter();
		final String sSalt = adapter.marshal(bSalt);
		final String sDigest = adapter.marshal(bDigest);
		final String encoded = new StringBuilder(ENCODE_PREFIX).append(sSalt).append("|").append(sDigest).toString();

		return encoded;
	}

	private static byte[] getSalt() throws NoSuchAlgorithmException {
		final SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		return getSalt(random);
	}

	private static byte[] getSalt(final SecureRandom random) throws NoSuchAlgorithmException {
		// Salt generation 64 bits long
		final byte[] bSalt = new byte[8];
		random.nextBytes(bSalt);
		return bSalt;
	}

	private static byte[] getHash(final String password, final byte[] salt)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		final MessageDigest digest = MessageDigest.getInstance("SHA-256");
		return getHash(password, salt, digest);
	}

	private static byte[] getHash(final String password, final byte[] salt, final MessageDigest digest)
			throws UnsupportedEncodingException {
		digest.reset();
		digest.update(salt);
		return digest.digest(password.getBytes("UTF-8"));
	}

	private static boolean isEncoded(final String text) {
		return text.startsWith(ENCODE_PREFIX);
	}
}

package nz.co.fit.projectmanagement.server.core;

import static nz.co.fit.projectmanagement.server.core.PasswordUtilities.decodeSaltAndPassword;
import static nz.co.fit.projectmanagement.server.core.PasswordUtilities.encode;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class PasswordUtilitiesTest {

	@Test
	public void doNotEncodeAnEncodedValue() throws Exception {
		final String expected = "HP1HASH";

		final String actual = encode(expected);
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void encodePlainText() throws Exception {
		final String plainText = "plain text";

		final String actual = encode(plainText);
		System.out.println(actual);
		assertThat(actual, startsWith("HP1"));
		assertThat(actual, not(equalTo(plainText)));
	}

	@Test
	public void verifyDecodingSaltAndHash() {
		final String storedPassword = "HP14E761754440E0159|1A7C9F409852C1BEFFB3DF7053DC4762D34BCE33CD28FA9958066CE4EC17910F";
		final String[] saltDetails = { "4E761754440E0159",
				"1A7C9F409852C1BEFFB3DF7053DC4762D34BCE33CD28FA9958066CE4EC17910F" };
		final String[] actual = decodeSaltAndPassword(storedPassword);
		assertThat(actual, arrayContaining(saltDetails));
	}
}

package nz.co.fit.projectmanagement.server.dao.entities;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import nz.co.fit.projectmanagement.server.dao.AuthType;

@Entity
@Table(name = "user")
public class UserModel extends BaseIdableModel {
	@Column
	private String email;
	@Column
	private String password;
	@Column
	private AuthType authType;
	@Column
	private String name;

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public AuthType getAuthType() {
		return authType;
	}

	public void setAuthType(final AuthType authType) {
		this.authType = authType;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, name, password);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final UserModel user = (UserModel) o;

		return Objects.equals(email, user.email) && Objects.equals(name, user.name)
				&& Objects.equals(password, user.password);

	}
}

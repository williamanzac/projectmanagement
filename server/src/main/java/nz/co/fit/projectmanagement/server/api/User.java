package nz.co.fit.projectmanagement.server.api;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class User extends BaseModel {
	private String email;
	private String password;
	private String name;
	private String role;
	private String group;

	public User() {
		// JSON constructor
	}

	public User(final String email, final String name) {
		this.email = email;
		this.name = name;
	}

	public User(final String email, final String name, final String password) {
		this(email, name);
		this.password = password;
	}

	@JsonProperty(required = true)
	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@JsonProperty(access = Access.WRITE_ONLY, required = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	@JsonProperty
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@JsonIgnore
	public String getRole() {
		return role;
	}

	public void setRole(final String role) {
		this.role = role;
	}

	@JsonIgnore
	public String getGroup() {
		return group;
	}

	public void setGroup(final String group) {
		this.group = group;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, group, name, password, role);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final User user = (User) o;

		return Objects.equals(email, user.email) && Objects.equals(group, user.group) && Objects.equals(name, user.name)
				&& Objects.equals(password, user.password) && Objects.equals(role, user.role);

	}
}

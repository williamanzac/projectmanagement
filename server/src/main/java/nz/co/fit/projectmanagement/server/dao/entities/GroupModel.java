package nz.co.fit.projectmanagement.server.dao.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "usergroup")
public class GroupModel extends BaseModel {
	@Column(unique = true, nullable = false)
	@NotEmpty
	private String name;
	@Column
	private String description;
	@OneToMany()
	@JoinColumn(name = "USER_ID")
	@OrderBy("name")
	private List<UserModel> users = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public List<UserModel> getUsers() {
		return users;
	}

	public void setUsers(final List<UserModel> users) {
		this.users = users;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final GroupModel p = (GroupModel) o;

		return Objects.equals(name, p.name) && Objects.equals(description, p.description);
	}
}

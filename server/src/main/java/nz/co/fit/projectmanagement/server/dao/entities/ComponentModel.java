package nz.co.fit.projectmanagement.server.dao.entities;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "component")
public class ComponentModel extends BaseModel {
	@Column(nullable = false)
	private String name;
	@Column
	private String description;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
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

		final ComponentModel c = (ComponentModel) o;

		return Objects.equals(name, c.name) && Objects.equals(description, c.description);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}
}

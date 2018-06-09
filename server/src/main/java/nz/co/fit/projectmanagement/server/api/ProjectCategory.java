package nz.co.fit.projectmanagement.server.api;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectCategory extends BaseModel {
	private String name;
	private String description;

	@SuppressWarnings("unused")
	private ProjectCategory() {
		// JSON constructor
	}

	public ProjectCategory(final String name, final String description) {
		this.description = description;
		this.name = name;
	}

	@JsonProperty(required = true)
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@JsonProperty(required = false)
	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
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

		final ProjectCategory p = (ProjectCategory) o;

		return Objects.equals(name, p.name) && Objects.equals(description, p.description);
	}
}

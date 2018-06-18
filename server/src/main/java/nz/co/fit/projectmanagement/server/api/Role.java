package nz.co.fit.projectmanagement.server.api;

import java.util.Objects;

public class Role extends BaseModel {
	private String name;
	private String description;
	private BaseIdable project;

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

	public BaseIdable getProject() {
		return project;
	}

	public void setProject(final BaseIdable project) {
		this.project = project;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description, project);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final Role p = (Role) o;

		return Objects.equals(name, p.name) && Objects.equals(description, p.description)
				&& Objects.equals(project, p.project);
	}
}

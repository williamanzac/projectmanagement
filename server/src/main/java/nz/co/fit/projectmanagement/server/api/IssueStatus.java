package nz.co.fit.projectmanagement.server.api;

import java.util.Objects;

public class IssueStatus extends BaseModel {
	private String name;
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

		final IssueStatus c = (IssueStatus) o;

		return Objects.equals(name, c.name) && Objects.equals(description, c.description);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

}

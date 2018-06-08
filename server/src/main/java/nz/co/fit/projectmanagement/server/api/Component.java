package nz.co.fit.projectmanagement.server.api;

import java.util.Objects;

public class Component extends BaseIdable {
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

		final Component c = (Component) o;

		return Objects.equals(name, c.name) && Objects.equals(description, c.description);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

}

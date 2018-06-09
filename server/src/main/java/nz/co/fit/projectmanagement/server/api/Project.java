package nz.co.fit.projectmanagement.server.api;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Project extends BaseModel {
	private String name;
	private String description;
	private String key;
	private BaseIdable projectLead;
	private BaseIdable category;
	private String url;

	@SuppressWarnings("unused")
	private Project() {
		// JSON constructor
	}

	public Project(final String name, final String description) {
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

	@JsonProperty
	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@JsonProperty(required = true)
	public String getKey() {
		return key;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	@JsonProperty(required = true)
	public BaseIdable getProjectLead() {
		return projectLead;
	}

	public void setProjectLead(final BaseIdable projectLead) {
		this.projectLead = projectLead;
	}

	@JsonProperty(required = true)
	public BaseIdable getCategory() {
		return category;
	}

	public void setCategory(final BaseIdable category) {
		this.category = category;
	}

	@JsonProperty
	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description, key, category, url);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final Project p = (Project) o;

		return Objects.equals(name, p.name) && Objects.equals(description, p.description) && Objects.equals(key, p.key)
				&& Objects.equals(category, p.category) && Objects.equals(url, p.url);
	}
}

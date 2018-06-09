package nz.co.fit.projectmanagement.server.dao.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "project")
public class ProjectModel extends BaseModel {
	@Column(unique = true, nullable = false)
	private String name;
	@Column
	private String description;
	@OneToMany(orphanRemoval = true)
	@JoinColumn(name = "PROJ_ID")
	@OrderBy("priority")
	private List<VersionModel> versions = new ArrayList<>();
	@Column(unique = true, nullable = false)
	private String key;
	@ManyToOne
	@JoinColumn(name = "USER_ID", nullable = false)
	private UserModel projectLead;
	@ManyToOne
	@JoinColumn(name = "CAT_ID", nullable = false)
	private ProjectCategoryModel category;
	@Column
	private String url;
	@OneToMany(orphanRemoval = true)
	@JoinColumn(name = "COMP_ID")
	@OrderBy("name")
	private List<ComponentModel> components = new ArrayList<>();

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

	@Override
	public int hashCode() {
		return Objects.hash(name, description, key, projectLead, category, url);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final ProjectModel p = (ProjectModel) o;

		return Objects.equals(name, p.name) && Objects.equals(description, p.description) && Objects.equals(key, p.key)
				&& Objects.equals(projectLead, p.projectLead) && Objects.equals(category, p.category)
				&& Objects.equals(url, p.url);
	}

	public List<VersionModel> getVersions() {
		return versions;
	}

	public void setVersions(final List<VersionModel> versions) {
		this.versions = versions;
	}

	public String getKey() {
		return key;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public UserModel getProjectLead() {
		return projectLead;
	}

	public void setProjectLead(final UserModel projectLead) {
		this.projectLead = projectLead;
	}

	public ProjectCategoryModel getCategory() {
		return category;
	}

	public void setCategory(final ProjectCategoryModel category) {
		this.category = category;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public List<ComponentModel> getComponents() {
		return components;
	}

	public void setComponents(final List<ComponentModel> components) {
		this.components = components;
	}
}

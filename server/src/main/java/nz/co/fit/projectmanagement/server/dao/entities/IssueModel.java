package nz.co.fit.projectmanagement.server.dao.entities;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotEmpty;

import nz.co.fit.projectmanagement.server.dao.IssueType;

public class IssueModel extends BaseModel {
	@ManyToOne
	@JoinColumn(name = "PROJ_ID", nullable = false)
	@NotEmpty
	private ProjectModel project;
	@Column(unique = true, nullable = false)
	@NotEmpty
	private String key; // <project key>-<issue number>
	@Column(nullable = false)
	@NotEmpty
	private String name;
	@Column
	private IssueType type;
	@ManyToOne
	@JoinColumn(name = "STATUS_ID")
	private IssueStatusModel status;
	@Column(nullable = false)
	@NotEmpty
	private Integer priority;
	@Column
	private String resolution;
	@OneToMany(orphanRemoval = true)
	@JoinColumn(name = "AFFECT_ID")
	private List<VersionModel> affectsVersions;
	@OneToMany(orphanRemoval = true)
	@JoinColumn(name = "FIX_ID")
	private List<VersionModel> fixedVersions;
	@OneToMany
	@JoinColumn(name = "COMP_ID")
	private List<ComponentModel> components;
	@ElementCollection
	private List<String> tags;
	@Column
	private String description;
	@OneToMany
	@JoinColumn(name = "REL_ID")
	private List<IssueModel> relatedIssues;
	@ManyToOne
	@JoinColumn(name = "ASSIGN_ID", nullable = false)
	@NotEmpty
	private UserModel assignee;
	@Column
	@Temporal(TemporalType.DATE)
	private Date dueDate;
	@Column
	@Temporal(TemporalType.DATE)
	private Date resolvedDate;
	@Column
	private Float estimate;
	@Column
	private Float remaining;

	public ProjectModel getProject() {
		return project;
	}

	public void setProject(final ProjectModel project) {
		this.project = project;
	}

	public String getKey() {
		return key;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public IssueType getType() {
		return type;
	}

	public void setType(final IssueType type) {
		this.type = type;
	}

	public IssueStatusModel getStatus() {
		return status;
	}

	public void setStatus(final IssueStatusModel status) {
		this.status = status;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(final Integer priority) {
		this.priority = priority;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(final String resolution) {
		this.resolution = resolution;
	}

	public List<VersionModel> getAffectsVersions() {
		return affectsVersions;
	}

	public void setAffectsVersions(final List<VersionModel> affectsVersions) {
		this.affectsVersions = affectsVersions;
	}

	public List<VersionModel> getFixedVersions() {
		return fixedVersions;
	}

	public void setFixedVersions(final List<VersionModel> fixedVersions) {
		this.fixedVersions = fixedVersions;
	}

	public List<ComponentModel> getComponents() {
		return components;
	}

	public void setComponents(final List<ComponentModel> components) {
		this.components = components;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(final List<String> tags) {
		this.tags = tags;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public List<IssueModel> getRelatedIssues() {
		return relatedIssues;
	}

	public void setRelatedIssues(final List<IssueModel> relatedIssues) {
		this.relatedIssues = relatedIssues;
	}

	public UserModel getAssignee() {
		return assignee;
	}

	public void setAssignee(final UserModel assignee) {
		this.assignee = assignee;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(final Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getResolvedDate() {
		return resolvedDate;
	}

	public void setResolvedDate(final Date resolvedDate) {
		this.resolvedDate = resolvedDate;
	}

	public Float getEstimate() {
		return estimate;
	}

	public void setEstimate(final Float estimate) {
		this.estimate = estimate;
	}

	public Float getRemaining() {
		return remaining;
	}

	public void setRemaining(final Float remaining) {
		this.remaining = remaining;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, dueDate, estimate, key, name, priority, remaining, resolution, resolvedDate,
				status, tags, type);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final IssueModel other = (IssueModel) obj;
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (dueDate == null) {
			if (other.dueDate != null) {
				return false;
			}
		} else if (!dueDate.equals(other.dueDate)) {
			return false;
		}
		if (estimate == null) {
			if (other.estimate != null) {
				return false;
			}
		} else if (!estimate.equals(other.estimate)) {
			return false;
		}
		if (key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!key.equals(other.key)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (priority == null) {
			if (other.priority != null) {
				return false;
			}
		} else if (!priority.equals(other.priority)) {
			return false;
		}
		if (remaining == null) {
			if (other.remaining != null) {
				return false;
			}
		} else if (!remaining.equals(other.remaining)) {
			return false;
		}
		if (resolution == null) {
			if (other.resolution != null) {
				return false;
			}
		} else if (!resolution.equals(other.resolution)) {
			return false;
		}
		if (resolvedDate == null) {
			if (other.resolvedDate != null) {
				return false;
			}
		} else if (!resolvedDate.equals(other.resolvedDate)) {
			return false;
		}
		if (status == null) {
			if (other.status != null) {
				return false;
			}
		} else if (!status.equals(other.status)) {
			return false;
		}
		if (tags == null) {
			if (other.tags != null) {
				return false;
			}
		} else if (!tags.equals(other.tags)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}
}

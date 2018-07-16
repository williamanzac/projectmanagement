package nz.co.fit.projectmanagement.server.api;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import nz.co.fit.projectmanagement.server.dao.IssueType;

public class Issue extends BaseModel {
	private BaseIdable project;
	private String key; // <project key>-<issue number>
	private String name;
	private IssueType type;
	private BaseIdable status;
	private Integer priority;
	private String resolution;
	private List<BaseIdable> affectsVersions;
	private List<BaseIdable> fixedVersions;
	private List<BaseIdable> components;
	private List<String> tags;
	private String description;
	private List<BaseIdable> relatedIssues;
	private BaseIdable assignee;
	private Date dueDate;
	private Date resolvedDate;
	private Float estimate;
	private Float remaining;

	@JsonProperty(required = false)
	public BaseIdable getProject() {
		return project;
	}

	public void setProject(final BaseIdable project) {
		this.project = project;
	}

	@JsonProperty(required = false, access = Access.READ_ONLY)
	public String getKey() {
		return key;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	@JsonProperty(required = false)
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@JsonProperty(required = false)
	public IssueType getType() {
		return type;
	}

	public void setType(final IssueType type) {
		this.type = type;
	}

	@JsonProperty(required = false)
	public BaseIdable getStatus() {
		return status;
	}

	public void setStatus(final BaseIdable status) {
		this.status = status;
	}

	@JsonProperty(required = false)
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(final Integer priority) {
		this.priority = priority;
	}

	@JsonProperty(required = false)
	public String getResolution() {
		return resolution;
	}

	public void setResolution(final String resolution) {
		this.resolution = resolution;
	}

	@JsonProperty(required = false)
	public List<BaseIdable> getAffectsVersions() {
		return affectsVersions;
	}

	public void setAffectsVersions(final List<BaseIdable> affectsVersions) {
		this.affectsVersions = affectsVersions;
	}

	@JsonProperty(required = false)
	public List<BaseIdable> getFixedVersions() {
		return fixedVersions;
	}

	public void setFixedVersions(final List<BaseIdable> fixedVersions) {
		this.fixedVersions = fixedVersions;
	}

	@JsonProperty(required = false)
	public List<BaseIdable> getComponents() {
		return components;
	}

	public void setComponents(final List<BaseIdable> components) {
		this.components = components;
	}

	@JsonProperty(required = false)
	public List<String> getTags() {
		return tags;
	}

	public void setTags(final List<String> tags) {
		this.tags = tags;
	}

	@JsonProperty(required = false)
	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@JsonProperty(required = false)
	public List<BaseIdable> getRelatedIssues() {
		return relatedIssues;
	}

	public void setRelatedIssues(final List<BaseIdable> relatedIssues) {
		this.relatedIssues = relatedIssues;
	}

	@JsonProperty(required = false)
	public BaseIdable getAssignee() {
		return assignee;
	}

	public void setAssignee(final BaseIdable assignee) {
		this.assignee = assignee;
	}

	@JsonProperty(required = false)
	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(final Date dueDate) {
		this.dueDate = dueDate;
	}

	@JsonProperty(required = false)
	public Date getResolvedDate() {
		return resolvedDate;
	}

	public void setResolvedDate(final Date resolvedDate) {
		this.resolvedDate = resolvedDate;
	}

	@JsonProperty(required = false)
	public Float getEstimate() {
		return estimate;
	}

	public void setEstimate(final Float estimate) {
		this.estimate = estimate;
	}

	@JsonProperty(required = false)
	public Float getRemaining() {
		return remaining;
	}

	public void setRemaining(final Float remaining) {
		this.remaining = remaining;
	}
}

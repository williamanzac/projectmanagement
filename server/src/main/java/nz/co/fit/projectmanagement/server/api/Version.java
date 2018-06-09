package nz.co.fit.projectmanagement.server.api;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import nz.co.fit.projectmanagement.server.dao.VersionStatus;

public class Version extends BaseModel {
	private String name;
	private String description;
	private VersionStatus status;
	private Date startDate;
	private Date releaseDate;
	private Date releasedDate;
	private Date archivedDate;
	private Integer priority;

	@SuppressWarnings("unused")
	private Version() {
		// JSON constructor
	}

	public Version(final String name) {
		this.name = name;
	}

	@JsonProperty(required = true)
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description, status, startDate, releaseDate, releasedDate, archivedDate);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final Version p = (Version) o;

		return Objects.equals(name, p.name) && Objects.equals(description, p.description)
				&& Objects.equals(status, p.status) && Objects.equals(startDate, p.startDate)
				&& Objects.equals(releaseDate, p.releaseDate) && Objects.equals(releasedDate, p.releasedDate)
				&& Objects.equals(archivedDate, p.archivedDate);
	}

	@JsonProperty
	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@JsonProperty(access = Access.READ_ONLY)
	public VersionStatus getStatus() {
		return status;
	}

	public void setStatus(final VersionStatus status) {
		this.status = status;
	}

	@JsonProperty
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	@JsonProperty
	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(final Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	@JsonProperty
	public Date getReleasedDate() {
		return releasedDate;
	}

	public void setReleasedDate(final Date releasedDate) {
		this.releasedDate = releasedDate;
	}

	@JsonProperty
	public Date getArchivedDate() {
		return archivedDate;
	}

	public void setArchivedDate(final Date archivedDate) {
		this.archivedDate = archivedDate;
	}

	@JsonProperty
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(final Integer order) {
		priority = order;
	}
}

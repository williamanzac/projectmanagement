package nz.co.fit.projectmanagement.server.dao.entities;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nz.co.fit.projectmanagement.server.dao.VersionStatus;

@Entity
@Table(name = "version")
public class VersionModel extends BaseIdableModel {
	@Column(nullable = false)
	private String name;
	@Column
	private String description;
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date releaseDate;
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date releasedDate;
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date archivedDate;
	@Column(nullable = false)
	private Integer priority;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description, startDate, releaseDate, releasedDate, archivedDate, priority);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final VersionModel p = (VersionModel) o;

		return Objects.equals(name, p.name) && Objects.equals(description, p.description)
				&& Objects.equals(startDate, p.startDate) && Objects.equals(releaseDate, p.releaseDate)
				&& Objects.equals(releasedDate, p.releasedDate) && Objects.equals(archivedDate, p.archivedDate)
				&& Objects.equals(priority, p.priority);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public VersionStatus getStatus() {
		if (archivedDate != null) {
			return VersionStatus.ARCHIVED;
		}
		if (releasedDate != null) {
			return VersionStatus.RELEASED;
		}
		final Date now = new Date();
		if (now.after(releaseDate)) {
			return VersionStatus.OVERDUE;
		}
		return VersionStatus.UNRELEASED;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(final Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Date getReleasedDate() {
		return releasedDate;
	}

	public void setReleasedDate(final Date releasedDate) {
		this.releasedDate = releasedDate;
	}

	public Date getArchivedDate() {
		return archivedDate;
	}

	public void setArchivedDate(final Date archivedDate) {
		this.archivedDate = archivedDate;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(final Integer order) {
		priority = order;
	}
}

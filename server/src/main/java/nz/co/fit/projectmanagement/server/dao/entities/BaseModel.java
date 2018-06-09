package nz.co.fit.projectmanagement.server.dao.entities;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.CreationTimestamp;

@MappedSuperclass
public abstract class BaseModel extends BaseIdableModel {
	@Column
	@Version
	private Timestamp timestamp;
	@Temporal(TemporalType.DATE)
	@Column(name = "created_on")
	@CreationTimestamp
	private Date createdOn;
	@ManyToOne
	@JoinColumn(name = "CREATE_ID")
	private UserModel createdBy;
	@ManyToOne
	@JoinColumn(name = "UPDATE_ID")
	private UserModel updatedBy;

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(final Date createdOn) {
		this.createdOn = createdOn;
	}

	public UserModel getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(final UserModel createdBy) {
		this.createdBy = createdBy;
	}

	public UserModel getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(final UserModel updatedBy) {
		this.updatedBy = updatedBy;
	}
}

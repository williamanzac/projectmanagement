package nz.co.fit.projectmanagement.server.api;

import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseModel extends BaseIdable {
	private Timestamp timestamp;
	private Date createdOn;
	private BaseIdable createdBy;
	private BaseIdable updatedBy;

	@JsonProperty(required = false)
	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	@JsonProperty(required = false)
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(final Date createdOn) {
		this.createdOn = createdOn;
	}

	@JsonProperty(required = false)
	public BaseIdable getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(final BaseIdable createdBy) {
		this.createdBy = createdBy;
	}

	@JsonProperty(required = false)
	public BaseIdable getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(final BaseIdable updatedBy) {
		this.updatedBy = updatedBy;
	}
}

package nz.co.fit.projectmanagement.server.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class History extends BaseIdable {
	private String fieldName;
	private String oldValue;
	private String newValue;

	@JsonProperty(access = Access.READ_ONLY)
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(final String fieldName) {
		this.fieldName = fieldName;
	}

	@JsonProperty(access = Access.READ_ONLY)
	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(final String oldValue) {
		this.oldValue = oldValue;
	}

	@JsonProperty(access = Access.READ_ONLY)
	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(final String newValue) {
		this.newValue = newValue;
	}
}

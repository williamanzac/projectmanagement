package nz.co.fit.projectmanagement.server.dao.entities;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "history")
public class HistoryModel extends BaseIdableModel {
	@Column(updatable = false, nullable = false)
	private String entityClass;
	@Column(updatable = false, nullable = false)
	private String fieldName;
	@Column(updatable = false, nullable = true)
	private String oldValue;
	@Column(updatable = false, nullable = true)
	private String newValue;
	@Column(updatable = false, nullable = false)
	private Long objectId;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(final String fieldName) {
		this.fieldName = fieldName;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(final String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(final String newValue) {
		this.newValue = newValue;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(final Long objectId) {
		this.objectId = objectId;
	}

	public String getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(final String entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public int hashCode() {
		return Objects.hash(entityClass, fieldName, oldValue, newValue, objectId);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final HistoryModel h = (HistoryModel) o;

		return Objects.equals(entityClass, h.entityClass) && Objects.equals(fieldName, h.fieldName)
				&& Objects.equals(oldValue, h.oldValue) && Objects.equals(newValue, h.newValue)
				&& Objects.equals(objectId, h.objectId);
	}
}

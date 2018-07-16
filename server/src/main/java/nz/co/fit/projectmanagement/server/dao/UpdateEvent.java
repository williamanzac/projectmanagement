package nz.co.fit.projectmanagement.server.dao;

import nz.co.fit.projectmanagement.server.dao.entities.IdableModel;

public class UpdateEvent {
	private String entityClass;
	private String fieldName;
	private String oldValue;
	private String newValue;
	private Long objectId;

	public <E extends IdableModel> UpdateEvent(final String fieldName, final String oldValue, final String newValue,
			final E object) {
		this(object.getClass().getName(), fieldName, oldValue, newValue, object.getId());
	}

	public UpdateEvent(final String entityClass, final String fieldName, final String oldValue, final String newValue,
			final Long objectId) {
		this.entityClass = entityClass;
		this.fieldName = fieldName;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.objectId = objectId;
	}

	public String getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(final String entityClass) {
		this.entityClass = entityClass;
	}

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
}

package nz.co.fit.projectmanagement.server.dao;

import nz.co.fit.projectmanagement.server.dao.entities.IdableModel;

public class DeleteEvent {
	private String entityClass;
	private Long objectId;

	public <E extends IdableModel> DeleteEvent(final E object) {
		this(object.getClass().getName(), object.getId());
	}

	public DeleteEvent(final String entityClass, final Long objectId) {
		this.entityClass = entityClass;
		this.objectId = objectId;
	}

	public String getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(final String entityClass) {
		this.entityClass = entityClass;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(final Long objectId) {
		this.objectId = objectId;
	}
}

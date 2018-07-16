package nz.co.fit.projectmanagement.server.core;

import java.util.function.Consumer;

import nz.co.fit.projectmanagement.server.dao.entities.IdableModel;

class DeleteConsumer<T extends IdableModel> implements Consumer<T> {
	private final CRUDLService<T> service;

	public DeleteConsumer(final CRUDLService<T> service) {
		this.service = service;
	}

	@Override
	public void accept(final T v) {
		try {
			service.delete(v.getId());
		} catch (final ServiceException e) {
			e.printStackTrace();
		}
	}
}
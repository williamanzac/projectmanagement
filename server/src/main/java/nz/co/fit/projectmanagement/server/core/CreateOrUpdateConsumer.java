package nz.co.fit.projectmanagement.server.core;

import java.util.function.Consumer;

import nz.co.fit.projectmanagement.server.dao.entities.IdableModel;

class CreateOrUpdateConsumer<T extends IdableModel> implements Consumer<T> {
	private final CRUDLService<T> service;

	public CreateOrUpdateConsumer(final CRUDLService<T> service) {
		this.service = service;
	}

	@Override
	public void accept(final T v) {
		try {
			if (v.getId() != null) {
				service.update(v);
			} else {
				service.create(v);
			}
		} catch (final ServiceException e) {
			e.printStackTrace();
		}
	}
}
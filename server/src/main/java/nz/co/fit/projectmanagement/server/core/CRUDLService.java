package nz.co.fit.projectmanagement.server.core;

import java.util.List;

import nz.co.fit.projectmanagement.server.dao.BaseDAO;
import nz.co.fit.projectmanagement.server.dao.DAOException;
import nz.co.fit.projectmanagement.server.dao.entities.IdableModel;

public abstract class CRUDLService<T extends IdableModel> {

	final BaseDAO<T> dao;

	public CRUDLService(final BaseDAO<T> dao) {
		this.dao = dao;
	}

	public T create(final T value) throws ServiceException {
		try {
			return dao.upsert(value);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public T read(final Long id) throws ServiceException {
		try {
			return dao.read(id);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public T update(final T value) throws ServiceException {
		try {
			return dao.upsert(value);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public void delete(final Long id) throws ServiceException {
		try {
			dao.delete(id);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public List<T> list() throws ServiceException {
		try {
			return dao.list();
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}
}

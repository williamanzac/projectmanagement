package nz.co.fit.projectmanagement.server.core;

import java.util.List;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.DAOException;
import nz.co.fit.projectmanagement.server.dao.HistoryDAO;
import nz.co.fit.projectmanagement.server.dao.entities.HistoryModel;
import nz.co.fit.projectmanagement.server.dao.entities.IdableModel;

@Service
public class HistoryService {

	private final HistoryDAO dao;

	@Inject
	public HistoryService(final HistoryDAO dao) {
		this.dao = dao;
	}

	public HistoryModel create(final HistoryModel history) throws ServiceException {
		try {
			if (history.getId() != null) {
				throw new ServiceException("History entries cannot be updated");
			}
			return dao.upsert(history);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public HistoryModel read(final Long id) throws ServiceException {
		try {
			return dao.read(id);
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

	public <E extends IdableModel> void deleteHistoryForObject(final Long objectId, final Class<E> entityClass) {
		dao.deleteHistoryForObject(objectId, entityClass);
	}

	public <E extends IdableModel> List<HistoryModel> historyForObject(final Long id, final Class<E> entityClass) {
		return dao.historyForObject(id, entityClass);
	}
}

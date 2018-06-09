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

	private final HistoryDAO historyDAO;

	@Inject
	public HistoryService(final HistoryDAO historyDAO) {
		this.historyDAO = historyDAO;
	}

	public HistoryModel createHistory(final HistoryModel history) throws ServiceException {
		try {
			return historyDAO.upsert(history);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public HistoryModel readHistory(final Long id) throws ServiceException {
		try {
			return historyDAO.read(id);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public void deleteHistory(final Long id) throws ServiceException {
		try {
			historyDAO.delete(id);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public <E extends IdableModel> void deleteHistoryForObject(final Long objectId, final Class<E> entityClass) {
		historyDAO.deleteHistoryForObject(objectId, entityClass);
	}

	public <E extends IdableModel> List<HistoryModel> historyForObject(final Long id, final Class<E> entityClass) {
		return historyDAO.historyForObject(id, entityClass);
	}
}

package nz.co.fit.projectmanagement.server.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import io.dropwizard.hibernate.AbstractDAO;
import nz.co.fit.projectmanagement.server.dao.entities.HistoryModel;
import nz.co.fit.projectmanagement.server.dao.entities.IdableModel;

@Service
public class HistoryDAO extends AbstractDAO<HistoryModel> {

	@Inject
	public HistoryDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public HistoryModel create(final HistoryModel history) throws DAOException {
		if (history.getId() != null) {
			throw new DAOException("History cannot be changed");
		}
		return persist(history);
	}

	public HistoryModel read(final Long id) {
		return get(id);
	}

	public void delete(final Long id) {
		final HistoryModel history = read(id);
		currentSession().delete(history);
	}

	public <E extends IdableModel> void deleteHistoryForObject(final Long id, final Class<E> entityClass) {
		final List<HistoryModel> historyForObject = historyForObject(id, entityClass);
		historyForObject.forEach(h -> currentSession().delete(h));
	}

	public <E extends IdableModel> List<HistoryModel> historyForObject(final Long id, final Class<E> entityClass) {
		final CriteriaBuilder cb = currentSession().getCriteriaBuilder();
		final CriteriaQuery<HistoryModel> query = cb.createQuery(getEntityClass());
		final Root<HistoryModel> root = query.from(getEntityClass());
		query.where(
				cb.and(cb.equal(root.get("objectId"), id), cb.equal(root.get("entityClass"), entityClass.getName())));
		query.select(root);
		return list(query);
	}
}

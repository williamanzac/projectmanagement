package nz.co.fit.projectmanagement.server.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.entities.HistoryModel;
import nz.co.fit.projectmanagement.server.dao.entities.IdableModel;

@Service
public class HistoryDAO extends BaseDAO<HistoryModel> {

	@Inject
	public HistoryDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public HistoryModel upsert(final HistoryModel history) throws DAOException {
		if (history.getId() != null) {
			throw new DAOException("History cannot be changed");
		}
		return super.upsert(history);
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

package nz.co.fit.projectmanagement.server.dao;

import java.util.Collections;
import java.util.List;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import io.dropwizard.hibernate.AbstractDAO;
import nz.co.fit.projectmanagement.server.dao.entities.IdableModel;

public abstract class BaseDAO<T extends IdableModel> extends AbstractDAO<T> {
	public BaseDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	List<String> getExcludeList() {
		return Collections.emptyList();
	}

	public T upsert(final T value) throws DAOException {
		try {
			if (value.getId() != null) {
				// update
				final T existing = read(value.getId());
				DAOUtilities.updateAndNotify(existing, value, getExcludeList());
				return persist(existing);
			}
			return persist(value);
		} catch (final HibernateException e) {
			throw new DAOException(e);
		}
	}

	public T read(final Long id) throws DAOException {
		try {
			return get(id);
		} catch (final HibernateException e) {
			throw new DAOException(e);
		}
	}

	public void delete(final Long id) throws DAOException {
		try {
			final T value = read(id);
			currentSession().delete(value);
		} catch (final HibernateException e) {
			throw new DAOException(e);
		}
	}

	public List<T> list() throws DAOException {
		try {
			final CriteriaQuery<T> query = criteriaQuery();
			final Root<T> root = query.from(getEntityClass());
			query.select(root);
			return list(query);
		} catch (final HibernateException e) {
			throw new DAOException(e);
		}
	}
}

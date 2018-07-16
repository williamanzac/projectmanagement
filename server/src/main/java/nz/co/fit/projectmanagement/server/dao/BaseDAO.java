package nz.co.fit.projectmanagement.server.dao;

import static java.util.Arrays.asList;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import io.dropwizard.hibernate.AbstractDAO;
import nz.co.fit.projectmanagement.server.core.ServiceUtilities;
import nz.co.fit.projectmanagement.server.dao.entities.BaseModel;
import nz.co.fit.projectmanagement.server.dao.entities.IdableModel;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

public abstract class BaseDAO<T extends IdableModel> extends AbstractDAO<T> {
	private final SessionFactory sessionFactory;

	public BaseDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
		this.sessionFactory = sessionFactory;
	}

	List<String> getExcludeList() {
		return asList("id", "timestamp", "createdOn", "createdBy", "updatedBy");
	}

	public T upsert(final T value) throws DAOException {
		try {
			final UserModel currentUser = new ServiceUtilities(sessionFactory).getCurrentUser();

			if (value.getId() != null) {
				// update
				final T existing = read(value.getId());
				if (currentUser != null && value instanceof BaseModel) {
					((BaseModel) value).setUpdatedBy(currentUser);
				}
				DAOUtilities.updateAndNotify(existing, value, getExcludeList());
				return persist(existing);
			} else {
				// create
				if (currentUser != null && value instanceof BaseModel) {
					((BaseModel) value).setCreatedBy(currentUser);
					((BaseModel) value).setUpdatedBy(currentUser);
					((BaseModel) value).setCreatedOn(new Date());
				}
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
			DAOUtilities.deleteHistory(value);
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

package nz.co.fit.projectmanagement.server.dao;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

@Service
public class UserDAO extends BaseDAO<UserModel> {

	@Inject
	public UserDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public UserModel findByUsername(final String username) {
		final CriteriaBuilder cb = currentSession().getCriteriaBuilder();
		final CriteriaQuery<UserModel> query = cb.createQuery(getEntityClass());
		final Root<UserModel> root = query.from(getEntityClass());
		query.where(cb.equal(root.get("email"), username));
		query.select(root);
		return uniqueResult(query);
	}
}

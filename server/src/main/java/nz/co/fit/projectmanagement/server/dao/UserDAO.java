package nz.co.fit.projectmanagement.server.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import io.dropwizard.hibernate.AbstractDAO;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

@Service
public class UserDAO extends AbstractDAO<UserModel> {

	@Inject
	public UserDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public List<UserModel> listAll() {
		final CriteriaQuery<UserModel> query = criteriaQuery();
		final Root<UserModel> root = query.from(getEntityClass());
		query.select(root);
		return list(query);
	}

	public UserModel upsert(final UserModel user) {
		return persist(user);
	}

	public UserModel read(final Long id) {
		return get(id);
	}

	public void delete(final Long id) {
		final UserModel user = read(id);
		currentSession().remove(user);
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

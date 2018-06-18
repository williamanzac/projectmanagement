package nz.co.fit.projectmanagement.server.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.entities.GroupModel;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

@Service
public class GroupDAO extends BaseDAO<GroupModel> {

	@Inject
	public GroupDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public List<GroupModel> listGroupsForUser(final Long userId) throws DAOException {
		final CriteriaBuilder cb = currentSession().getCriteriaBuilder();
		final CriteriaQuery<GroupModel> query = cb.createQuery(getEntityClass());
		final Root<GroupModel> roleRoot = query.from(getEntityClass());
		final Root<UserModel> userRoot = query.from(UserModel.class);
		query.where(cb.equal(userRoot.get("id"), userId));
		query.select(roleRoot);

		return list(query);
	}
}

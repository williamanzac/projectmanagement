package nz.co.fit.projectmanagement.server.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.entities.GroupModel;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectModel;
import nz.co.fit.projectmanagement.server.dao.entities.RoleModel;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

@Service
public class RoleDAO extends BaseDAO<RoleModel> {

	@Inject
	public RoleDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public List<RoleModel> getRolesForProject(final Long projectId) throws DAOException {
		if (projectId == null) {
			return list();
		}

		final CriteriaBuilder cb = currentSession().getCriteriaBuilder();
		final CriteriaQuery<RoleModel> query = cb.createQuery(getEntityClass());
		final Root<RoleModel> roleRoot = query.from(getEntityClass());
		final Root<ProjectModel> projectRoot = query.from(ProjectModel.class);
		query.where(cb.equal(projectRoot.get("id"), projectId));
		query.select(roleRoot);

		return list(query);
	}

	public List<RoleModel> listRolesForUser(final Long userId) throws DAOException {
		final CriteriaBuilder cb = currentSession().getCriteriaBuilder();
		final CriteriaQuery<RoleModel> query = cb.createQuery(getEntityClass());
		final Root<RoleModel> roleRoot = query.from(getEntityClass());
		final Root<UserModel> userRoot = query.from(UserModel.class);
		query.where(cb.equal(userRoot.get("id"), userId));
		query.select(roleRoot);

		return list(query);
	}

	public List<RoleModel> listRolesForGroup(final Long groupId) throws DAOException {
		final CriteriaBuilder cb = currentSession().getCriteriaBuilder();
		final CriteriaQuery<RoleModel> query = cb.createQuery(getEntityClass());
		final Root<RoleModel> roleRoot = query.from(getEntityClass());
		final Root<GroupModel> groupRoot = query.from(GroupModel.class);
		query.where(cb.equal(groupRoot.get("id"), groupId));
		query.select(roleRoot);

		return list(query);
	}
}

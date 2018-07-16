package nz.co.fit.projectmanagement.server.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.entities.IssueModel;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectModel;

@Service
public class IssueDAO extends BaseDAO<IssueModel> {

	@Inject
	public IssueDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public List<IssueModel> listIssuesByProject(final Long projectId) throws DAOException {
		if (projectId == null) {
			return list();
		}

		final CriteriaBuilder cb = currentSession().getCriteriaBuilder();
		final CriteriaQuery<IssueModel> query = cb.createQuery(getEntityClass());
		final Root<IssueModel> issueRoot = query.from(getEntityClass());
		final Root<ProjectModel> projectRoot = query.from(ProjectModel.class);
		query.where(cb.equal(projectRoot.get("id"), projectId));
		query.select(issueRoot);

		return list(query);
	}
}

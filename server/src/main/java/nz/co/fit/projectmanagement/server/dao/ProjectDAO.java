package nz.co.fit.projectmanagement.server.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import io.dropwizard.hibernate.AbstractDAO;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectCategoryModel;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectModel;

@Service
public class ProjectDAO extends AbstractDAO<ProjectModel> {

	@Inject
	public ProjectDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public List<ProjectModel> listAll() {
		final CriteriaQuery<ProjectModel> query = criteriaQuery();
		final Root<ProjectModel> root = query.from(getEntityClass());
		query.select(root);
		return list(query);
	}

	public ProjectModel upsert(final ProjectModel project) {
		return persist(project);
	}

	public ProjectModel read(final Long id) {
		return get(id);
	}

	public void delete(final Long id) {
		final ProjectModel project = read(id);
		currentSession().remove(project);
	}

	public List<ProjectModel> listProjectsForCategory(final Long categoryId) {
		if (categoryId == null) {
			return listAll();
		}

		final CriteriaBuilder cb = currentSession().getCriteriaBuilder();
		final CriteriaQuery<ProjectModel> query = cb.createQuery(getEntityClass());
		final Root<ProjectModel> projectRoot = query.from(getEntityClass());
		final Root<ProjectCategoryModel> categoryRoot = query.from(ProjectCategoryModel.class);
		query.where(cb.equal(categoryRoot.get("id"), categoryId));
		query.select(projectRoot);

		return list(query);
	}
}

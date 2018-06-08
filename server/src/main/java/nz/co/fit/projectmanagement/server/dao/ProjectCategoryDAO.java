package nz.co.fit.projectmanagement.server.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import io.dropwizard.hibernate.AbstractDAO;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectCategoryModel;

@Service
public class ProjectCategoryDAO extends AbstractDAO<ProjectCategoryModel> {

	@Inject
	public ProjectCategoryDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public List<ProjectCategoryModel> listAll() {
		final CriteriaQuery<ProjectCategoryModel> query = criteriaQuery();
		final Root<ProjectCategoryModel> root = query.from(getEntityClass());
		query.select(root);
		return list(query);
	}

	public ProjectCategoryModel upsert(final ProjectCategoryModel category) {
		return persist(category);
	}

	public ProjectCategoryModel read(final Long id) {
		return get(id);
	}

	public void delete(final Long id) {
		final ProjectCategoryModel category = read(id);
		currentSession().remove(category);
	}
}

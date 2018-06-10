package nz.co.fit.projectmanagement.server.dao;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.entities.ProjectCategoryModel;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectModel;

@Service
public class ProjectDAO extends BaseDAO<ProjectModel> {

	@Inject
	public ProjectDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	List<String> getExcludeList() {
		// exclude the components and versions lists from the history
		final List<String> existingList = super.getExcludeList();
		final List<String> additionalList = asList("components", "versions");
		return Stream.of(existingList, additionalList).flatMap(Collection::stream).collect(toList());
	}

	public List<ProjectModel> listProjectsForCategory(final Long categoryId) throws DAOException {
		if (categoryId == null) {
			return list();
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

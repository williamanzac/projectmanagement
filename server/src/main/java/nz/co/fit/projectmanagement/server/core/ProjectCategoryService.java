package nz.co.fit.projectmanagement.server.core;

import java.util.List;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.ProjectCategoryDAO;
import nz.co.fit.projectmanagement.server.dao.ProjectDAO;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectCategoryModel;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectModel;

@Service
public class ProjectCategoryService {

	private final ProjectCategoryDAO categoryDAO;
	private final ProjectDAO projectDAO;

	@Inject
	public ProjectCategoryService(final ProjectCategoryDAO categoryDAO, final ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
		this.categoryDAO = categoryDAO;
	}

	public List<ProjectCategoryModel> listAll() {
		return categoryDAO.listAll();
	}

	public ProjectCategoryModel createCategory(final ProjectCategoryModel category) {
		return categoryDAO.upsert(category);
	}

	public ProjectCategoryModel readCategory(final Long id) {
		return categoryDAO.read(id);
	}

	public ProjectCategoryModel updateCategory(final ProjectCategoryModel category) {
		return categoryDAO.upsert(category);
	}

	public void deleteCategory(final Long id) throws ServiceException {
		final List<ProjectModel> listProjectsForCategory = projectDAO.listProjectsForCategory(id);
		if (!listProjectsForCategory.isEmpty()) {
			throw new ServiceException("There are projects assigned to this category.");
		}
		categoryDAO.delete(id);
	}
}

package nz.co.fit.projectmanagement.server.core;

import java.util.List;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.DAOException;
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

	public List<ProjectCategoryModel> listAll() throws ServiceException {
		try {
			return categoryDAO.list();
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public ProjectCategoryModel createCategory(final ProjectCategoryModel category) throws ServiceException {
		try {
			return categoryDAO.upsert(category);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public ProjectCategoryModel readCategory(final Long id) throws ServiceException {
		try {
			return categoryDAO.read(id);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public ProjectCategoryModel updateCategory(final ProjectCategoryModel category) throws ServiceException {
		try {
			return categoryDAO.upsert(category);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public void deleteCategory(final Long id) throws ServiceException {
		List<ProjectModel> listProjectsForCategory;
		try {
			listProjectsForCategory = projectDAO.listProjectsForCategory(id);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
		if (!listProjectsForCategory.isEmpty()) {
			throw new ServiceException("There are projects assigned to this category.");
		}
		try {
			categoryDAO.delete(id);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}
}

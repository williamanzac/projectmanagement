package nz.co.fit.projectmanagement.server.core;

import java.util.List;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.DAOException;
import nz.co.fit.projectmanagement.server.dao.ProjectCategoryDAO;
import nz.co.fit.projectmanagement.server.dao.ProjectDAO;
import nz.co.fit.projectmanagement.server.dao.UserDAO;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectCategoryModel;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectModel;

@Service
public class ProjectCategoryService extends CRUDLService<ProjectCategoryModel> {

	private final ProjectDAO projectDAO;

	@Inject
	public ProjectCategoryService(final ProjectCategoryDAO categoryDAO, final ProjectDAO projectDAO,
			final HistoryService historyService, final UserDAO userDAO) {
		super(categoryDAO, historyService, userDAO);
		this.projectDAO = projectDAO;
	}

	@Override
	public void delete(final Long id) throws ServiceException {
		try {
			// check if there are projects assigned to this category
			final List<ProjectModel> listProjectsForCategory = projectDAO.listProjectsForCategory(id);
			if (!listProjectsForCategory.isEmpty()) {
				throw new ServiceException("There are projects assigned to this category.");
			}
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
		super.delete(id);
	}
}

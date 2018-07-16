package nz.co.fit.projectmanagement.server.core;

import java.util.List;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.ProjectCategoryDAO;
import nz.co.fit.projectmanagement.server.dao.UserDAO;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectCategoryModel;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectModel;

@Service
public class ProjectCategoryService extends CRUDLService<ProjectCategoryModel> {

	private final ProjectService projectService;

	@Inject
	public ProjectCategoryService(final ProjectCategoryDAO categoryDAO, final ProjectService projectService,
			final UserDAO userDAO) {
		super(categoryDAO);
		this.projectService = projectService;
	}

	@Override
	public void delete(final Long id) throws ServiceException {
		// check if there are projects assigned to this category
		final List<ProjectModel> listProjectsForCategory = projectService.listProjectsForCategory(id);
		if (!listProjectsForCategory.isEmpty()) {
			throw new ServiceException("There are projects assigned to this category.");
		}
		super.delete(id);
	}
}

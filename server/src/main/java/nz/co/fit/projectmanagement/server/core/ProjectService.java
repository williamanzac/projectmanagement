package nz.co.fit.projectmanagement.server.core;

import java.util.List;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.DAOException;
import nz.co.fit.projectmanagement.server.dao.ProjectDAO;
import nz.co.fit.projectmanagement.server.dao.UserDAO;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectCategoryModel;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectModel;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

@Service
public class ProjectService extends CRUDLService<ProjectModel> {

	private final VersionService versionService;
	private final ComponentService componentService;
	private final ProjectCategoryService categoryService;
	private final UserService userService;

	@Inject
	public ProjectService(final ProjectDAO projectDAO, final VersionService versionService,
			final ComponentService componentService, final ProjectCategoryService categoryService,
			final UserService userService, final HistoryService historyService, final UserDAO userDAO) {
		super(projectDAO, historyService, userDAO);
		this.versionService = versionService;
		this.componentService = componentService;
		this.categoryService = categoryService;
		this.userService = userService;
	}

	@Override
	public ProjectModel create(final ProjectModel project) throws ServiceException {
		// the data from the REST layer will only have the id so we will need to find the actual object
		if (project.getCategory() != null) {
			final ProjectCategoryModel category = categoryService.read(project.getCategory().getId());
			project.setCategory(category);
		}
		if (project.getProjectLead() != null) {
			final UserModel user = userService.read(project.getProjectLead().getId());
			project.setProjectLead(user);
		}
		return super.create(project);
	}

	@Override
	public ProjectModel update(final ProjectModel project) throws ServiceException {
		if (!project.getVersions().isEmpty()) {
			// some versions have been passed in, so make sure that they are upserted first.
			project.getVersions().forEach(v -> {
				try {
					if (v.getId() != null) {
						versionService.update(v);
					} else {
						versionService.create(v);
					}
				} catch (final ServiceException e) {
					e.printStackTrace();
				}
			});
		}
		if (!project.getComponents().isEmpty()) {
			// some components have been passed in, so make sure that they are upserted first.
			project.getComponents().forEach(c -> {
				try {
					if (c.getId() != null) {
						componentService.update(c);
					} else {
						componentService.create(c);
					}
				} catch (final ServiceException e) {
					e.printStackTrace();
				}
			});
		}

		// the data from the api will only be the id, so we need to read the whole object
		if (project.getCategory() != null) {
			final ProjectCategoryModel category = categoryService.read(project.getCategory().getId());
			project.setCategory(category);
		}
		if (project.getProjectLead() != null) {
			final UserModel user = userService.read(project.getProjectLead().getId());
			project.setProjectLead(user);
		}

		return super.update(project);
	}

	@Override
	public void delete(final Long id) throws ServiceException {
		ProjectModel existingProject;
		try {
			existingProject = dao.read(id);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
		// delete the versions, components
		existingProject.getVersions().forEach(v -> {
			try {
				versionService.delete(v.getId());
			} catch (final ServiceException e2) {
				e2.printStackTrace();
			}
		});
		existingProject.getComponents().forEach(c -> {
			try {
				componentService.delete(c.getId());
			} catch (final ServiceException e1) {
				e1.printStackTrace();
			}
		});

		super.delete(id);
	}

	public List<ProjectModel> listProjectsForCategory(final Long categoryId) throws ServiceException {
		try {
			return ((ProjectDAO) dao).listProjectsForCategory(categoryId);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}
}

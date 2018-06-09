package nz.co.fit.projectmanagement.server.core;

import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.core.SecurityContext;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.DAOException;
import nz.co.fit.projectmanagement.server.dao.ProjectDAO;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectCategoryModel;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectModel;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

@Service
public class ProjectService {

	private final ProjectDAO projectDAO;
	private final VersionService versionService;
	private final ComponentService componentService;
	private final ProjectCategoryService categoryService;
	private final UserService userService;
	private final HistoryService historyService;

	@Resource
	SecurityContext securityContext;

	@Inject
	public ProjectService(final ProjectDAO projectDAO, final VersionService versionService,
			final ComponentService componentService, final ProjectCategoryService categoryService,
			final UserService userService, final HistoryService historyService) {
		this.projectDAO = projectDAO;
		this.versionService = versionService;
		this.componentService = componentService;
		this.categoryService = categoryService;
		this.userService = userService;
		this.historyService = historyService;
	}

	public List<ProjectModel> listProjects() throws ServiceException {
		try {
			return projectDAO.list();
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public ProjectModel createProject(final ProjectModel project) throws ServiceException {
		// assume the project does not have an id
		// if (!project.getVersions().isEmpty()) {
		// // some versions have been passed in, so make sure that they are upserted first.
		// project.getVersions().forEach(v -> versionService.updateVersion(v));
		// }
		// if (!project.getComponents().isEmpty()) {
		// // some versions have been passed in, so make sure that they are upserted first.
		// project.getComponents().forEach(c -> componentService.upsert(c));
		// }
		if (securityContext != null) {
			securityContext.getUserPrincipal();
		}
		if (project.getCategory() != null) {
			final ProjectCategoryModel category = categoryService.readCategory(project.getCategory().getId());
			project.setCategory(category);
		}
		if (project.getProjectLead() != null) {
			final UserModel user = userService.readUser(project.getProjectLead().getId());
			project.setProjectLead(user);
		}
		try {
			return projectDAO.upsert(project);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public ProjectModel readProject(final Long id) throws ServiceException {
		try {
			return projectDAO.read(id);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public ProjectModel updateProject(final ProjectModel project) throws ServiceException {
		// assume the project does not have an id
		if (!project.getVersions().isEmpty()) {
			// some versions have been passed in, so make sure that they are upserted first.
			project.getVersions().forEach(v -> {
				try {
					if (v.getId() != null) {
						versionService.updateVersion(v);
					} else {
						versionService.createVersion(v);
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
						componentService.updateComponent(c);
					} else {
						componentService.createComponent(c);
					}
				} catch (final ServiceException e) {
					e.printStackTrace();
				}
			});
		}

		// the data from the api will only be the id, so we need to read the whole object
		if (project.getCategory() != null) {
			final ProjectCategoryModel category = categoryService.readCategory(project.getCategory().getId());
			project.setCategory(category);
		}
		if (project.getProjectLead() != null) {
			final UserModel user = userService.readUser(project.getProjectLead().getId());
			project.setProjectLead(user);
		}

		try {
			return projectDAO.upsert(project);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public void deleteProject(final Long id) throws ServiceException {
		ProjectModel existingProject;
		try {
			existingProject = projectDAO.read(id);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
		// delete the versions, components and history first
		existingProject.getVersions().forEach(v -> {
			try {
				versionService.deleteVersion(v.getId());
			} catch (final ServiceException e2) {
				e2.printStackTrace();
			}
		});
		existingProject.getComponents().forEach(c -> {
			try {
				componentService.deleteComponent(c.getId());
			} catch (final ServiceException e1) {
				e1.printStackTrace();
			}
		});
		historyService.deleteHistoryForObject(id, ProjectModel.class);
		try {
			projectDAO.delete(id);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public List<ProjectModel> listProjectsForCategory(final Long categoryId) throws ServiceException {
		try {
			return projectDAO.listProjectsForCategory(categoryId);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}
}

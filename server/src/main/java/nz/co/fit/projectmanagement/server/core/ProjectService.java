package nz.co.fit.projectmanagement.server.core;

import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.core.SecurityContext;

import org.greenrobot.eventbus.EventBus;
import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.HistoryEvent;
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

	public List<ProjectModel> listAll() {
		return projectDAO.listAll();
	}

	public ProjectModel createProject(final ProjectModel project) {
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
		return projectDAO.upsert(project);
	}

	public ProjectModel readProject(final Long id) {
		return projectDAO.read(id);
	}

	public ProjectModel updateProject(final ProjectModel project) {
		// assume the project does not have an id
		if (!project.getVersions().isEmpty()) {
			// some versions have been passed in, so make sure that they are upserted first.
			project.getVersions().forEach(v -> {
				if (v.getId() != null) {
					versionService.updateVersion(v);
				} else {
					versionService.createVersion(v);
				}
			});
		}
		if (!project.getComponents().isEmpty()) {
			// some versions have been passed in, so make sure that they are upserted first.
			project.getComponents().forEach(c -> {
				if (c.getId() != null) {
					componentService.updateComponent(c);
				} else {
					componentService.createComponent(c);
				}
			});
		}

		final ProjectModel existingProject = projectDAO.read(project.getId());
		// existingProject.setVersions(project.getVersions());
		// existingProject.setComponents(project.getComponents());

		if (project.getCategory() != null) {
			final ProjectCategoryModel category = categoryService.readCategory(project.getCategory().getId());
			if (category != existingProject.getCategory()) {
				final String oldValue = existingProject.getCategory() != null
						? String.valueOf(existingProject.getCategory().getId())
						: null;
				final String newValue = category != null ? String.valueOf(category.getId()) : null;
				EventBus.getDefault().post(new HistoryEvent("category", oldValue, newValue, project));
			}
			existingProject.setCategory(category);
		}
		if (project.getProjectLead() != null) {
			final UserModel user = userService.readUser(project.getProjectLead().getId());
			if (user != existingProject.getProjectLead()) {
				final String oldValue = existingProject.getProjectLead() != null
						? String.valueOf(existingProject.getProjectLead().getId())
						: null;
				final String newValue = user != null ? String.valueOf(user.getId()) : null;
				EventBus.getDefault().post(new HistoryEvent("projectLead", oldValue, newValue, project));
			}
			existingProject.setProjectLead(user);
		}
		if (project.getDescription() != null) {
			if (!project.getDescription().equals(existingProject.getDescription())) {
				EventBus.getDefault().post(new HistoryEvent("description", existingProject.getDescription(),
						project.getDescription(), project));
			}
			existingProject.setDescription(project.getDescription());
		}
		if (project.getName() != null) {
			if (!project.getName().equals(existingProject.getName())) {
				EventBus.getDefault()
						.post(new HistoryEvent("name", existingProject.getName(), project.getName(), project));
			}
			existingProject.setName(project.getName());
		}
		if (project.getUrl() != null) {
			if (!project.getUrl().equals(existingProject.getUrl())) {
				EventBus.getDefault()
						.post(new HistoryEvent("url", existingProject.getUrl(), project.getUrl(), project));
			}
			existingProject.setUrl(project.getUrl());
		}
		return projectDAO.upsert(existingProject);
	}

	public void deleteProject(final Long id) {
		final ProjectModel existingProject = projectDAO.read(id);
		// delete the versions, components and history first
		existingProject.getVersions().forEach(v -> versionService.deleteVersion(v.getId()));
		existingProject.getComponents().forEach(c -> componentService.deleteComponent(c.getId()));
		historyService.deleteHistoryForObject(id, ProjectModel.class);
		projectDAO.delete(id);
	}

	public List<ProjectModel> listProjectsForCategory(final Long categoryId) {
		return projectDAO.listProjectsForCategory(categoryId);
	}
}

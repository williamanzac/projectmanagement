package nz.co.fit.projectmanagement.server.resources;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.dropwizard.hibernate.UnitOfWork;
import nz.co.fit.projectmanagement.server.api.BaseIdable;
import nz.co.fit.projectmanagement.server.api.Component;
import nz.co.fit.projectmanagement.server.api.History;
import nz.co.fit.projectmanagement.server.api.Project;
import nz.co.fit.projectmanagement.server.api.Version;
import nz.co.fit.projectmanagement.server.core.ComponentService;
import nz.co.fit.projectmanagement.server.core.HistoryService;
import nz.co.fit.projectmanagement.server.core.ProjectService;
import nz.co.fit.projectmanagement.server.core.VersionService;
import nz.co.fit.projectmanagement.server.dao.entities.ComponentModel;
import nz.co.fit.projectmanagement.server.dao.entities.HistoryModel;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectModel;
import nz.co.fit.projectmanagement.server.dao.entities.VersionModel;

@Path("/projects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjectsResource {

	private final ProjectService projectService;
	private final VersionService versionService;
	private final ComponentService componentService;
	private final HistoryService historyService;

	@Inject
	public ProjectsResource(final ProjectService projectService, final VersionService versionService,
			final ComponentService componentService, final HistoryService historyService) {
		this.projectService = projectService;
		this.versionService = versionService;
		this.componentService = componentService;
		this.historyService = historyService;
	}

	@GET
	@PermitAll
	@UnitOfWork
	public List<BaseIdable> listAllProjects() throws ResourceException {
		return projectService.listAll().stream().map(ModelUtilities::toIdable).collect(toList());
	}

	@POST
	@UnitOfWork
	public Project createProject(final Project project) throws ResourceException {
		final ProjectModel model = ModelUtilities.convert(project, ProjectModel.class);
		final ProjectModel createProject = projectService.createProject(model);
		final Project retProject = ModelUtilities.convert(createProject, Project.class);
		return retProject;
	}

	@PUT
	@Path("/{id}")
	@UnitOfWork
	public Project updateProject(final @PathParam("id") Long id, final Project project) throws ResourceException {
		project.setId(id); // use the id from the path as the id field will be ignored from the JSON
		final ProjectModel model = ModelUtilities.convert(project, ProjectModel.class);
		final ProjectModel createProject = projectService.updateProject(model);
		final Project retProject = ModelUtilities.convert(createProject, Project.class);
		return retProject;
	}

	@GET
	@Path("/{id}")
	@UnitOfWork
	public Project readProject(final @PathParam("id") Long id) throws ResourceException {
		final ProjectModel project = projectService.readProject(id);
		final Project retProject = ModelUtilities.convert(project, Project.class);
		return retProject;
	}

	@DELETE
	@Path("/{id}")
	@UnitOfWork
	public void deleteProject(final @PathParam("id") Long id) {
		projectService.deleteProject(id);
	}

	@GET
	@Path("/{id}/history")
	@UnitOfWork
	public List<History> listProjectHistory(final @PathParam("id") Long id) {
		final List<HistoryModel> historyForObject = historyService.historyForObject(id, ProjectModel.class);
		return historyForObject.stream().map(h -> {
			try {
				return ModelUtilities.convert(h, History.class);
			} catch (final ResourceException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(toList());
	}

	@GET
	@Path("/{id}/versions")
	@PermitAll
	@UnitOfWork
	public List<BaseIdable> listAllVersions(final @PathParam("id") Long projectId) throws ResourceException {
		return projectService.readProject(projectId).getVersions().stream().map(ModelUtilities::toIdable)
				.collect(toList());
	}

	@POST
	@Path("/{id}/versions")
	@UnitOfWork
	public Version createVersion(final @PathParam("id") Long projectId, final Version version)
			throws ResourceException {
		final ProjectModel projModel = projectService.readProject(projectId);
		final List<VersionModel> versions = projModel.getVersions();
		final long matchingName = versions.stream().filter(v -> v.getName().equals(version.getName())).count();
		if (matchingName > 0) {
			throw new ResourceException("There is already a version '" + version.getName() + "' for this project.");
		}

		final VersionModel model = ModelUtilities.convert(version, VersionModel.class);
		final int length = versions.size();
		model.setPriority(length + 1);
		versions.add(model);
		final VersionModel createVersion = versionService.createVersion(model);
		projectService.updateProject(projModel);
		final Version retVersion = ModelUtilities.convert(createVersion, Version.class);
		return retVersion;
	}

	@GET
	@Path("/{id}/components")
	@PermitAll
	@UnitOfWork
	public List<BaseIdable> listAllComponents(final @PathParam("id") Long projectId) throws ResourceException {
		return projectService.readProject(projectId).getComponents().stream().map(ModelUtilities::toIdable)
				.collect(toList());
	}

	@POST
	@Path("/{id}/components")
	@UnitOfWork
	public Component createComponent(final @PathParam("id") Long projectId, final Component component)
			throws ResourceException {
		final ProjectModel projModel = projectService.readProject(projectId);
		final List<ComponentModel> components = projModel.getComponents();
		final long matchingName = components.stream().filter(v -> v.getName().equals(component.getName())).count();
		if (matchingName > 0) {
			throw new ResourceException("There is already a component '" + component.getName() + "' for this project.");
		}

		final ComponentModel model = ModelUtilities.convert(component, ComponentModel.class);
		components.add(model);
		final ComponentModel createComponent = componentService.createComponent(model);
		projectService.updateProject(projModel);
		final Component retComponent = ModelUtilities.convert(createComponent, Component.class);
		return retComponent;
	}
}

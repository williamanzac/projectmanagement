package nz.co.fit.projectmanagement.server.resources;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.dropwizard.hibernate.UnitOfWork;
import nz.co.fit.projectmanagement.server.api.BaseIdable;
import nz.co.fit.projectmanagement.server.api.Component;
import nz.co.fit.projectmanagement.server.api.Project;
import nz.co.fit.projectmanagement.server.api.Version;
import nz.co.fit.projectmanagement.server.core.ComponentService;
import nz.co.fit.projectmanagement.server.core.HistoryService;
import nz.co.fit.projectmanagement.server.core.ProjectService;
import nz.co.fit.projectmanagement.server.core.ServiceException;
import nz.co.fit.projectmanagement.server.core.VersionService;
import nz.co.fit.projectmanagement.server.dao.entities.ComponentModel;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectModel;
import nz.co.fit.projectmanagement.server.dao.entities.VersionModel;

@Path("/projects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
public class ProjectsResource extends CRUDLResource<Project, ProjectModel> {

	private final VersionService versionService;
	private final ComponentService componentService;

	@Inject
	public ProjectsResource(final ProjectService projectService, final VersionService versionService,
			final ComponentService componentService, final HistoryService historyService) {
		super(projectService, historyService);
		this.versionService = versionService;
		this.componentService = componentService;
	}

	@GET
	@Path("/{id}/versions")
	@UnitOfWork
	public List<BaseIdable> listVersions(final @PathParam("id") Long projectId) throws ResourceException {
		try {
			return service.read(projectId).getVersions().stream().map(ModelUtilities::toIdable).collect(toList());
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
	}

	@POST
	@Path("/{id}/versions")
	@UnitOfWork
	public Version createVersion(final @PathParam("id") Long projectId, final Version version)
			throws ResourceException {
		ProjectModel projModel;
		try {
			projModel = service.read(projectId);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		final List<VersionModel> versions = projModel.getVersions();
		final long matchingName = versions.stream().filter(v -> v.getName().equals(version.getName())).count();
		if (matchingName > 0) {
			throw new ResourceException("There is already a version '" + version.getName() + "' for this project.");
		}

		final VersionModel model = ModelUtilities.convert(version, VersionModel.class);
		final int length = versions.size();
		model.setPriority(length + 1);
		versions.add(model);
		final VersionModel createVersion;
		try {
			createVersion = versionService.create(model);
			service.update(projModel);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		final Version retVersion = ModelUtilities.convert(createVersion, Version.class);
		return retVersion;
	}

	@GET
	@Path("/{id}/components")
	@PermitAll
	@UnitOfWork
	public List<BaseIdable> listComponents(final @PathParam("id") Long projectId) throws ResourceException {
		try {
			return service.read(projectId).getComponents().stream().map(ModelUtilities::toIdable).collect(toList());
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
	}

	@POST
	@Path("/{id}/components")
	@UnitOfWork
	public Component createComponent(final @PathParam("id") Long projectId, final Component component)
			throws ResourceException {
		ProjectModel projModel;
		try {
			projModel = service.read(projectId);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		final List<ComponentModel> components = projModel.getComponents();
		final long matchingName = components.stream().filter(v -> v.getName().equals(component.getName())).count();
		if (matchingName > 0) {
			throw new ResourceException("There is already a component '" + component.getName() + "' for this project.");
		}

		final ComponentModel model = ModelUtilities.convert(component, ComponentModel.class);
		components.add(model);
		final ComponentModel createComponent;
		try {
			createComponent = componentService.create(model);
			service.update(projModel);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		final Component retComponent = ModelUtilities.convert(createComponent, Component.class);
		return retComponent;
	}
}

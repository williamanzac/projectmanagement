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
import nz.co.fit.projectmanagement.server.api.Role;
import nz.co.fit.projectmanagement.server.api.Version;
import nz.co.fit.projectmanagement.server.core.HistoryService;
import nz.co.fit.projectmanagement.server.core.ProjectService;
import nz.co.fit.projectmanagement.server.core.RoleService;
import nz.co.fit.projectmanagement.server.core.ServiceException;
import nz.co.fit.projectmanagement.server.dao.entities.ComponentModel;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectModel;
import nz.co.fit.projectmanagement.server.dao.entities.RoleModel;
import nz.co.fit.projectmanagement.server.dao.entities.VersionModel;

@Path("/projects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
public class ProjectsResource extends CRUDLResource<Project, ProjectModel> {

	private final RoleService roleService;

	@Inject
	public ProjectsResource(final ProjectService projectService, final HistoryService historyService,
			final RoleService roleService) {
		super(projectService, historyService);
		this.roleService = roleService;
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
		final VersionModel model = ModelUtilities.convert(version, VersionModel.class);
		VersionModel createVersion;
		try {
			createVersion = ((ProjectService) service).addVersionToProject(projectId, model);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		final Version retVersion = ModelUtilities.convert(createVersion, Version.class);
		return retVersion;
	}

	@GET
	@Path("/{id}/components")
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
		final ComponentModel model = ModelUtilities.convert(component, ComponentModel.class);
		ComponentModel createComponent;
		try {
			createComponent = ((ProjectService) service).addComponentToProject(projectId, model);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		final Component retComponent = ModelUtilities.convert(createComponent, Component.class);
		return retComponent;
	}

	@GET
	@Path("/{id}/roles")
	@UnitOfWork
	public List<BaseIdable> listRoles(final @PathParam("id") Long projectId) throws ResourceException {
		try {
			return roleService.getRolesForProject(projectId).stream().map(ModelUtilities::toIdable).collect(toList());
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
	}

	@POST
	@Path("/{id}/roles")
	@UnitOfWork
	public Role createRole(final @PathParam("id") Long projectId, final Role role) throws ResourceException {
		final RoleModel model = ModelUtilities.convert(role, RoleModel.class);
		final RoleModel createRole;
		try {
			createRole = ((ProjectService) service).addRoleToProject(projectId, model);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		final Role retRole = ModelUtilities.convert(createRole, Role.class);
		return retRole;
	}

	@GET
	@Path("/{id}/users")
	@UnitOfWork
	public List<BaseIdable> listUsers(final @PathParam("id") Long projectId) throws ResourceException {
		try {
			return ((ProjectService) service).listUsersForProject(projectId).stream().map(ModelUtilities::toIdable)
					.collect(toList());
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
	}
}

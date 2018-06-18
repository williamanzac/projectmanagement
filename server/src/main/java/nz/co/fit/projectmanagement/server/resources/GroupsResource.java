package nz.co.fit.projectmanagement.server.resources;

import static java.util.stream.Collectors.toList;
import static nz.co.fit.projectmanagement.server.resources.ModelUtilities.toIdable;

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
import nz.co.fit.projectmanagement.server.api.Group;
import nz.co.fit.projectmanagement.server.core.GroupService;
import nz.co.fit.projectmanagement.server.core.HistoryService;
import nz.co.fit.projectmanagement.server.core.RoleService;
import nz.co.fit.projectmanagement.server.core.ServiceException;
import nz.co.fit.projectmanagement.server.dao.entities.GroupModel;
import nz.co.fit.projectmanagement.server.dao.entities.RoleModel;

@Path("/groups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
public class GroupsResource extends CRUDLResource<Group, GroupModel> {

	private final RoleService roleService;

	@Inject
	public GroupsResource(final GroupService versionService, final HistoryService historyService,
			final RoleService roleService) {
		super(versionService, historyService);
		this.roleService = roleService;
	}

	@GET
	@Path("/{id}/users")
	@UnitOfWork
	public List<BaseIdable> listUsers(final @PathParam("id") Long roleId) throws ResourceException {
		try {
			return service.read(roleId).getUsers().stream().map(ModelUtilities::toIdable).collect(toList());
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
	}

	@POST
	@Path("/{id}/users/{userId}")
	@UnitOfWork
	public Group addUserToGroup(final @PathParam("id") Long roleId, final @PathParam("userId") Long userId)
			throws ResourceException {
		final GroupModel roleModel;
		try {
			roleModel = ((GroupService) service).addUserToGroup(roleId, userId);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		return ModelUtilities.convert(roleModel, Group.class);
	}

	@GET
	@Path("/{id}/roles")
	public List<BaseIdable> listRoles(final @PathParam("id") Long groupId) throws ResourceException {
		final List<RoleModel> rolesForGroup;
		try {
			rolesForGroup = roleService.listRolesForGroup(groupId);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		return rolesForGroup.stream().map(ModelUtilities::toIdable).collect(toList());
	}

	@POST
	@Path("/{id}/roles/{roleId}")
	@UnitOfWork
	public BaseIdable addGroupToRole(final @PathParam("id") Long groupId, final @PathParam("roleId") Long roleId)
			throws ResourceException {
		final RoleModel roleModel;
		try {
			roleModel = roleService.addGroupToRole(roleId, groupId);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		return toIdable(roleModel);
	}
}

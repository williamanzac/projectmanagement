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
import nz.co.fit.projectmanagement.server.api.User;
import nz.co.fit.projectmanagement.server.core.HistoryService;
import nz.co.fit.projectmanagement.server.core.RoleService;
import nz.co.fit.projectmanagement.server.core.ServiceException;
import nz.co.fit.projectmanagement.server.core.UserService;
import nz.co.fit.projectmanagement.server.dao.entities.RoleModel;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
public class UsersResource extends CRUDLResource<User, UserModel> {

	private final RoleService roleService;

	@Inject
	public UsersResource(final UserService userService, final HistoryService historyService,
			final RoleService roleService) {
		super(userService, historyService);
		this.roleService = roleService;
	}

	@GET
	@Path("/{id}/roles")
	public List<BaseIdable> listRoles(final @PathParam("id") Long userId) throws ResourceException {
		final List<RoleModel> rolesForUser;
		try {
			rolesForUser = roleService.listRolesForUser(userId);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		return rolesForUser.stream().map(ModelUtilities::toIdable).collect(toList());
	}

	@POST
	@Path("/{id}/roles/{roleId}")
	@UnitOfWork
	public BaseIdable addUserToRole(final @PathParam("id") Long userId, final @PathParam("roleId") Long roleId)
			throws ResourceException {
		final RoleModel roleModel;
		try {
			roleModel = roleService.addUserToRole(roleId, userId);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		return toIdable(roleModel);
	}
}

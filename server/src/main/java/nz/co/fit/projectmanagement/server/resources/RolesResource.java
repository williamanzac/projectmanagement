package nz.co.fit.projectmanagement.server.resources;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.annotation.security.DenyAll;
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
import nz.co.fit.projectmanagement.server.api.Role;
import nz.co.fit.projectmanagement.server.core.HistoryService;
import nz.co.fit.projectmanagement.server.core.RoleService;
import nz.co.fit.projectmanagement.server.core.ServiceException;
import nz.co.fit.projectmanagement.server.dao.entities.RoleModel;

@Path("/versions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
public class RolesResource extends CRUDLResource<Role, RoleModel> {

	@Inject
	public RolesResource(final RoleService versionService, final HistoryService historyService) {
		super(versionService, historyService);
	}

	@POST
	@DenyAll
	@Override
	public Role create(final Role value) throws ResourceException {
		// versions should be created via the project resource
		return null;
	}

	@GET
	@DenyAll
	@Override
	public List<BaseIdable> list() throws ResourceException {
		// versions should be listed via the project resource
		return null;
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
	public Role addUserToRole(final @PathParam("id") Long roleId, final @PathParam("userId") Long userId)
			throws ResourceException {
		final RoleModel roleModel;
		try {
			roleModel = ((RoleService) service).addUserToRole(roleId, userId);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		return ModelUtilities.convert(roleModel, Role.class);
	}
}

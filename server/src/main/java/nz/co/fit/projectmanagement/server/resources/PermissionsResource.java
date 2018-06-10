package nz.co.fit.projectmanagement.server.resources;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.dropwizard.hibernate.UnitOfWork;
import nz.co.fit.projectmanagement.server.core.PermissionService;
import nz.co.fit.projectmanagement.server.core.ServiceException;
import nz.co.fit.projectmanagement.server.dao.entities.PermissionModel;

@Path("/permissions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
public class PermissionsResource {

	private final PermissionService permissionService;

	@Inject
	public PermissionsResource(final PermissionService permissionService) {
		this.permissionService = permissionService;
	}

	@GET
	@UnitOfWork
	public List<String> list() throws ResourceException {
		try {
			return permissionService.list().stream().map(PermissionModel::getName).collect(toList());
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
	}
}

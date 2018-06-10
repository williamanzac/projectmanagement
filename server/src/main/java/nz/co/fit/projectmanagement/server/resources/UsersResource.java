package nz.co.fit.projectmanagement.server.resources;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nz.co.fit.projectmanagement.server.api.User;
import nz.co.fit.projectmanagement.server.core.UserService;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
public class UsersResource extends CRUDLResource<User, UserModel> {
	@Inject
	public UsersResource(final UserService userService) {
		super(userService);
	}
}

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

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import nz.co.fit.projectmanagement.server.api.BaseIdable;
import nz.co.fit.projectmanagement.server.api.User;
import nz.co.fit.projectmanagement.server.auth.CustomAuthUser;
import nz.co.fit.projectmanagement.server.core.ServiceException;
import nz.co.fit.projectmanagement.server.core.UserService;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersResource {

	private final UserService userService;

	@Inject
	public UsersResource(final UserService userService) {
		this.userService = userService;
	}

	@GET
	@PermitAll
	@UnitOfWork
	public List<BaseIdable> listAllUsers() throws ResourceException {
		try {
			return userService.list().stream().map(ModelUtilities::toIdable).collect(toList());
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
	}

	@POST
	@UnitOfWork
	@PermitAll
	public User createUser(final User user, @Auth final CustomAuthUser authUser) throws ResourceException {
		final UserModel model = ModelUtilities.convert(user, UserModel.class);
		UserModel createUser;
		try {
			createUser = userService.create(model);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		final User retUser = ModelUtilities.convert(createUser, User.class);
		return retUser;
	}

	@PUT
	@Path("/{id}")
	@UnitOfWork
	public User updateUser(final @PathParam("id") Long id, final User user) throws ResourceException {
		user.setId(id); // use the id from the path as the id field will be ignored from the JSON
		final UserModel model = ModelUtilities.convert(user, UserModel.class);
		UserModel createUser;
		try {
			createUser = userService.update(model);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		final User retUser = ModelUtilities.convert(createUser, User.class);
		return retUser;
	}

	@GET
	@Path("/{id}")
	@UnitOfWork
	public User readUser(final @PathParam("id") Long id) throws ResourceException {
		UserModel user;
		try {
			user = userService.read(id);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		final User retUser = ModelUtilities.convert(user, User.class);
		return retUser;
	}

	@DELETE
	@Path("/{id}")
	@UnitOfWork
	public void deleteUser(final @PathParam("id") Long id) throws ResourceException {
		try {
			userService.delete(id);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
	}
}

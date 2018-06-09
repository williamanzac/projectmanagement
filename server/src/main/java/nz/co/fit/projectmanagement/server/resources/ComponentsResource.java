package nz.co.fit.projectmanagement.server.resources;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.dropwizard.hibernate.UnitOfWork;
import nz.co.fit.projectmanagement.server.api.Component;
import nz.co.fit.projectmanagement.server.core.ComponentService;
import nz.co.fit.projectmanagement.server.core.ServiceException;
import nz.co.fit.projectmanagement.server.dao.entities.ComponentModel;

@Path("/components")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ComponentsResource {

	private final ComponentService componentService;

	@Inject
	public ComponentsResource(final ComponentService componentService) {
		this.componentService = componentService;
	}

	@PUT
	@Path("/{id}")
	@UnitOfWork
	public Component updateComponent(final @PathParam("id") Long id, final Component component)
			throws ResourceException {
		component.setId(id); // use the id from the path as the id field will be ignored from the JSON
		final ComponentModel model = ModelUtilities.convert(component, ComponentModel.class);
		ComponentModel createComponent;
		try {
			createComponent = componentService.update(model);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		final Component retComponent = ModelUtilities.convert(createComponent, Component.class);
		return retComponent;
	}

	@GET
	@Path("/{id}")
	@UnitOfWork
	public Component readComponent(final @PathParam("id") Long id) throws ResourceException {
		ComponentModel component;
		try {
			component = componentService.read(id);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		final Component retComponent = ModelUtilities.convert(component, Component.class);
		return retComponent;
	}

	@DELETE
	@Path("/{id}")
	@UnitOfWork
	public void deleteComponent(final @PathParam("id") Long id) throws ResourceException {
		try {
			componentService.delete(id);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
	}
}

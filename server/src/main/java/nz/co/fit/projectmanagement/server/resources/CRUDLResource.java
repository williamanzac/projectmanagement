package nz.co.fit.projectmanagement.server.resources;

import static java.util.stream.Collectors.toList;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import io.dropwizard.hibernate.UnitOfWork;
import nz.co.fit.projectmanagement.server.api.BaseIdable;
import nz.co.fit.projectmanagement.server.core.CRUDLService;
import nz.co.fit.projectmanagement.server.core.ServiceException;
import nz.co.fit.projectmanagement.server.dao.entities.BaseIdableModel;

@PermitAll
public class CRUDLResource<A extends BaseIdable, D extends BaseIdableModel> {

	final CRUDLService<D> service;
	final Class<A> apiClass;
	final Class<D> dbClass;

	@SuppressWarnings("unchecked")
	public CRUDLResource(final CRUDLService<D> service) {
		this.service = service;
		apiClass = (Class<A>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		dbClass = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@POST
	@UnitOfWork
	public A create(final A value) throws ResourceException {
		final D model = ModelUtilities.convert(value, dbClass);
		D createValue;
		try {
			createValue = service.create(model);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		final A retValue = ModelUtilities.convert(createValue, apiClass);
		return retValue;
	}

	@GET
	@Path("/{id}")
	@UnitOfWork
	public A read(final @PathParam("id") Long id) throws ResourceException {
		D value;
		try {
			value = service.read(id);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		final A retValue = ModelUtilities.convert(value, apiClass);
		return retValue;
	}

	@PUT
	@Path("/{id}")
	@UnitOfWork
	public A update(final @PathParam("id") Long id, final A value) throws ResourceException {
		value.setId(id); // use the id from the path as the id field will be ignored from the JSON
		final D model = ModelUtilities.convert(value, dbClass);
		D createValue;
		try {
			createValue = service.update(model);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		final A retValue = ModelUtilities.convert(createValue, apiClass);
		return retValue;
	}

	@DELETE
	@Path("/{id}")
	@UnitOfWork
	public void delete(final @PathParam("id") Long id) throws ResourceException {
		try {
			service.delete(id);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
	}

	@GET
	@UnitOfWork
	public List<BaseIdable> list() throws ResourceException {
		try {
			return service.list().stream().map(ModelUtilities::toIdable).collect(toList());
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
	}
}

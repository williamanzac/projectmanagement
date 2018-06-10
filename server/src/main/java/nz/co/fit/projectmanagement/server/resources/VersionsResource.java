package nz.co.fit.projectmanagement.server.resources;

import java.util.Date;
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
import nz.co.fit.projectmanagement.server.api.Version;
import nz.co.fit.projectmanagement.server.core.ServiceException;
import nz.co.fit.projectmanagement.server.core.VersionService;
import nz.co.fit.projectmanagement.server.dao.entities.VersionModel;

@Path("/versions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
public class VersionsResource extends CRUDLResource<Version, VersionModel> {

	@Inject
	public VersionsResource(final VersionService versionService) {
		super(versionService);
	}

	@POST
	@DenyAll
	@Override
	public Version create(final Version value) throws ResourceException {
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

	@POST
	@Path("/{id}/release")
	@UnitOfWork
	public void releaseVersion(final @PathParam("id") Long id) throws ResourceException {
		VersionModel version;
		try {
			version = service.read(id);
			if (version.getReleasedDate() != null) {
				throw new ResourceException("This version has already been released.");
			}
			version.setReleasedDate(new Date());
			service.update(version);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
	}

	@POST
	@Path("/{id}/unrelease")
	@UnitOfWork
	public void unreleaseVersion(final @PathParam("id") Long id) throws ResourceException {
		VersionModel version;
		try {
			version = service.read(id);
			if (version.getReleasedDate() == null) {
				throw new ResourceException("This version has not been released.");
			}
			version.setReleasedDate(null);
			service.update(version);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
	}

	@POST
	@Path("/{id}/archive")
	@UnitOfWork
	public void archiveVersion(final @PathParam("id") Long id) throws ResourceException {
		VersionModel version;
		try {
			version = service.read(id);
			if (version.getArchivedDate() != null) {
				throw new ResourceException("This version has already been archived.");
			}
			version.setArchivedDate(new Date());
			service.update(version);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
	}

	@POST
	@Path("/{id}/unarchive")
	@UnitOfWork
	public void unarchiveVersion(final @PathParam("id") Long id) throws ResourceException {
		VersionModel version;
		try {
			version = service.read(id);
			if (version.getArchivedDate() == null) {
				throw new ResourceException("This version has not been archived.");
			}
			version.setArchivedDate(null);
			service.update(version);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
	}
}

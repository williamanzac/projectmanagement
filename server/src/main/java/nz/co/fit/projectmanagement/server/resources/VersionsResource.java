package nz.co.fit.projectmanagement.server.resources;

import java.util.Date;

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

import io.dropwizard.hibernate.UnitOfWork;
import nz.co.fit.projectmanagement.server.api.Version;
import nz.co.fit.projectmanagement.server.core.ServiceException;
import nz.co.fit.projectmanagement.server.core.VersionService;
import nz.co.fit.projectmanagement.server.dao.entities.VersionModel;

@Path("/versions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VersionsResource {

	private final VersionService versionService;

	@Inject
	public VersionsResource(final VersionService versionService) {
		this.versionService = versionService;
	}

	@PUT
	@Path("/{id}")
	@UnitOfWork
	public Version updateVersion(final @PathParam("id") Long id, final Version version) throws ResourceException {
		version.setId(id); // use the id from the path as the id field will be ignored from the JSON
		final VersionModel model = ModelUtilities.convert(version, VersionModel.class);
		VersionModel createVersion;
		try {
			createVersion = versionService.update(model);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		final Version retVersion = ModelUtilities.convert(createVersion, Version.class);
		return retVersion;
	}

	@GET
	@Path("/{id}")
	@UnitOfWork
	public Version readVersion(final @PathParam("id") Long id) throws ResourceException {
		VersionModel version;
		try {
			version = versionService.read(id);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		final Version retVersion = ModelUtilities.convert(version, Version.class);
		return retVersion;
	}

	@DELETE
	@Path("/{id}")
	@UnitOfWork
	public void deleteVersion(final @PathParam("id") Long id) throws ResourceException {
		try {
			versionService.delete(id);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
	}

	@POST
	@Path("/{id}/release")
	@UnitOfWork
	public void releaseVersion(final @PathParam("id") Long id) throws ResourceException {
		VersionModel version;
		try {
			version = versionService.read(id);
			if (version.getReleasedDate() != null) {
				throw new ResourceException("This version has already been released.");
			}
			version.setReleasedDate(new Date());
			versionService.update(version);
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
			version = versionService.read(id);
			if (version.getReleasedDate() == null) {
				throw new ResourceException("This version has not been released.");
			}
			version.setReleasedDate(null);
			versionService.update(version);
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
			version = versionService.read(id);
			if (version.getArchivedDate() != null) {
				throw new ResourceException("This version has already been archived.");
			}
			version.setArchivedDate(new Date());
			versionService.update(version);
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
			version = versionService.read(id);
			if (version.getArchivedDate() == null) {
				throw new ResourceException("This version has not been archived.");
			}
			version.setArchivedDate(null);
			versionService.update(version);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
	}
}

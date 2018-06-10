package nz.co.fit.projectmanagement.server.resources;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.dropwizard.hibernate.UnitOfWork;
import nz.co.fit.projectmanagement.server.api.BaseIdable;
import nz.co.fit.projectmanagement.server.api.ProjectCategory;
import nz.co.fit.projectmanagement.server.core.ProjectCategoryService;
import nz.co.fit.projectmanagement.server.core.ProjectService;
import nz.co.fit.projectmanagement.server.core.ServiceException;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectCategoryModel;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectModel;

@Path("/projectCategories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
public class ProjectCategoriesResource extends CRUDLResource<ProjectCategory, ProjectCategoryModel> {

	private final ProjectService projectService;

	@Inject
	public ProjectCategoriesResource(final ProjectCategoryService categoryService,
			final ProjectService projectService) {
		super(categoryService);
		this.projectService = projectService;
	}

	@GET
	@Path("/{id}/projects")
	@UnitOfWork
	public List<BaseIdable> listProjectsByCategory(final @PathParam("id") Long id) throws ResourceException {
		List<ProjectModel> projects;
		try {
			projects = projectService.listProjectsForCategory(id);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		return projects.stream().map(ModelUtilities::toIdable).collect(toList());
	}
}

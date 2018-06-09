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
public class ProjectCategoriesResource {

	private final ProjectCategoryService categoryService;
	private final ProjectService projectService;

	@Inject
	public ProjectCategoriesResource(final ProjectCategoryService categoryService,
			final ProjectService projectService) {
		this.categoryService = categoryService;
		this.projectService = projectService;
	}

	@GET
	@PermitAll
	@UnitOfWork
	public List<BaseIdable> listAllProjects() throws ResourceException {
		try {
			return categoryService.listAll().stream().map(ModelUtilities::toIdable).collect(toList());
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
	}

	@POST
	@UnitOfWork
	public ProjectCategory createCategory(final ProjectCategory category) throws ResourceException {
		final ProjectCategoryModel model = ModelUtilities.convert(category, ProjectCategoryModel.class);
		ProjectCategoryModel createCategory;
		try {
			createCategory = categoryService.createCategory(model);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		final ProjectCategory retCategory = ModelUtilities.convert(createCategory, ProjectCategory.class);
		return retCategory;
	}

	@PUT
	@Path("/{id}")
	@UnitOfWork
	public ProjectCategory updateCategory(final @PathParam("id") Long id, final ProjectCategory category)
			throws ResourceException {
		category.setId(id); // use the id from the path as the id field will be ignored from the JSON
		final ProjectCategoryModel model = ModelUtilities.convert(category, ProjectCategoryModel.class);
		ProjectCategoryModel createCategory;
		try {
			createCategory = categoryService.updateCategory(model);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		final ProjectCategory retCategory = ModelUtilities.convert(createCategory, ProjectCategory.class);
		return retCategory;
	}

	@GET
	@Path("/{id}")
	@UnitOfWork
	public ProjectCategory readCategory(final @PathParam("id") Long id) throws ResourceException {
		ProjectCategoryModel category;
		try {
			category = categoryService.readCategory(id);
		} catch (final ServiceException e) {
			throw new ResourceException(e);
		}
		final ProjectCategory retCategory = ModelUtilities.convert(category, ProjectCategory.class);
		return retCategory;
	}

	@DELETE
	@Path("/{id}")
	@UnitOfWork
	public void deleteCategory(final @PathParam("id") Long id) throws ServiceException {
		categoryService.deleteCategory(id);
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

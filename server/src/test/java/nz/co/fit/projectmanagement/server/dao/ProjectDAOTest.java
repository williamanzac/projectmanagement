package nz.co.fit.projectmanagement.server.dao;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.dropwizard.testing.junit.DAOTestRule;
import nz.co.fit.projectmanagement.server.dao.entities.ComponentModel;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectCategoryModel;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectModel;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;
import nz.co.fit.projectmanagement.server.dao.entities.VersionModel;

public class ProjectDAOTest {

	@Rule
	public DAOTestRule database = DAOTestRule.newBuilder().addEntityClass(ProjectModel.class)
			.addEntityClass(VersionModel.class).addEntityClass(ProjectCategoryModel.class)
			.addEntityClass(UserModel.class).addEntityClass(ComponentModel.class).build();

	private ProjectDAO projectDAO;
	private VersionDAO versionDAO;
	private ProjectCategoryDAO categoryDAO;
	private UserDAO userDAO;
	private ComponentDAO componentDAO;

	@Before
	public void setUp() {
		projectDAO = new ProjectDAO(database.getSessionFactory());
		versionDAO = new VersionDAO(database.getSessionFactory());
		categoryDAO = new ProjectCategoryDAO(database.getSessionFactory());
		userDAO = new UserDAO(database.getSessionFactory());
		componentDAO = new ComponentDAO(database.getSessionFactory());
	}

	@Test
	public void createProject() throws Exception {
		final ProjectCategoryModel categoryModel = new ProjectCategoryModel();
		categoryModel.setName("foo");
		final ProjectCategoryModel category = categoryDAO.upsert(categoryModel);

		final UserModel userModel = new UserModel();
		final UserModel user = userDAO.upsert(userModel);

		final ProjectModel projectModel = new ProjectModel();
		projectModel.setName("bar");
		projectModel.setKey("BAR");
		projectModel.setCategory(category);
		projectModel.setProjectLead(user);

		final ProjectModel actual = database.inTransaction(() -> {
			return projectDAO.upsert(projectModel);
		});

		assertThat(actual.getId(), notNullValue());
	}

	@Test
	public void createVersionForProject() throws Exception {
		final Long projectId = database.inTransaction(() -> {
			final ProjectCategoryModel categoryModel = new ProjectCategoryModel();
			categoryModel.setName("foo");
			final ProjectCategoryModel category = categoryDAO.upsert(categoryModel);

			final UserModel userModel = new UserModel();
			final UserModel user = userDAO.upsert(userModel);

			final ProjectModel projectModel = new ProjectModel();
			projectModel.setName("baz");
			projectModel.setKey("BAZ");
			projectModel.setCategory(category);
			projectModel.setProjectLead(user);

			return projectDAO.upsert(projectModel).getId();
		});

		assertThat(projectId, notNullValue());

		database.inTransaction(() -> {
			try {
				final ProjectModel projectModel = projectDAO.read(projectId);
				final VersionModel versionModel = new VersionModel();
				versionModel.setName("1.0.0");
				versionModel.setPriority(1);
				versionDAO.upsert(versionModel);
				projectModel.getVersions().add(versionModel);
				projectDAO.upsert(projectModel);
			} catch (final DAOException e) {
				e.printStackTrace();
			}
		});

		final ProjectModel actualProject = projectDAO.read(projectId);

		final List<VersionModel> versions = actualProject.getVersions();
		assertThat(versions, notNullValue());
		assertThat(versions.size(), equalTo(1));
		final VersionModel actualVersion = versions.stream().findFirst().get();
		assertThat(actualVersion.getName(), equalTo("1.0.0"));
	}

	@Test
	public void createComponentForProject() throws Exception {
		final Long projectId = database.inTransaction(() -> {
			final ProjectCategoryModel categoryModel = new ProjectCategoryModel();
			categoryModel.setName("foo");
			final ProjectCategoryModel category = categoryDAO.upsert(categoryModel);

			final UserModel userModel = new UserModel();
			final UserModel user = userDAO.upsert(userModel);

			final ProjectModel projectModel = new ProjectModel();
			projectModel.setName("baz");
			projectModel.setKey("BAZ");
			projectModel.setCategory(category);
			projectModel.setProjectLead(user);

			return projectDAO.upsert(projectModel).getId();
		});

		assertThat(projectId, notNullValue());

		database.inTransaction(() -> {
			try {
				final ProjectModel projectModel = projectDAO.read(projectId);
				final ComponentModel componentModel = new ComponentModel();
				componentModel.setName("bar");
				componentDAO.upsert(componentModel);
				projectModel.getComponents().add(componentModel);
				projectDAO.upsert(projectModel);
			} catch (final DAOException e) {
				e.printStackTrace();
			}
		});

		final ProjectModel actualProject = projectDAO.read(projectId);

		final List<ComponentModel> components = actualProject.getComponents();
		assertThat(components, notNullValue());
		assertThat(components.size(), equalTo(1));
		final ComponentModel actualComponent = components.stream().findFirst().get();
		assertThat(actualComponent.getName(), equalTo("bar"));
	}

	@Test
	public void listProjectsByCategory() throws Exception {
		final Long categoryId = database.inTransaction(() -> {
			final ProjectCategoryModel categoryModel = new ProjectCategoryModel();
			categoryModel.setName("foo");
			final ProjectCategoryModel category = categoryDAO.upsert(categoryModel);
			System.out.println("category: " + category);

			final UserModel userModel = new UserModel();
			final UserModel user = userDAO.upsert(userModel);
			System.out.println("user: " + user);

			final ProjectModel projectModel = new ProjectModel();
			projectModel.setName("baz");
			projectModel.setKey("BAZ");
			projectModel.setCategory(category);
			projectModel.setProjectLead(user);
			final ProjectModel project = projectDAO.upsert(projectModel);
			System.out.println("project: " + project);

			return category.getId();
		});

		assertThat(categoryId, notNullValue());

		final ProjectCategoryModel actualCategory = categoryDAO.read(categoryId);
		assertThat(actualCategory.getName(), equalTo("foo"));

		final List<ProjectModel> actual = projectDAO.listProjectsForCategory(categoryId);
		assertThat(actual, notNullValue());
		assertThat(actual.size(), equalTo(1));
	}
}

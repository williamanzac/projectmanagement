package nz.co.fit.projectmanagement.server.resources;

import static nz.co.fit.projectmanagement.server.resources.ModelUtilities.convert;
import static nz.co.fit.projectmanagement.server.resources.ModelUtilities.toIdable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import nz.co.fit.projectmanagement.server.api.BaseIdable;
import nz.co.fit.projectmanagement.server.api.Project;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectModel;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

public class ModelUtilitiesTest {

	@Test
	public void getOnlyIdsFromModel() throws Exception {
		final ProjectModel project = new ProjectModel();
		project.setId(1l);
		final BaseIdable actual = toIdable(project);
		assertThat(actual.getId(), equalTo(1l));
	}

	@Test
	public void convertFromModelToAPI() throws Exception {
		final UserModel userModel = new UserModel();
		userModel.setName("Test User");
		userModel.setId(1l);

		final ProjectModel model = new ProjectModel();
		model.setDescription("Test Description");
		model.setId(1l);
		model.setName("Test Name");
		model.setProjectLead(userModel);

		final Project actual = convert(model, Project.class);

		assertThat(actual.getDescription(), equalTo("Test Description"));
		assertThat(actual.getId(), equalTo(1l));
		assertThat(actual.getName(), equalTo("Test Name"));
		assertThat(actual.getProjectLead(), notNullValue());
		assertThat(actual.getProjectLead().getId(), equalTo(1l));
	}

	@Test
	public void convertFromAPIToModel() throws Exception {
		final Project model = new Project("Test Name", "Test Description");
		model.setId(1l);

		final ProjectModel actual = convert(model, ProjectModel.class);

		assertThat(actual.getDescription(), equalTo("Test Description"));
		assertThat(actual.getId(), equalTo(1l));
		assertThat(actual.getName(), equalTo("Test Name"));
	}
}

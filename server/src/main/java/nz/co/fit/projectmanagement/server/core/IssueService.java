package nz.co.fit.projectmanagement.server.core;

import java.util.List;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.DAOException;
import nz.co.fit.projectmanagement.server.dao.IssueDAO;
import nz.co.fit.projectmanagement.server.dao.entities.IssueModel;
import nz.co.fit.projectmanagement.server.dao.entities.IssueStatusModel;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectModel;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

@Service
public class IssueService extends CRUDLService<IssueModel> {

	private final ProjectService projectService;
	private final VersionService versionService;
	private final UserService userService;
	private final ComponentService componentService;
	private final IssueStatusService statusService;

	@Inject
	public IssueService(final IssueDAO issueDAO, final ProjectService projectService,
			final VersionService versionService, final UserService userService, final ComponentService componentService,
			final IssueStatusService statusService) {
		super(issueDAO);
		this.projectService = projectService;
		this.versionService = versionService;
		this.userService = userService;
		this.componentService = componentService;
		this.statusService = statusService;
	}

	public List<IssueModel> listIssuesByProject(final Long projectId) throws ServiceException {
		try {
			return ((IssueDAO) dao).listIssuesByProject(projectId);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public IssueModel create(final IssueModel value) throws ServiceException {
		createOrUpdateLinkedValues(value);
		return super.create(value);
	}

	private void createOrUpdateLinkedValues(final IssueModel value) throws ServiceException {
		if (!value.getAffectsVersions().isEmpty()) {
			value.getAffectsVersions().forEach(new CreateOrUpdateConsumer<>(versionService));
		}
		if (value.getAssignee() != null) {
			final UserModel user = userService.read(value.getAssignee().getId());
			value.setAssignee(user);
		}
		if (!value.getComponents().isEmpty()) {
			value.getComponents().forEach(new CreateOrUpdateConsumer<>(componentService));
		}
		if (!value.getFixedVersions().isEmpty()) {
			value.getFixedVersions().forEach(new CreateOrUpdateConsumer<>(versionService));
		}
		if (value.getProject() != null) {
			final ProjectModel project = projectService.read(value.getProject().getId());
			value.setProject(project);
		}
		if (!value.getRelatedIssues().isEmpty()) {
			value.getRelatedIssues().forEach(new CreateOrUpdateConsumer<>(this));
		}
		if (value.getStatus() != null) {
			final IssueStatusModel project = statusService.read(value.getStatus().getId());
			value.setStatus(project);
		}
	}

	@Override
	public IssueModel update(final IssueModel value) throws ServiceException {
		createOrUpdateLinkedValues(value);
		return super.update(value);
	}
}

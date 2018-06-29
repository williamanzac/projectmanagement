package nz.co.fit.projectmanagement.server.core;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.DAOException;
import nz.co.fit.projectmanagement.server.dao.ProjectDAO;
import nz.co.fit.projectmanagement.server.dao.UserDAO;
import nz.co.fit.projectmanagement.server.dao.entities.ComponentModel;
import nz.co.fit.projectmanagement.server.dao.entities.EpicModel;
import nz.co.fit.projectmanagement.server.dao.entities.InitiativeModel;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectCategoryModel;
import nz.co.fit.projectmanagement.server.dao.entities.ProjectModel;
import nz.co.fit.projectmanagement.server.dao.entities.RoleModel;
import nz.co.fit.projectmanagement.server.dao.entities.ThemeModel;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;
import nz.co.fit.projectmanagement.server.dao.entities.VersionModel;

@Service
public class ProjectService extends CRUDLService<ProjectModel> {

	private final VersionService versionService;
	private final ComponentService componentService;
	private final ProjectCategoryService categoryService;
	private final UserService userService;
	private final RoleService roleService;
	private final ThemeService themeService;
	private final InitiativeService initiativeService;
	private final EpicService epicService;

	@Inject
	public ProjectService(final ProjectDAO projectDAO, final VersionService versionService,
			final ComponentService componentService, final ProjectCategoryService categoryService,
			final UserService userService, final HistoryService historyService, final UserDAO userDAO,
			final RoleService roleService, final ThemeService themeService, final InitiativeService initiativeService,
			final EpicService epicService) {
		super(projectDAO, historyService, userDAO);
		this.versionService = versionService;
		this.componentService = componentService;
		this.categoryService = categoryService;
		this.userService = userService;
		this.roleService = roleService;
		this.themeService = themeService;
		this.initiativeService = initiativeService;
		this.epicService = epicService;
	}

	@Override
	public ProjectModel create(final ProjectModel project) throws ServiceException {
		// the data from the REST layer will only have the id so we will need to find the actual object
		if (project.getCategory() != null) {
			final ProjectCategoryModel category = categoryService.read(project.getCategory().getId());
			project.setCategory(category);
		}
		if (project.getProjectLead() != null) {
			final UserModel user = userService.read(project.getProjectLead().getId());
			project.setProjectLead(user);
		}
		return super.create(project);
	}

	@Override
	public ProjectModel update(final ProjectModel project) throws ServiceException {
		if (!project.getVersions().isEmpty()) {
			// some versions have been passed in, so make sure that they are upserted first.
			project.getVersions().forEach(v -> {
				try {
					if (v.getId() != null) {
						versionService.update(v);
					} else {
						versionService.create(v);
					}
				} catch (final ServiceException e) {
					e.printStackTrace();
				}
			});
		}
		if (!project.getComponents().isEmpty()) {
			// some components have been passed in, so make sure that they are upserted first.
			project.getComponents().forEach(c -> {
				try {
					if (c.getId() != null) {
						componentService.update(c);
					} else {
						componentService.create(c);
					}
				} catch (final ServiceException e) {
					e.printStackTrace();
				}
			});
		}
		if (!project.getRoles().isEmpty()) {
			// some components have been passed in, so make sure that they are upserted first.
			project.getRoles().forEach(c -> {
				try {
					if (c.getId() != null) {
						roleService.update(c);
					} else {
						roleService.create(c);
					}
				} catch (final ServiceException e) {
					e.printStackTrace();
				}
			});
		}

		// the data from the api will only be the id, so we need to read the whole object
		if (project.getCategory() != null) {
			final ProjectCategoryModel category = categoryService.read(project.getCategory().getId());
			project.setCategory(category);
		}
		if (project.getProjectLead() != null) {
			final UserModel user = userService.read(project.getProjectLead().getId());
			project.setProjectLead(user);
		}

		return super.update(project);
	}

	@Override
	public void delete(final Long id) throws ServiceException {
		final ProjectModel existingProject = read(id);
		// delete the versions, components
		existingProject.getVersions().forEach(v -> {
			try {
				versionService.delete(v.getId());
			} catch (final ServiceException e2) {
				e2.printStackTrace();
			}
		});
		existingProject.getComponents().forEach(c -> {
			try {
				componentService.delete(c.getId());
			} catch (final ServiceException e1) {
				e1.printStackTrace();
			}
		});
		existingProject.getRoles().forEach(c -> {
			try {
				roleService.delete(c.getId());
			} catch (final ServiceException e1) {
				e1.printStackTrace();
			}
		});

		super.delete(id);
	}

	public List<ProjectModel> listProjectsForCategory(final Long categoryId) throws ServiceException {
		try {
			return ((ProjectDAO) dao).listProjectsForCategory(categoryId);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public VersionModel addVersionToProject(final Long projectId, final VersionModel version) throws ServiceException {
		final ProjectModel projModel = read(projectId);
		final List<VersionModel> versions = projModel.getVersions();
		final long matchingName = versions.stream().filter(v -> v.getName().equals(version.getName())).count();
		if (matchingName > 0) {
			throw new ServiceException("There is already a version '" + version.getName() + "' for this project.");
		}

		final int length = versions.size();
		version.setPriority(length + 1);
		versions.add(version);
		final VersionModel createVersion = versionService.create(version);
		update(projModel);
		return createVersion;
	}

	public ComponentModel addComponentToProject(final Long projectId, final ComponentModel component)
			throws ServiceException {
		final ProjectModel projModel = read(projectId);
		final List<ComponentModel> components = projModel.getComponents();
		final long matchingName = components.stream().filter(v -> v.getName().equals(component.getName())).count();
		if (matchingName > 0) {
			throw new ServiceException("There is already a component '" + component.getName() + "' for this project.");
		}

		components.add(component);
		final ComponentModel createComponent = componentService.create(component);
		update(projModel);
		return createComponent;
	}

	public RoleModel addRoleToProject(final Long projectId, final RoleModel role) throws ServiceException {
		final ProjectModel projModel = read(projectId);
		final List<RoleModel> roles = projModel.getRoles();
		final long matchingName = roles.stream().filter(v -> v.getName().equals(role.getName())).count();
		if (matchingName > 0) {
			throw new ServiceException("There is already a role '" + role.getName() + "' for this project.");
		}

		roles.add(role);
		final RoleModel createRole = roleService.create(role);
		update(projModel);
		return createRole;
	}

	public List<UserModel> listUsersForProject(final Long projectId) throws ServiceException {
		final ProjectModel projectModel = read(projectId);
		final List<UserModel> users = projectModel.getRoles().stream().map(RoleModel::getUsers)
				.flatMap(Collection::stream).distinct().collect(toList());
		return users;
	}

	public ThemeModel addThemeToProject(final Long projectId, final ThemeModel theme) throws ServiceException {
		final ProjectModel projModel = read(projectId);
		final List<ThemeModel> themes = projModel.getThemes();
		final long matchingName = themes.stream().filter(v -> v.getName().equals(theme.getName())).count();
		if (matchingName > 0) {
			throw new ServiceException("There is already a theme '" + theme.getName() + "' for this project.");
		}

		themes.add(theme);
		final ThemeModel createTheme = themeService.create(theme);
		update(projModel);
		return createTheme;
	}

	public InitiativeModel addInitiativeToProject(final Long projectId, final InitiativeModel initiative)
			throws ServiceException {
		final ProjectModel projModel = read(projectId);
		final List<InitiativeModel> initiatives = projModel.getInitiatives();
		final long matchingName = initiatives.stream().filter(v -> v.getName().equals(initiative.getName())).count();
		if (matchingName > 0) {
			throw new ServiceException(
					"There is already a initiative '" + initiative.getName() + "' for this project.");
		}

		initiatives.add(initiative);
		final InitiativeModel createInitiative = initiativeService.create(initiative);
		update(projModel);
		return createInitiative;
	}

	public EpicModel addEpicToProject(final Long projectId, final EpicModel epic) throws ServiceException {
		final ProjectModel projModel = read(projectId);
		final List<EpicModel> epics = projModel.getEpics();
		final long matchingName = epics.stream().filter(v -> v.getName().equals(epic.getName())).count();
		if (matchingName > 0) {
			throw new ServiceException("There is already a epic '" + epic.getName() + "' for this project.");
		}

		epics.add(epic);
		final EpicModel createEpic = epicService.create(epic);
		update(projModel);
		return createEpic;
	}
}

package nz.co.fit.projectmanagement.server.core;

import java.util.List;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.DAOException;
import nz.co.fit.projectmanagement.server.dao.RoleDAO;
import nz.co.fit.projectmanagement.server.dao.UserDAO;
import nz.co.fit.projectmanagement.server.dao.entities.RoleModel;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

@Service
public class RoleService extends CRUDLService<RoleModel> {

	private final UserService userService;

	@Inject
	public RoleService(final RoleDAO permissionsDAO, final HistoryService historyService, final UserDAO userDAO,
			final UserService userService) {
		super(permissionsDAO, historyService, userDAO);
		this.userService = userService;
	}

	public List<RoleModel> getRolesForProject(final Long projectId) throws ServiceException {
		try {
			return ((RoleDAO) dao).getRolesForProject(projectId);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public RoleModel addUserToRole(final Long roleId, final Long userId) throws ServiceException {
		final RoleModel roleModel = read(roleId);
		final UserModel userModel = userService.read(userId);
		roleModel.getUsers().add(userModel);
		userService.update(userModel);
		update(roleModel);
		return roleModel;
	}

	public List<RoleModel> listRolesForUser(final Long userId) throws ServiceException {
		try {
			return ((RoleDAO) dao).listRolesForUser(userId);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}
}

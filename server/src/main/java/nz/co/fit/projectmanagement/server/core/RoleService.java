package nz.co.fit.projectmanagement.server.core;

import java.util.List;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.DAOException;
import nz.co.fit.projectmanagement.server.dao.RoleDAO;
import nz.co.fit.projectmanagement.server.dao.entities.GroupModel;
import nz.co.fit.projectmanagement.server.dao.entities.RoleModel;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

@Service
public class RoleService extends CRUDLService<RoleModel> {

	private final UserService userService;
	private final GroupService groupService;

	@Inject
	public RoleService(final RoleDAO permissionsDAO,
			final UserService userService, final GroupService groupService) {
		super(permissionsDAO);
		this.userService = userService;
		this.groupService = groupService;
	}

	public RoleModel addUserToRole(final Long roleId, final Long userId) throws ServiceException {
		final RoleModel roleModel = read(roleId);
		final UserModel userModel = userService.read(userId);
		roleModel.getUsers().add(userModel);
		userService.update(userModel);
		update(roleModel);
		return roleModel;
	}

	public RoleModel addGroupToRole(final Long roleId, final Long groupId) throws ServiceException {
		final RoleModel roleModel = read(roleId);
		final GroupModel groupModel = groupService.read(groupId);
		roleModel.getGroups().add(groupModel);
		groupService.update(groupModel);
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

	public List<RoleModel> listRolesForGroup(final Long groupId) throws ServiceException {
		try {
			return ((RoleDAO) dao).listRolesForGroup(groupId);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}
}

package nz.co.fit.projectmanagement.server.core;

import java.util.List;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.DAOException;
import nz.co.fit.projectmanagement.server.dao.GroupDAO;
import nz.co.fit.projectmanagement.server.dao.UserDAO;
import nz.co.fit.projectmanagement.server.dao.entities.GroupModel;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

@Service
public class GroupService extends CRUDLService<GroupModel> {

	private final UserService userService;

	@Inject
	public GroupService(final GroupDAO permissionsDAO, final HistoryService historyService, final UserDAO userDAO,
			final UserService userService) {
		super(permissionsDAO, historyService, userDAO);
		this.userService = userService;
	}

	public GroupModel addUserToGroup(final Long roleId, final Long userId) throws ServiceException {
		final GroupModel roleModel = read(roleId);
		final UserModel userModel = userService.read(userId);
		roleModel.getUsers().add(userModel);
		userService.update(userModel);
		update(roleModel);
		return roleModel;
	}

	public List<GroupModel> listGroupsForUser(final Long userId) throws ServiceException {
		try {
			return ((GroupDAO) dao).listGroupsForUser(userId);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}
}

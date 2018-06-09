package nz.co.fit.projectmanagement.server.core;

import java.security.Principal;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.greenrobot.eventbus.EventBus;
import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.auth.CustomAuthUser;
import nz.co.fit.projectmanagement.server.dao.DAOException;
import nz.co.fit.projectmanagement.server.dao.HistoryEvent;
import nz.co.fit.projectmanagement.server.dao.UserDAO;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

@Service
public class UserService {

	private final UserDAO userDAO;
	private final HistoryService historyService;

	@Resource
	@Context
	SecurityContext securityContext;

	@Inject
	public UserService(final UserDAO userDAO, final HistoryService historyService) {
		this.userDAO = userDAO;
		this.historyService = historyService;
	}

	public List<UserModel> listAll() throws ServiceException {
		try {
			return userDAO.list();
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public UserModel createUser(final UserModel user) throws ServiceException {
		if (securityContext != null) {
			final Principal userPrincipal = securityContext.getUserPrincipal();
			System.out.println(userPrincipal.getName());
			final CustomAuthUser authUser = (CustomAuthUser) userPrincipal;
			final Long userId = authUser.getUserId();
			System.out.println(userId);
			if (userId != null) {
				final UserModel currentUser = readUser(userId);
				user.setCreatedBy(currentUser);
			}
		}
		try {
			return userDAO.upsert(user);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public UserModel readUser(final Long id) throws ServiceException {
		try {
			return userDAO.read(id);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public UserModel updateUser(final UserModel user) throws ServiceException {
		UserModel existing;
		try {
			existing = userDAO.read(user.getId());
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
		if (user.getName() != null) {
			if (!user.getName().equals(existing.getName())) {
				EventBus.getDefault().post(new HistoryEvent("name", existing.getName(), user.getName(), user));
			}
			existing.setName(user.getName());
		}
		if (user.getEmail() != null) {
			if (!user.getEmail().equals(existing.getEmail())) {
				EventBus.getDefault().post(new HistoryEvent("email", existing.getEmail(), user.getEmail(), user));
			}
			existing.setEmail(user.getEmail());
		}
		if (user.getAuthType() != null) {
			if (user.getAuthType() != existing.getAuthType()) {
				EventBus.getDefault().post(
						new HistoryEvent("authType", existing.getAuthType().name(), user.getAuthType().name(), user));
			}
			existing.setAuthType(user.getAuthType());
		}
		if (user.getPassword() != null) {
			if (!user.getPassword().equals(existing.getPassword())) {
				// do not record the actual passwords here, but make a history entry to show that it has been changed
				EventBus.getDefault().post(new HistoryEvent("password", "", "", user));
			}
			existing.setPassword(user.getPassword());
		}
		try {
			return userDAO.upsert(existing);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public void deleteUser(final Long id) throws ServiceException {
		historyService.deleteHistoryForObject(id, UserModel.class);
		try {
			userDAO.delete(id);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}
}

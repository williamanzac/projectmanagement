package nz.co.fit.projectmanagement.server.core;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.UserDAO;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

@Service
public class UserService extends CRUDLService<UserModel> {
	@Inject
	public UserService(final UserDAO userDAO, final HistoryService historyService) {
		super(userDAO, historyService, userDAO);
	}
}

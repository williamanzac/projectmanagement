package nz.co.fit.projectmanagement.server.core;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.UserDAO;
import nz.co.fit.projectmanagement.server.dao.VersionDAO;
import nz.co.fit.projectmanagement.server.dao.entities.VersionModel;

@Service
public class VersionService extends CRUDLService<VersionModel> {
	@Inject
	public VersionService(final VersionDAO versionDAO, final HistoryService historyService, final UserDAO userDAO) {
		super(versionDAO, historyService, userDAO);
	}
}

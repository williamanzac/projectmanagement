package nz.co.fit.projectmanagement.server.core;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.EpicDAO;
import nz.co.fit.projectmanagement.server.dao.UserDAO;
import nz.co.fit.projectmanagement.server.dao.entities.EpicModel;

@Service
public class EpicService extends CRUDLService<EpicModel> {
	@Inject
	public EpicService(final EpicDAO epicDAO, final HistoryService historyService, final UserDAO userDAO) {
		super(epicDAO, historyService, userDAO);
	}
}

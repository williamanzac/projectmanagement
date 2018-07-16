package nz.co.fit.projectmanagement.server.core;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.InitiativeDAO;
import nz.co.fit.projectmanagement.server.dao.entities.InitiativeModel;

@Service
public class InitiativeService extends CRUDLService<InitiativeModel> {
	@Inject
	public InitiativeService(final InitiativeDAO initiativeDAO) {
		super(initiativeDAO);
	}
}

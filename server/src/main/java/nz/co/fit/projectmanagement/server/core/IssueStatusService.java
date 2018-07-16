package nz.co.fit.projectmanagement.server.core;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.IssueStatusDAO;
import nz.co.fit.projectmanagement.server.dao.entities.IssueStatusModel;

@Service
public class IssueStatusService extends CRUDLService<IssueStatusModel> {

	@Inject
	public IssueStatusService(final IssueStatusDAO issueStatusDAO) {
		super(issueStatusDAO);
	}
}

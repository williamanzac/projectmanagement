package nz.co.fit.projectmanagement.server.dao;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.entities.IssueStatusModel;

@Service
public class IssueStatusDAO extends BaseDAO<IssueStatusModel> {

	@Inject
	public IssueStatusDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}
}

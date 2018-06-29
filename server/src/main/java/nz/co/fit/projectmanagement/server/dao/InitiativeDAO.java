package nz.co.fit.projectmanagement.server.dao;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.entities.InitiativeModel;

@Service
public class InitiativeDAO extends BaseDAO<InitiativeModel> {

	@Inject
	public InitiativeDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}
}

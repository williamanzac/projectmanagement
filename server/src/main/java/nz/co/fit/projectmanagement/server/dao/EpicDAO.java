package nz.co.fit.projectmanagement.server.dao;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.entities.EpicModel;

@Service
public class EpicDAO extends BaseDAO<EpicModel> {

	@Inject
	public EpicDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}
}

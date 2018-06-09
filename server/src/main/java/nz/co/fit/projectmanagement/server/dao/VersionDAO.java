package nz.co.fit.projectmanagement.server.dao;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.entities.VersionModel;

@Service
public class VersionDAO extends BaseDAO<VersionModel> {

	@Inject
	public VersionDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}
}

package nz.co.fit.projectmanagement.server.dao;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.entities.PermissionModel;

@Service
public class PermissionsDAO extends BaseDAO<PermissionModel> {

	@Inject
	public PermissionsDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}
}

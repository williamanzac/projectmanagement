package nz.co.fit.projectmanagement.server.dao;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import io.dropwizard.hibernate.AbstractDAO;
import nz.co.fit.projectmanagement.server.dao.entities.PermissionModel;

@Service
public class PermissionsDAO extends AbstractDAO<PermissionModel> {

	@Inject
	public PermissionsDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public List<PermissionModel> listAll() {
		return list(criteriaQuery());
	}
}

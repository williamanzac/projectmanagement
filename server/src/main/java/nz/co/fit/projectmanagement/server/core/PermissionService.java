package nz.co.fit.projectmanagement.server.core;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.PermissionsDAO;
import nz.co.fit.projectmanagement.server.dao.entities.PermissionModel;

@Service
public class PermissionService extends CRUDLService<PermissionModel> {
	@Inject
	public PermissionService(final PermissionsDAO permissionsDAO) {
		super(permissionsDAO);
	}
}

package nz.co.fit.projectmanagement.server.core;

import java.util.List;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.PermissionsDAO;
import nz.co.fit.projectmanagement.server.dao.entities.PermissionModel;

@Service
public class PermissionService {

	private final PermissionsDAO permissionsDAO;

	@Inject
	public PermissionService(final PermissionsDAO permissionsDAO) {
		this.permissionsDAO = permissionsDAO;
	}

	public List<PermissionModel> listAll() {
		return permissionsDAO.listAll();
	}
}

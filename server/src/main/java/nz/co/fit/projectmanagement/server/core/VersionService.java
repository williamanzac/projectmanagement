package nz.co.fit.projectmanagement.server.core;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.VersionDAO;
import nz.co.fit.projectmanagement.server.dao.entities.VersionModel;

@Service
public class VersionService extends CRUDLService<VersionModel> {
	@Inject
	public VersionService(final VersionDAO versionDAO) {
		super(versionDAO);
	}
}

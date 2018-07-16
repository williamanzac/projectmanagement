package nz.co.fit.projectmanagement.server.core;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.ComponentDAO;
import nz.co.fit.projectmanagement.server.dao.entities.ComponentModel;

@Service
public class ComponentService extends CRUDLService<ComponentModel> {

	@Inject
	public ComponentService(final ComponentDAO componentDAO) {
		super(componentDAO);
	}
}

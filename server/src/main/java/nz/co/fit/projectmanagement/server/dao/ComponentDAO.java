package nz.co.fit.projectmanagement.server.dao;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.entities.ComponentModel;

@Service
public class ComponentDAO extends BaseDAO<ComponentModel> {

	@Inject
	public ComponentDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}
}

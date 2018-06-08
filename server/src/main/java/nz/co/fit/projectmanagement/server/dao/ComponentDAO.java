package nz.co.fit.projectmanagement.server.dao;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import io.dropwizard.hibernate.AbstractDAO;
import nz.co.fit.projectmanagement.server.dao.entities.ComponentModel;

@Service
public class ComponentDAO extends AbstractDAO<ComponentModel> {

	@Inject
	public ComponentDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public ComponentModel upsert(final ComponentModel version) {
		return persist(version);
	}

	public ComponentModel read(final Long id) {
		return get(id);
	}

	public void delete(final Long id) {
		final ComponentModel version = read(id);
		currentSession().remove(version);
	}
}

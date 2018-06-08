package nz.co.fit.projectmanagement.server.dao;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import io.dropwizard.hibernate.AbstractDAO;
import nz.co.fit.projectmanagement.server.dao.entities.VersionModel;

@Service
public class VersionDAO extends AbstractDAO<VersionModel> {

	@Inject
	public VersionDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public VersionModel upsert(final VersionModel version) {
		return persist(version);
	}

	public VersionModel read(final Long id) {
		return get(id);
	}

	public void delete(final Long id) {
		final VersionModel version = read(id);
		currentSession().remove(version);
	}
}

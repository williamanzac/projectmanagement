package nz.co.fit.projectmanagement.server.dao;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.entities.ThemeModel;

@Service
public class ThemeDAO extends BaseDAO<ThemeModel> {

	@Inject
	public ThemeDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}
}

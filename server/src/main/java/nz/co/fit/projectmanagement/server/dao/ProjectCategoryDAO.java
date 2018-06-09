package nz.co.fit.projectmanagement.server.dao;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.entities.ProjectCategoryModel;

@Service
public class ProjectCategoryDAO extends BaseDAO<ProjectCategoryModel> {

	@Inject
	public ProjectCategoryDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}
}

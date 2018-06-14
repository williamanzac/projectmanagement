package nz.co.fit.projectmanagement.server;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.hibernate.SessionFactory;

import nz.co.fit.projectmanagement.server.core.ComponentService;
import nz.co.fit.projectmanagement.server.core.HistoryService;
import nz.co.fit.projectmanagement.server.core.PermissionService;
import nz.co.fit.projectmanagement.server.core.ProjectCategoryService;
import nz.co.fit.projectmanagement.server.core.ProjectService;
import nz.co.fit.projectmanagement.server.core.TokenService;
import nz.co.fit.projectmanagement.server.core.UserService;
import nz.co.fit.projectmanagement.server.core.VersionService;
import nz.co.fit.projectmanagement.server.dao.ComponentDAO;
import nz.co.fit.projectmanagement.server.dao.HistoryDAO;
import nz.co.fit.projectmanagement.server.dao.PermissionsDAO;
import nz.co.fit.projectmanagement.server.dao.ProjectCategoryDAO;
import nz.co.fit.projectmanagement.server.dao.ProjectDAO;
import nz.co.fit.projectmanagement.server.dao.TokenDAO;
import nz.co.fit.projectmanagement.server.dao.UserDAO;
import nz.co.fit.projectmanagement.server.dao.VersionDAO;

public class ServerBinder extends AbstractBinder {
	private final SessionFactory sessionFactory;

	public ServerBinder(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	protected void configure() {
		bind(sessionFactory).to(SessionFactory.class);

		bind(ComponentDAO.class).to(ComponentDAO.class);
		bind(ProjectCategoryDAO.class).to(ProjectCategoryDAO.class);
		bind(ProjectDAO.class).to(ProjectDAO.class);
		bind(VersionDAO.class).to(VersionDAO.class);
		bind(PermissionsDAO.class).to(PermissionsDAO.class);
		bind(UserDAO.class).to(UserDAO.class);
		bind(HistoryDAO.class).to(HistoryDAO.class);
		bind(TokenDAO.class).to(TokenDAO.class);

		bind(ComponentService.class).to(ComponentService.class);
		bind(ProjectCategoryService.class).to(ProjectCategoryService.class);
		bind(ProjectService.class).to(ProjectService.class);
		bind(VersionService.class).to(VersionService.class);
		bind(PermissionService.class).to(PermissionService.class);
		bind(UserService.class).to(UserService.class);
		bind(HistoryService.class).to(HistoryService.class);
		bind(TokenService.class).to(TokenService.class);
	}
}

package nz.co.fit.projectmanagement.server;

import java.util.Set;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.hibernate.SessionFactory;
import org.reflections.Reflections;

import nz.co.fit.projectmanagement.server.core.CRUDLService;
import nz.co.fit.projectmanagement.server.core.HistoryService;
import nz.co.fit.projectmanagement.server.core.TokenService;
import nz.co.fit.projectmanagement.server.dao.BaseDAO;
import nz.co.fit.projectmanagement.server.dao.TokenDAO;

public class ServerBinder extends AbstractBinder {
	private final SessionFactory sessionFactory;

	public ServerBinder(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void configure() {
		bind(sessionFactory).to(SessionFactory.class);

		final Reflections daoReflections = new Reflections("nz.co.fit.projectmanagement.server.dao");
		final Set<Class<? extends BaseDAO>> daoSubTypes = daoReflections.getSubTypesOf(BaseDAO.class);
		daoSubTypes.forEach(s -> {
			bind(s).to(s);
		});

		bind(TokenDAO.class).to(TokenDAO.class); // does not implement BaseDAO

		final Reflections serviceReflections = new Reflections("nz.co.fit.projectmanagement.server.core");
		final Set<Class<? extends CRUDLService>> serviceSubTypes = serviceReflections.getSubTypesOf(CRUDLService.class);
		serviceSubTypes.forEach(s -> {
			bind(s).to(s);
		});

		bind(HistoryService.class).to(HistoryService.class); // does not implement CRUDLService
		bind(TokenService.class).to(TokenService.class); // does not implement CRUDLService
	}
}

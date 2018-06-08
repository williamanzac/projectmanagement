package nz.co.fit.projectmanagement.server;

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.hibernate.SessionFactory;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.hibernate.SessionFactoryFactory;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import nz.co.fit.projectmanagement.server.auth.CustomAuthFilter;
import nz.co.fit.projectmanagement.server.auth.CustomAuthUser;
import nz.co.fit.projectmanagement.server.auth.CustomAuthenticator;
import nz.co.fit.projectmanagement.server.core.ManagedHistoryService;
import nz.co.fit.projectmanagement.server.dao.UserDAO;

public class ServerApplication extends Application<ServerConfiguration> {

	private final HibernateBundle<ServerConfiguration> hibernate = new ScanningHibernateBundle<>(
			new String[] { "nz.co.fit.projectmanagement.server.dao.entities" }, new SessionFactoryFactory()) {
		@Override
		public DataSourceFactory getDataSourceFactory(final ServerConfiguration configuration) {
			return configuration.getDataSourceFactory();
		}
	};

	public static void main(final String[] args) throws Exception {
		new ServerApplication().run(args);
	}

	@Override
	public String getName() {
		return "Server";
	}

	@Override
	public void initialize(final Bootstrap<ServerConfiguration> bootstrap) {
		super.initialize(bootstrap);
		bootstrap.addBundle(hibernate);
	}

	@Override
	public void run(final ServerConfiguration configuration, final Environment environment) {
		final SessionFactory sessionFactory = hibernate.getSessionFactory();
		final UserDAO userDAO = new UserDAO(sessionFactory);

		environment.jersey().register(new ServerBinder(sessionFactory));

		final CustomAuthenticator authenticator = new UnitOfWorkAwareProxyFactory(hibernate)
				.create(CustomAuthenticator.class, new Class<?>[] { UserDAO.class }, new Object[] { userDAO });
		final CustomAuthFilter filter = new CustomAuthFilter(authenticator);

		environment.jersey().register(new AuthDynamicFeature(filter));
		environment.jersey().register(RolesAllowedDynamicFeature.class);
		environment.jersey().register(new AuthValueFactoryProvider.Binder<>(CustomAuthUser.class));

		environment.jersey().packages("nz.co.fit.projectmanagement.server.resources");

		environment.lifecycle().manage(new ManagedHistoryService(sessionFactory));
	}

}

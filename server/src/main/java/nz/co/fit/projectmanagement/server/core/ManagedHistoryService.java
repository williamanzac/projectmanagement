package nz.co.fit.projectmanagement.server.core;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import io.dropwizard.lifecycle.Managed;
import nz.co.fit.projectmanagement.server.dao.HistoryEvent;
import nz.co.fit.projectmanagement.server.dao.entities.HistoryModel;

public class ManagedHistoryService implements Managed {

	private final SessionFactory sessionFactory;
	// private final HistoryDAO historyDAO;

	public ManagedHistoryService(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void start() throws Exception {
		EventBus.getDefault().register(this);
	}

	@Override
	public void stop() throws Exception {
		EventBus.getDefault().unregister(this);
	}

	@Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
	public void comsumeEvent(final HistoryEvent event) throws ServiceException {
		final HistoryModel history = new HistoryModel();
		history.setEntityClass(event.getEntityClass());
		history.setFieldName(event.getFieldName());
		history.setNewValue(event.getNewValue());
		history.setObjectId(event.getObjectId());
		history.setOldValue(event.getOldValue());

		createHistory(history);
	}

	// this will be called in the same session and transaction, so we do not need to create one here
	private void createHistory(final HistoryModel history) {
		final Session session = sessionFactory.getCurrentSession();
		try {
			// ManagedSessionContext.bind(session);
			// final Transaction transaction = session.beginTransaction();
			try {
				session.persist(history);
				// transaction.commit();
			} catch (final Exception e) {
				// transaction.rollback();
				throw new RuntimeException(e);
			}
		} finally {
			// session.close();
			// ManagedSessionContext.unbind(sessionFactory);
		}
	}
}

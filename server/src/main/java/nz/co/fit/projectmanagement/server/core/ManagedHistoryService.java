package nz.co.fit.projectmanagement.server.core;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import io.dropwizard.lifecycle.Managed;
import nz.co.fit.projectmanagement.server.dao.DeleteEvent;
import nz.co.fit.projectmanagement.server.dao.UpdateEvent;
import nz.co.fit.projectmanagement.server.dao.entities.HistoryModel;
import nz.co.fit.projectmanagement.server.dao.entities.IdableModel;

public class ManagedHistoryService implements Managed {

	private final SessionFactory sessionFactory;

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
	public void comsumeEvent(final UpdateEvent event) throws ServiceException {
		final HistoryModel history = new HistoryModel();
		history.setEntityClass(event.getEntityClass());
		history.setFieldName(event.getFieldName());
		history.setNewValue(event.getNewValue());
		history.setObjectId(event.getObjectId());
		history.setOldValue(event.getOldValue());

		createHistory(history);
	}

	@Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
	public void comsumeEvent(final DeleteEvent event) throws ServiceException {
		deleteHistory(event.getEntityClass(), event.getObjectId());
	}

	// this will be called in the same session and transaction, so we do not need to create one here
	private void createHistory(final HistoryModel history) {
		final Session session = sessionFactory.getCurrentSession();
		try {
			session.persist(history);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	// this will be called in the same session and transaction, so we do not need to create one here
	private void deleteHistory(final String entityClass, final Long id) {
		final Session session = sessionFactory.getCurrentSession();
		try {
			final List<HistoryModel> historyForObject = historyForObject(id, entityClass);
			historyForObject.forEach(h -> session.delete(h));
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public <E extends IdableModel> List<HistoryModel> historyForObject(final Long id, final String entityClass) {
		final Session session = sessionFactory.getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<HistoryModel> query = cb.createQuery(HistoryModel.class);
		final Root<HistoryModel> root = query.from(HistoryModel.class);
		query.where(cb.and(cb.equal(root.get("objectId"), id), cb.equal(root.get("entityClass"), entityClass)));
		query.select(root);
		return session.createQuery(requireNonNull(query)).getResultList();
	}
}

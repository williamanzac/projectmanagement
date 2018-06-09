package nz.co.fit.projectmanagement.server.core;

import javax.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.ComponentDAO;
import nz.co.fit.projectmanagement.server.dao.DAOException;
import nz.co.fit.projectmanagement.server.dao.HistoryEvent;
import nz.co.fit.projectmanagement.server.dao.entities.ComponentModel;

@Service
public class ComponentService {

	private final ComponentDAO componentDAO;
	private final HistoryService historyService;

	@Inject
	public ComponentService(final ComponentDAO componentDAO, final HistoryService historyService) {
		this.componentDAO = componentDAO;
		this.historyService = historyService;
	}

	public ComponentModel createComponent(final ComponentModel component) throws ServiceException {
		try {
			return componentDAO.upsert(component);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public ComponentModel readComponent(final Long id) throws ServiceException {
		try {
			return componentDAO.read(id);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public ComponentModel updateComponent(final ComponentModel component) throws ServiceException {
		ComponentModel existing;
		try {
			existing = componentDAO.read(component.getId());
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
		if (component.getDescription() != null) {
			if (!component.getDescription().equals(existing.getDescription())) {
				EventBus.getDefault().post(new HistoryEvent("description", existing.getDescription(),
						component.getDescription(), component));
			}
			existing.setDescription(component.getDescription());
		}
		if (component.getName() != null) {
			if (!component.getName().equals(existing.getName())) {
				EventBus.getDefault()
						.post(new HistoryEvent("name", existing.getName(), component.getName(), component));
			}
			existing.setName(component.getName());
		}
		try {
			return componentDAO.upsert(existing);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public void deleteComponent(final Long id) throws ServiceException {
		historyService.deleteHistoryForObject(id, ComponentModel.class);
		try {
			componentDAO.delete(id);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}
}

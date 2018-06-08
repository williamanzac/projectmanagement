package nz.co.fit.projectmanagement.server.core;

import javax.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.ComponentDAO;
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

	public ComponentModel createComponent(final ComponentModel component) {
		return componentDAO.upsert(component);
	}

	public ComponentModel readComponent(final Long id) {
		return componentDAO.read(id);
	}

	public ComponentModel updateComponent(final ComponentModel component) {
		final ComponentModel existing = componentDAO.read(component.getId());
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
		return componentDAO.upsert(existing);
	}

	public void deleteComponent(final Long id) {
		historyService.deleteHistoryForObject(id, ComponentModel.class);
		componentDAO.delete(id);
	}
}

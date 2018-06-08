package nz.co.fit.projectmanagement.server.core;

import javax.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.HistoryEvent;
import nz.co.fit.projectmanagement.server.dao.VersionDAO;
import nz.co.fit.projectmanagement.server.dao.entities.VersionModel;

@Service
public class VersionService {

	private final VersionDAO versionDAO;
	private final HistoryService historyService;

	@Inject
	public VersionService(final VersionDAO versionDAO, final HistoryService historyService) {
		this.versionDAO = versionDAO;
		this.historyService = historyService;
	}

	public VersionModel createVersion(final VersionModel version) {
		return versionDAO.upsert(version);
	}

	public VersionModel readVersion(final Long id) {
		return versionDAO.read(id);
	}

	public VersionModel updateVersion(final VersionModel version) {
		final VersionModel existing = versionDAO.read(version.getId());
		if (version.getDescription() != null) {
			if (!version.getDescription().equals(existing.getDescription())) {
				EventBus.getDefault().post(
						new HistoryEvent("description", existing.getDescription(), version.getDescription(), version));
			}
			existing.setDescription(version.getDescription());
		}
		if (version.getName() != null) {
			if (!version.getName().equals(existing.getName())) {
				EventBus.getDefault().post(new HistoryEvent("name", existing.getName(), version.getName(), version));
			}
			existing.setName(version.getName());
		}
		if (version.getArchivedDate() != null) {
			if (!version.getArchivedDate().equals(existing.getArchivedDate())) {
				EventBus.getDefault()
						.post(new HistoryEvent("archivedDate", String.valueOf(existing.getArchivedDate().getTime()),
								String.valueOf(version.getArchivedDate().getTime()), version));
			}
			existing.setArchivedDate(version.getArchivedDate());
		}
		if (version.getPriority() != null) {
			if (!version.getPriority().equals(existing.getPriority())) {
				EventBus.getDefault().post(new HistoryEvent("priority", String.valueOf(existing.getPriority()),
						String.valueOf(version.getPriority()), version));
			}
			existing.setPriority(version.getPriority());
		}
		if (version.getReleaseDate() != null) {
			if (!version.getReleaseDate().equals(existing.getReleaseDate())) {
				EventBus.getDefault()
						.post(new HistoryEvent("releaseDate", String.valueOf(existing.getReleaseDate().getTime()),
								String.valueOf(version.getReleaseDate().getTime()), version));
			}
			existing.setReleaseDate(version.getReleaseDate());
		}
		if (version.getReleasedDate() != null) {
			if (!version.getReleasedDate().equals(existing.getReleasedDate())) {
				EventBus.getDefault()
						.post(new HistoryEvent("releasedDate", String.valueOf(existing.getReleasedDate().getTime()),
								String.valueOf(version.getReleasedDate().getTime()), version));
			}
			existing.setReleasedDate(version.getReleasedDate());
		}
		if (version.getStartDate() != null) {
			if (!version.getStartDate().equals(existing.getStartDate())) {
				EventBus.getDefault()
						.post(new HistoryEvent("startDate", String.valueOf(existing.getStartDate().getTime()),
								String.valueOf(version.getStartDate().getTime()), version));
			}
			existing.setStartDate(version.getStartDate());
		}
		if (version.getStatus() != null) {
			if (!version.getStatus().equals(existing.getStatus())) {
				EventBus.getDefault().post(new HistoryEvent("status", String.valueOf(existing.getStatus()),
						String.valueOf(version.getStatus()), version));
			}
		}
		return versionDAO.upsert(existing);
	}

	public void deleteVersion(final Long id) {
		historyService.deleteHistoryForObject(id, VersionModel.class);
		versionDAO.delete(id);
	}
}

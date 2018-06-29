package nz.co.fit.projectmanagement.server.core;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.ThemeDAO;
import nz.co.fit.projectmanagement.server.dao.UserDAO;
import nz.co.fit.projectmanagement.server.dao.entities.ThemeModel;

@Service
public class ThemeService extends CRUDLService<ThemeModel> {
	@Inject
	public ThemeService(final ThemeDAO themeDAO, final HistoryService historyService, final UserDAO userDAO) {
		super(themeDAO, historyService, userDAO);
	}
}

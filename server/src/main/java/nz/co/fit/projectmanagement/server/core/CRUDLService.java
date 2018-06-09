package nz.co.fit.projectmanagement.server.core;

import java.lang.reflect.ParameterizedType;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import nz.co.fit.projectmanagement.server.auth.CustomAuthUser;
import nz.co.fit.projectmanagement.server.dao.BaseDAO;
import nz.co.fit.projectmanagement.server.dao.DAOException;
import nz.co.fit.projectmanagement.server.dao.UserDAO;
import nz.co.fit.projectmanagement.server.dao.entities.BaseModel;
import nz.co.fit.projectmanagement.server.dao.entities.IdableModel;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

public abstract class CRUDLService<T extends IdableModel> {

	final BaseDAO<T> dao;
	private final HistoryService historyService;
	private final Class<T> entityClass;
	private final UserDAO userDAO;

	@Context
	SecurityContext securityContext;

	@SuppressWarnings("unchecked")
	public CRUDLService(final BaseDAO<T> dao, final HistoryService historyService, final UserDAO userDAO) {
		this.dao = dao;
		this.historyService = historyService;
		this.userDAO = userDAO;
		entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public T create(final T value) throws ServiceException {
		try {
			if (securityContext != null && value instanceof BaseModel) {
				final Principal userPrincipal = securityContext.getUserPrincipal();
				System.out.println("principal name:" + userPrincipal.getName());
				final CustomAuthUser authUser = (CustomAuthUser) userPrincipal;
				final Long userId = authUser.getUserId();
				System.out.println("user id:" + userId);
				if (userId != null) {
					final UserModel currentUser = userDAO.read(userId);
					((BaseModel) value).setCreatedBy(currentUser);
					((BaseModel) value).setUpdatedBy(currentUser);
					((BaseModel) value).setCreatedOn(new Date());
				}
			}

			return dao.upsert(value);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public T read(final Long id) throws ServiceException {
		try {
			return dao.read(id);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public T update(final T value) throws ServiceException {
		try {
			if (securityContext != null && value instanceof BaseModel) {
				final Principal userPrincipal = securityContext.getUserPrincipal();
				System.out.println("principal name:" + userPrincipal.getName());
				final CustomAuthUser authUser = (CustomAuthUser) userPrincipal;
				final Long userId = authUser.getUserId();
				System.out.println("user id:" + userId);
				if (userId != null) {
					final UserModel currentUser = userDAO.read(userId);
					((BaseModel) value).setUpdatedBy(currentUser);
				}
			}
			return dao.upsert(value);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public void delete(final Long id) throws ServiceException {
		try {
			// make sure to remove any history data as there is not automatic link
			historyService.deleteHistoryForObject(id, entityClass);
			dao.delete(id);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	public List<T> list() throws ServiceException {
		try {
			return dao.list();
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}
}

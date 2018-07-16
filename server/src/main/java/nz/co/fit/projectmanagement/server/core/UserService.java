package nz.co.fit.projectmanagement.server.core;

import static nz.co.fit.projectmanagement.server.dao.AuthType.PASSWORD;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import nz.co.fit.projectmanagement.server.dao.UserDAO;
import nz.co.fit.projectmanagement.server.dao.entities.UserModel;

@Service
public class UserService extends CRUDLService<UserModel> {
	@Inject
	public UserService(final UserDAO userDAO) {
		super(userDAO);
	}

	@Override
	public UserModel create(final UserModel value) throws ServiceException {
		final String password = value.getPassword();
		// make sure that the password is encoded
		if (isNotBlank(password) && value.getAuthType() == PASSWORD) {
			final String encode;
			try {
				encode = PasswordUtilities.encode(password);
			} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
				throw new ServiceException(e);
			}
			value.setPassword(encode);
		}
		return super.create(value);
	}

	@Override
	public UserModel update(final UserModel value) throws ServiceException {
		final String password = value.getPassword();
		// make sure that the password is encoded
		if (isNotBlank(password) && value.getAuthType() == PASSWORD) {
			final String encode;
			try {
				encode = PasswordUtilities.encode(password);
			} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
				throw new ServiceException(e);
			}
			value.setPassword(encode);
		}
		return super.update(value);
	}

	public UserModel findByUsername(final String username) {
		return ((UserDAO) dao).findByUsername(username);
	}
}

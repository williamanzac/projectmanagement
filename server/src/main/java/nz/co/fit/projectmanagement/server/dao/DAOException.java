package nz.co.fit.projectmanagement.server.dao;

public class DAOException extends Exception {

	private static final long serialVersionUID = -9222802986784179032L;

	public DAOException() {
	}

	public DAOException(final String message) {
		super(message);
	}

	public DAOException(final Throwable cause) {
		super(cause);
	}

	public DAOException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public DAOException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}

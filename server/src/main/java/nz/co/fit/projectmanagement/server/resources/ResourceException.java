package nz.co.fit.projectmanagement.server.resources;

public class ResourceException extends Exception {

	private static final long serialVersionUID = 4370851438048084607L;

	public ResourceException() {
	}

	public ResourceException(final String message) {
		super(message);
	}

	public ResourceException(final Throwable cause) {
		super(cause);
	}

	public ResourceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ResourceException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}

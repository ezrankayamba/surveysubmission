package tz.co.nezatech.dev.surveysubmission.storage;

public class StorageException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8337979475827122722L;

	public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}

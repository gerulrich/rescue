package net.cloudengine.service.statistics;

public class ReportException extends Exception {

	private static final long serialVersionUID = 1L;

	public ReportException() {

	}

	public ReportException(String message) {
		super(message);
	}

	public ReportException(Throwable cause) {
		super(cause);
	}

	public ReportException(String message, Throwable cause) {
		super(message, cause);
	}

}

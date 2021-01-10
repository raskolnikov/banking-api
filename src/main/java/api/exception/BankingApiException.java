package api.exception;

/**
 * Created by Mehmet Aktas on 2020-12-11
 */

public class BankingApiException extends RuntimeException {

	private static final long serialVersionUID = -7785949343623980782L;

	public BankingApiException() {
		super();
	}

	public BankingApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public BankingApiException(String message) {
		super(message);
	}

	public BankingApiException(Throwable cause) {
		super(cause);
	}


}

package api.exception;

/**
 * Created by Mehmet Aktas on 2020-12-11
 */

public class InsufficientBalanceException extends BankingApiException {

	public InsufficientBalanceException(String message) {

		super(message);
	}


}

package api.exception;

/**
 * Created by Mehmet Aktas on 2020-12-11
 */

public class InvalidRequestParamException extends BankingApiException {

	public InvalidRequestParamException(String message) {

		super(message);
	}


}

package api.exception;

/**
 * Created by Mehmet Aktas on 2020-12-11
 */


public class NotFoundException extends BankingApiException {

	private Long itemId;

	public NotFoundException(String message) {

		super(message);
	}

	public NotFoundException(String message, Long itemId) {

		this(message);
		this.itemId = itemId;
	}

}

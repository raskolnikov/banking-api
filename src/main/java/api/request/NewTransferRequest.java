package api.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Created by Mehmet Aktas on 2020-12-11
 */

@Getter
@Setter
public class NewTransferRequest {

	@NotNull(message = "Source account is mandatory")
	private Long accountId;
	@NotNull(message = "Destination account is mandatory")
	private Long destinationAccountId;
	private String description;
	@NotNull(message = "Amount is mandatory")
	private Long amount;

}

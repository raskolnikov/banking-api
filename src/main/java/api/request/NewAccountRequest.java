package api.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Created by Mehmet Aktas on 2020-12-10
 */

@Getter
@Setter
public class NewAccountRequest {

	@NotNull(message = "Customer is mandatory")
	private Long customerId;
	@NotNull(message = "Initial balance is mandatory")
	private Long initialBalance;
	@NotEmpty(message = "Account name is mandatory")
	private String accountName;

}

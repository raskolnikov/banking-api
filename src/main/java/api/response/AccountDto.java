package api.response;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

/**
 * Created by Mehmet Aktas on 2020-12-10
 */

@Getter
@Setter
public class AccountDto {

	private Long id;
	private String name;
	private Long customerId;
	private Long balance;
	private DateTime createdAt;
	private DateTime updatedAt;
}
